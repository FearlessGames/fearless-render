package se.fearlessgames.fear.collada.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataCache {
	private final Map<String, Element> idCache;
	private final Map<String, Element> sidCache;
	private final Map<String, XPath> xPathExpressions;
	private final Pattern pattern;
	private final List<String> transformTypes;

	private final Map<Element, float[]> floatArrays;
	private final Map<Element, double[]> doubleArrays;
	private final Map<Element, boolean[]> booleanArrays;
	private final Map<Element, int[]> intArrays;
	private final Map<Element, String[]> stringArrays;

	private final Multimap<Element, MeshVertPairs> vertMappings;
	private final Map<Mesh, VertMap> meshVertMap;


	//private final Map<Element, Joint> _elementJointMapping;
	//private JointNode _rootJointNode;
	//private final Map<Joint, Skeleton> _jointSkeletonMapping;
	//private final Map<Skeleton, SkeletonPose> _skeletonPoseMapping;
	//private final List<Skeleton> _skeletons;
	//private final List<ControllerStore> _controllers;

	public DataCache() {
		idCache = Maps.newHashMap();
		sidCache = Maps.newHashMap();
		xPathExpressions = Maps.newHashMap();
		pattern = Pattern.compile("\\s");

		transformTypes = Collections.unmodifiableList(Lists.newArrayList("lookat", "matrix", "rotate", "scale", "scew", "translate"));

		floatArrays = Maps.newHashMap();
		doubleArrays = Maps.newHashMap();
		booleanArrays = Maps.newHashMap();
		intArrays = Maps.newHashMap();
		stringArrays = Maps.newHashMap();
		vertMappings = ArrayListMultimap.create();
		meshVertMap = Maps.newIdentityHashMap();


		//_elementJointMapping = Maps.newHashMap();
		//_skeletons = Lists.newArrayList();
		//_jointSkeletonMapping = Maps.newHashMap();
		//_skeletonPoseMapping = Maps.newHashMap();
		//_controllers = Lists.newArrayList();
	}


	public Map<String, Element> getIdCache() {
		return idCache;
	}

	public Map<String, Element> getSidCache() {
		return sidCache;
	}

	public Map<String, XPath> getxPathExpressions() {
		return xPathExpressions;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public List<String> getTransformTypes() {
		return transformTypes;
	}

	public Map<Element, float[]> getFloatArrays() {
		return floatArrays;
	}

	public Map<Element, double[]> getDoubleArrays() {
		return doubleArrays;
	}

	public Map<Element, boolean[]> getBooleanArrays() {
		return booleanArrays;
	}

	public Map<Element, int[]> getIntArrays() {
		return intArrays;
	}

	public Map<Element, String[]> getStringArrays() {
		return stringArrays;
	}

	public Multimap<Element, MeshVertPairs> getVertMappings() {
		return vertMappings;
	}

	public Map<Mesh, VertMap> getMeshVertMap() {
		return meshVertMap;
	}


	/*public Map<Element, Joint> getElementJointMapping() {
		return _elementJointMapping;
	}

	public JointNode getRootJointNode() {
		return _rootJointNode;
	}

	public void setRootJointNode(final JointNode rootJointNode) {
		_rootJointNode = rootJointNode;
	}

	public Map<Joint, Skeleton> getJointSkeletonMapping() {
		return _jointSkeletonMapping;
	}

	public Map<Skeleton, SkeletonPose> getSkeletonPoseMapping() {
		return _skeletonPoseMapping;
	}

	public List<ControllerStore> getControllers() {
		return _controllers;
	}

	public List<Skeleton> getSkeletons() {
		return _skeletons;
	}

	public void addSkeleton(final Skeleton skeleton) {
		_skeletons.add(skeleton);
	}*/

	public void setMeshVertMap(final Mesh geometry, final VertMap map) {
		meshVertMap.put(geometry, map);
	}
}
