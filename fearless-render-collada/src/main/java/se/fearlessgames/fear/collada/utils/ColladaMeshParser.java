package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.mesh.MeshData;

import java.util.ArrayList;
import java.util.List;


public class ColladaMeshParser {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final DataCache dataCache;
	private final ColladaDOMUtil colladaDOMUtil;
	private final List<ColladaPrimitiveParser> primitiveParsers;

	public ColladaMeshParser(final DataCache dataCache,
							 final ColladaDOMUtil colladaDOMUtil) {
		this.dataCache = dataCache;
		this.colladaDOMUtil = colladaDOMUtil;

		primitiveParsers = new ArrayList<ColladaPrimitiveParser>();
		primitiveParsers.add(new ColladaPolygonsParser(colladaDOMUtil, dataCache));
		primitiveParsers.add(new ColladaPolyListParser(colladaDOMUtil, dataCache));
		primitiveParsers.add(new ColladaTrianglesParser(colladaDOMUtil, dataCache));
	}

	public Node getGeometryMesh(final Element instanceGeometry) {
		final Element geometry = colladaDOMUtil.findTargetWithId(instanceGeometry.getAttributeValue("url"));

		if (geometry != null) {
			return buildMesh(geometry);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Node buildMesh(final Element colladaGeometry) {
		if (colladaGeometry.getChild("mesh") != null) {
			final Element cMesh = colladaGeometry.getChild("mesh");
			final Node meshNode = new Node(colladaGeometry.getAttributeValue("name", colladaGeometry.getName()));

			boolean hasChild = false;

			for (ColladaPrimitiveParser primitiveParser : primitiveParsers) {
				if (primitiveParser.isParserFor(cMesh)) {
					for (Element element : primitiveParser.getChildren(cMesh)) {
						final MeshData child = primitiveParser.buildMesh(colladaGeometry, element);
						if (child != null) {
							if (child.getName() == null) {
								child.setName(meshNode.getName() + "_" + primitiveParser.getElementName());
							}
							meshNode.attachChild(child);
							hasChild = true;
						}
					}
				}
			}

			if (!hasChild) {
				logger.error("No valid child found");
			}

			return meshNode;
		}
		return null;
	}


}
