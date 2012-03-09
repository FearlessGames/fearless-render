package se.fearlessgames.fear.collada.utils;

import com.google.common.collect.Lists;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.collada.ColladaException;
import se.fearlessgames.fear.collada.data.*;
import se.fearlessgames.fear.math.*;

import java.util.ArrayList;
import java.util.List;

public class ColladaNodeParser {
	private final Logger logger = LoggerFactory.getLogger(ColladaNodeParser.class);

	private final DataCache _dataCache;
	private final ColladaDOMUtil _colladaDOMUtil;
	private final ColladaMeshParser colladaMeshParser;

	public ColladaNodeParser(DataCache _dataCache, ColladaDOMUtil _colladaDOMUtil, ColladaMeshParser _colladaMeshUtil) {
		this._dataCache = _dataCache;
		this._colladaDOMUtil = _colladaDOMUtil;
		this.colladaMeshParser = _colladaMeshUtil;
	}

	@SuppressWarnings("unchecked")
	public Node getVisualScene(final Element colladaRoot) {
		if (colladaRoot.getChild("scene") == null) {
			logger.warn("No scene found in collada file!");
			return null;
		}

		final Element instanceVisualScene = colladaRoot.getChild("scene").getChild("instance_visual_scene");
		if (instanceVisualScene == null) {
			logger.warn("No instance_visual_scene found in collada file!");
			return null;
		}

		final Element visualScene = _colladaDOMUtil.findTargetWithId(instanceVisualScene.getAttributeValue("url"));

		if (visualScene != null) {
			final Node sceneRoot = new Node(visualScene.getAttributeValue("name") != null ? visualScene.getAttributeValue("name") : "Collada Root");

			// Load each sub node and attach
			final JointNode jointNode = new JointNode(null);
			_dataCache.setRootJointNode(jointNode);
			for (final Element n : (List<Element>) visualScene.getChildren("node")) {
				final Node subNode = buildNode(n, jointNode);
				if (subNode != null) {
					sceneRoot.attachChild(subNode);
				}
			}

			final List<List<Joint>> jointCollection = Lists.newArrayList();
			for (final JointNode jointChildNode : _dataCache.getRootJointNode().getChildren()) {
				final List<Joint> jointList = Lists.newArrayList();
				buildJointLists(jointChildNode, jointList);
				jointCollection.add(jointList);
			}

			for (final List<Joint> jointList : jointCollection) {
				final Joint[] joints = jointList.toArray(new Joint[jointList.size()]);
				final Skeleton skeleton = new Skeleton(joints[0].getName() + "_skeleton", joints);
				logger.info(skeleton.getName());
				for (final Joint joint : jointList) {
					_dataCache.getJointSkeletonMapping().put(joint, skeleton);
					logger.info("- Joint " + joint.getName() + " - index: " + joint.getIndex() + " parent index: "
							+ joint.getParentIndex());
				}
				_dataCache.addSkeleton(skeleton);
			}

			return sceneRoot;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Node buildNode(final Element dNode, JointNode jointNode) {
		NodeType nodeType = NodeType.NODE;
		if (dNode.getAttribute("type") != null) {
			nodeType = Enum.valueOf(NodeType.class, dNode.getAttributeValue("type"));
		}
		if (nodeType == NodeType.JOINT) {
			String name = dNode.getAttributeValue("name");
			if (name == null) {
				name = dNode.getAttributeValue("id");
			}
			if (name == null) {
				name = dNode.getAttributeValue("sid");
			}
			final Joint joint = new Joint(name);
			final JointNode jointChildNode = new JointNode(joint);
			jointChildNode.setParent(jointNode);
			jointNode.getChildren().add(jointChildNode);
			jointNode = jointChildNode;

			_dataCache.getElementJointMapping().put(dNode, joint);
		}

		String nodeName = dNode.getAttributeValue("name", (String) null);
		if (nodeName == null) { // use id if name doesn't exist
			nodeName = dNode.getAttributeValue("id", dNode.getName());
		}
		final Node node = new Node(nodeName);

		final List<Element> transforms = new ArrayList<Element>();
		for (final Element child : (List<Element>) dNode.getChildren()) {
			if (_dataCache.getTransformTypes().contains(child.getName())) {
				transforms.add(child);
			}
		}

		// process any transform information.
		if (!transforms.isEmpty()) {
			node.setTransform(getNodeTransforms(transforms));
		}

		// process any instance geometries
		for (final Element instance_geometry : (List<Element>) dNode.getChildren("instance_geometry")) {
			Node mesh = colladaMeshParser.getGeometryMesh(instance_geometry);
			if (mesh != null) {
				node.attachChild(mesh);
			}
		}

		// process any instance nodes
		for (final Element in : (List<Element>) dNode.getChildren("instance_node")) {
			final Node subNode = getNode(in, jointNode);
			if (subNode != null) {
				node.attachChild(subNode);
			}
		}

		// process any concrete child nodes.
		for (final Element n : (List<Element>) dNode.getChildren("node")) {
			final Node subNode = buildNode(n, jointNode);
			if (subNode != null) {
				node.attachChild(subNode);
			}
		}

		return node;
	}

	public Node getNode(final Element instanceNode, final JointNode jointNode) {
		final Element node = _colladaDOMUtil.findTargetWithId(instanceNode.getAttributeValue("url"));

		if (node == null) {
			throw new ColladaException("No node with id: " + instanceNode.getAttributeValue("url") + " found", instanceNode);
		}

		return buildNode(node, jointNode);
	}

	private void buildJointLists(final JointNode jointNode, final List<Joint> jointList) {
		final Joint joint = jointNode.getJoint();
		joint.setIndex((short) jointList.size());
		if (jointNode.getParent().getJoint() != null) {
			joint.setParentIndex(jointNode.getParent().getJoint().getIndex());
		} else {
			joint.setParentIndex(Joint.NO_PARENT);
		}
		jointList.add(joint);
		for (final JointNode jointChildNode : jointNode.getChildren()) {
			buildJointLists(jointChildNode, jointList);
		}
	}

	public Matrix4 getNodeTransforms(final List<Element> transforms) {
		Matrix4 finalMat = Matrix4.IDENTITY;

		for (final Element transform : transforms) {
			final double[] array = _colladaDOMUtil.parseDoubleArray(transform);
			if ("translate".equals(transform.getName())) {
				Matrix4 matrix4 = new Matrix4().setColumn(3, new double[]{array[0], array[1], array[2], 1});
				finalMat = finalMat.multiply(matrix4);

			} else if ("rotate".equals(transform.getName())) {
				if (array[3] != 0) {
					final Matrix3 rotate = Matrix3.fromAngleAxis(array[3] * MathUtils.DEG_TO_RAD, new Vector3(array[0], array[1], array[2]));
					Matrix4 matrix4 = new Matrix4(rotate);
					finalMat = finalMat.multiply(matrix4);
				}

			} else if ("scale".equals(transform.getName())) {
				Matrix4 matrix4 = new Matrix4();
				matrix4.scale(new Vector4(array[0], array[1], array[2], 1));
				finalMat = finalMat.multiply(matrix4);

			} else if ("matrix".equals(transform.getName())) {
				Matrix4 matrix4 = new Matrix4(array);
				finalMat = finalMat.multiply(matrix4);

			} else if ("lookat".equals(transform.getName())) {
				final Vector3 pos = new Vector3(array[0], array[1], array[2]);
				final Vector3 target = new Vector3(array[3], array[4], array[5]);
				final Vector3 up = new Vector3(array[6], array[7], array[8]);
				final Matrix3 rot = new Matrix3();
				rot.lookAt(target.subtract(pos), up);
				Matrix4 matrix4 = new Matrix4(rot).setColumn(3, new double[]{array[0], array[1], array[2], 1});
				finalMat = finalMat.multiply(matrix4);

			} else {
				logger.warn("transform not currently supported: " + transform.getClass().getCanonicalName());
			}
		}

		return finalMat;
	}


}
