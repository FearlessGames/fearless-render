package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.collada.data.ColladaInputPipe;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public abstract class ColladaPrimitiveParser {
	private Logger logger = LoggerFactory.getLogger(getClass());

	protected final ColladaDOMUtil colladaDOMUtil;
	protected final DataCache dataCache;

	protected ColladaPrimitiveParser(ColladaDOMUtil colladaDOMUtil, DataCache dataCache) {
		this.colladaDOMUtil = colladaDOMUtil;
		this.dataCache = dataCache;
	}

	public abstract MeshData buildMesh(Element colladaGeometry, Element element);

	public boolean isParserFor(Element mesh) {
		return mesh.getChild(getElementName()) != null;
	}

	@SuppressWarnings("unchecked")
	public List<Element> getChildren(Element cMesh) {
		return (List<Element>) cMesh.getChildren(getElementName());
	}

	protected abstract String getElementName();

	/**
	 * Extract name from xml element, some exporters don't support 'name' attribute, so we better use the material
	 * instead of a generic name.
	 *
	 * @param element
	 * @return value from 'name' or 'material' attribute
	 */
	protected String extractName(final Element colladaGeometry, final Element element) {
		// Try to get mesh name
		String name = element.getAttributeValue("name");
		if (name == null || name.isEmpty()) {
			// No mesh name found, try to get mesh id
			name = element.getAttributeValue("id");
		}
		if (name == null || name.isEmpty()) {
			// No mesh name or id found, try to get parent geometry name
			name = colladaGeometry.getAttributeValue("name");
			if (name == null || name.isEmpty()) {
				// No parent geometry name found, try to get geometry id (mandatory according to spec)
				name = colladaGeometry.getAttributeValue("id");
			}
			if (name == null) {
				name = "";
			}

			// Since we have retrieved the parent geometry name/id, we append the material(if any),
			// to make identification unique.
			final String materialName = element.getAttributeValue("material");
			if (materialName != null && !materialName.isEmpty()) {
				name += "[" + materialName + "]";
			}
		}

		return name;
	}

	/**
	 * Extract our pipes from the given parent element.
	 *
	 * @param inputsParent
	 * @param pipesStore   the store for our pipes
	 * @return the max offset of our pipes.
	 */
	@SuppressWarnings("unchecked")
	protected int extractPipes(final Element inputsParent, final LinkedList<ColladaInputPipe> pipesStore) {
		int maxOffset = 0;
		int texCount = 0;
		for (final Element input : (List<Element>) inputsParent.getChildren("input")) {
			maxOffset = Math.max(maxOffset, colladaDOMUtil.getAttributeIntValue(input, "offset", 0));
			try {
				final ColladaInputPipe.Type type = ColladaInputPipe.Type.valueOf(input.getAttributeValue("semantic"));
				if (type == ColladaInputPipe.Type.VERTEX) {
					final Element vertexElement = colladaDOMUtil.findTargetWithId(input.getAttributeValue("source"));
					for (final Element vertexInput : (List<Element>) vertexElement.getChildren("input")) {
						vertexInput.setAttribute("offset", input.getAttributeValue("offset"));
						vertexInput.setAttribute("isVertexDefined", "true");
						pipesStore.add(new ColladaInputPipe(colladaDOMUtil, vertexInput));
					}
				} else {
					final ColladaInputPipe pipe = new ColladaInputPipe(colladaDOMUtil, input);
					if (pipe.getType() == ColladaInputPipe.Type.TEXCOORD) {
						pipe.setTexCoord(texCount++);
					}
					pipesStore.add(pipe);
				}
			} catch (final Exception ex) {
				logger.warn("Unknown input type: " + input.getAttributeValue("semantic"));
				continue;
			}
		}
		return maxOffset;
	}

	protected IntBuffer createIndexBufferData(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	/**
	 * Push the values at the given indices of currentVal onto the buffers defined in pipes.
	 *
	 * @param pipes
	 * @param currentVal
	 * @return the vertex index referenced in the given indices based on the pipes. Integer.MIN_VALUE is returned if no
	 *         vertex pipe is found.
	 */
	protected int processPipes(final LinkedList<ColladaInputPipe> pipes, final int[] currentVal) {
		// go through our pipes. use the indices in currentVal to pull the correct float val
		// from our source and set into our buffer.
		int rVal = Integer.MIN_VALUE;
		for (final ColladaInputPipe pipe : pipes) {
			pipe.pushValues(currentVal[pipe.getOffset()]);
			if (pipe.getType() == ColladaInputPipe.Type.POSITION) {
				rVal = currentVal[pipe.getOffset()];
			}
		}
		return rVal;
	}


}
