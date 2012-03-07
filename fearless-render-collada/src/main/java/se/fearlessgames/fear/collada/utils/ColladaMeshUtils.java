package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.collada.data.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;


public class ColladaMeshUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final DataCache _dataCache;
	private final ColladaDOMUtil _colladaDOMUtil;

	public ColladaMeshUtils(final DataCache dataCache,
							final ColladaDOMUtil colladaDOMUtil) {
		_dataCache = dataCache;
		_colladaDOMUtil = colladaDOMUtil;

	}

	public Node getGeometryMesh(final Element instanceGeometry) {
		final Element geometry = _colladaDOMUtil.findTargetWithId(instanceGeometry.getAttributeValue("url"));

		if (geometry != null) {
			return buildMesh(geometry);
		}
		return null;
	}

	/**
	 * Builds a mesh from a Collada geometry element. Currently supported mesh types: mesh, polygons, polylist,
	 * triangles, lines. Not supported yet: linestrips, trifans, tristrips. If no meshtype is found, a pointcloud is
	 * built.
	 *
	 * @param colladaGeometry
	 * @return a Node containing all of the Ardor3D meshes we've parsed from this geometry element.
	 */
	@SuppressWarnings("unchecked")
	public Node buildMesh(final Element colladaGeometry) {
		if (colladaGeometry.getChild("mesh") != null) {
			final Element cMesh = colladaGeometry.getChild("mesh");
			final Node meshNode = new Node(colladaGeometry.getAttributeValue("name", colladaGeometry.getName()));

			// Grab all mesh types (polygons, triangles, etc.)
			// Create each as an Ardor3D Mesh, and attach to node
			boolean hasChild = false;
			if (cMesh.getChild("polygons") != null) {
				for (final Element p : (List<Element>) cMesh.getChildren("polygons")) {
					final Mesh child = buildMeshPolygons(colladaGeometry, p);
					if (child != null) {
						if (child.getName() == null) {
							child.setName(meshNode.getName() + "_polygons");
						}
						meshNode.attachChild(child);
						hasChild = true;
					}
				}
			}
			if (cMesh.getChild("polylist") != null) {
				for (final Element p : (List<Element>) cMesh.getChildren("polylist")) {
					final Mesh child = buildMeshPolylist(colladaGeometry, p);
					if (child != null) {
						if (child.getName() == null) {
							child.setName(meshNode.getName() + "_polylist");
						}
						meshNode.attachChild(child);
						hasChild = true;
					}
				}
			}
			if (cMesh.getChild("triangles") != null) {
				for (final Element t : (List<Element>) cMesh.getChildren("triangles")) {
					final Mesh child = buildMeshTriangles(colladaGeometry, t);
					if (child != null) {
						if (child.getName() == null) {
							child.setName(meshNode.getName() + "_triangles");
						}
						meshNode.attachChild(child);
						hasChild = true;
					}
				}
			}

			// If we did not find a valid child, the spec says to add verts as a "cloud of points"
			if (!hasChild) {
				logger.error("No valid child found");
			}

			return meshNode;
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public Mesh buildMeshPolygons(final Element colladaGeometry, final Element polys) {
		if (polys == null || polys.getChild("input") == null) {
			return null;
		}
		final Mesh polyMesh = new Mesh(extractName(colladaGeometry, polys));
		polyMesh.setIndexMode(IndexMode.Triangles);


		final LinkedList<ColladaInputPipe> pipes = new LinkedList<ColladaInputPipe>();
		final int maxOffset = extractPipes(polys, pipes);
		final int interval = maxOffset + 1;

		// use interval & sum of sizes of p entries to determine buffer sizes.
		int numEntries = 0;
		int numIndices = 0;
		for (final Element vals : (List<Element>) polys.getChildren("p")) {
			final int length = _colladaDOMUtil.parseIntArray(vals).length;
			numEntries += length;
			numIndices += (length / interval - 2) * 3;
		}
		numEntries /= interval;

		// Construct nio buffers for specified inputs.
		for (final ColladaInputPipe pipe : pipes) {
			pipe.setupBuffer(numEntries, polyMesh);
		}

		// Add to vert mapping
		final int[] indices = new int[numEntries];
		final MeshVertPairs mvp = new MeshVertPairs(polyMesh, indices);
		_dataCache.getVertMappings().put(colladaGeometry, mvp);

		// Prepare indices buffer

		IntBuffer meshIndices = createIndexBufferData(polyMesh.getVertexCount());
		polyMesh.setIndices(meshIndices);

		// go through the polygon entries
		int firstIndex = 0, vecIndex;
		final int[] currentVal = new int[interval];
		for (final Element dia : (List<Element>) polys.getChildren("p")) {
			// for each p, iterate using max offset
			final int[] vals = _colladaDOMUtil.parseIntArray(dia);

			final int first = firstIndex + 0;
			System.arraycopy(vals, 0, currentVal, 0, interval);
			vecIndex = processPipes(pipes, currentVal);
			if (vecIndex != Integer.MIN_VALUE) {
				indices[firstIndex + 0] = vecIndex;
			}

			int prev = firstIndex + 1;
			System.arraycopy(vals, interval, currentVal, 0, interval);
			vecIndex = processPipes(pipes, currentVal);
			if (vecIndex != Integer.MIN_VALUE) {
				indices[firstIndex + 1] = vecIndex;
			}

			// first add the first two entries to the buffers.

			// Now go through remaining entries and create a polygon as a triangle fan.
			for (int j = 2, max = vals.length / interval; j < max; j++) {
				// add first as index
				meshIndices.put(first);
				// add prev as index
				meshIndices.put(prev);

				// set prev to current
				prev = firstIndex + j;
				// add current to buffers
				System.arraycopy(vals, j * interval, currentVal, 0, interval);
				vecIndex = processPipes(pipes, currentVal);
				if (vecIndex != Integer.MIN_VALUE) {
					indices[firstIndex + j] = vecIndex;
				}
				// add current as index
				meshIndices.put(prev);
			}
			firstIndex += vals.length / interval;
		}

		return polyMesh;
	}

	private IntBuffer createIndexBufferData(int size) {
		final IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
		buf.clear();
		return buf;
	}

	public Mesh buildMeshPolylist(final Element colladaGeometry, final Element polys) {
		if (polys == null || polys.getChild("input") == null) {
			return null;
		}
		final Mesh polyMesh = new Mesh(extractName(colladaGeometry, polys));
		polyMesh.setIndexMode(IndexMode.Triangles);


		final LinkedList<ColladaInputPipe> pipes = new LinkedList<ColladaInputPipe>();
		final int maxOffset = extractPipes(polys, pipes);
		final int interval = maxOffset + 1;

		// use interval & sum of sizes of vcount to determine buffer sizes.
		int numEntries = 0;
		int numIndices = 0;
		for (final int length : _colladaDOMUtil.parseIntArray(polys.getChild("vcount"))) {
			numEntries += length;
			numIndices += (length - 2) * 3;
		}

		// Construct nio buffers for specified inputs.
		for (final ColladaInputPipe pipe : pipes) {
			pipe.setupBuffer(numEntries, polyMesh);
		}

		// Add to vert mapping
		final int[] indices = new int[numEntries];
		final MeshVertPairs mvp = new MeshVertPairs(polyMesh, indices);
		_dataCache.getVertMappings().put(colladaGeometry, mvp);

		// Prepare indices buffer
		IntBuffer meshIndices = createIndexBufferData(polyMesh.getVertexCount());
		polyMesh.setIndices(meshIndices);

		// go through the polygon entries
		int firstIndex = 0;
		int vecIndex;
		final int[] vals = _colladaDOMUtil.parseIntArray(polys.getChild("p"));
		for (final int length : _colladaDOMUtil.parseIntArray(polys.getChild("vcount"))) {
			final int[] currentVal = new int[interval];

			// first add the first two entries to the buffers.
			final int first = firstIndex + 0;
			System.arraycopy(vals, (first * interval), currentVal, 0, interval);
			vecIndex = processPipes(pipes, currentVal);
			if (vecIndex != Integer.MIN_VALUE) {
				indices[firstIndex + 0] = vecIndex;
			}

			int prev = firstIndex + 1;
			System.arraycopy(vals, (prev * interval), currentVal, 0, interval);
			vecIndex = processPipes(pipes, currentVal);
			if (vecIndex != Integer.MIN_VALUE) {
				indices[firstIndex + 1] = vecIndex;
			}

			// Now go through remaining entries and create a polygon as a triangle fan.
			for (int j = 2, max = length; j < max; j++) {
				// add first as index
				meshIndices.put(first);
				// add prev as index
				meshIndices.put(prev);

				// set prev to current
				prev = firstIndex + j;
				// add current to buffers
				System.arraycopy(vals, (prev * interval), currentVal, 0, interval);
				vecIndex = processPipes(pipes, currentVal);
				if (vecIndex != Integer.MIN_VALUE) {
					indices[firstIndex + j] = vecIndex;
				}
				// add current as index
				meshIndices.put(prev);
			}
			firstIndex += length;
		}

		// return
		return polyMesh;
	}

	public Mesh buildMeshTriangles(final Element colladaGeometry, final Element tris) {
		if (tris == null || tris.getChild("input") == null || tris.getChild("p") == null) {
			return null;
		}
		final Mesh triMesh = new Mesh(extractName(colladaGeometry, tris));
		triMesh.setIndexMode(IndexMode.Triangles);

		final LinkedList<ColladaInputPipe> pipes = new LinkedList<ColladaInputPipe>();
		final int maxOffset = extractPipes(tris, pipes);
		final int interval = maxOffset + 1;

		// use interval & size of p array to determine buffer sizes.
		final int[] vals = _colladaDOMUtil.parseIntArray(tris.getChild("p"));
		final int numEntries = vals.length / interval;

		// Construct nio buffers for specified inputs.
		for (final ColladaInputPipe pipe : pipes) {
			pipe.setupBuffer(numEntries, triMesh);
		}

		// Add to vert mapping
		final int[] indices = new int[numEntries];
		final MeshVertPairs mvp = new MeshVertPairs(triMesh, indices);
		_dataCache.getVertMappings().put(colladaGeometry, mvp);

		// go through the p entry
		// for each p, iterate using max offset
		final int[] currentVal = new int[interval];

		// Go through entries and add to buffers.
		for (int j = 0, max = numEntries; j < max; j++) {
			// add entry to buffers
			System.arraycopy(vals, j * interval, currentVal, 0, interval);
			final int rVal = processPipes(pipes, currentVal);
			if (rVal != Integer.MIN_VALUE) {
				indices[j] = rVal;
			}
		}


		return triMesh;
	}


	/**
	 * Extract our pipes from the given parent element.
	 *
	 * @param inputsParent
	 * @param pipesStore   the store for our pipes
	 * @return the max offset of our pipes.
	 */
	@SuppressWarnings("unchecked")
	private int extractPipes(final Element inputsParent, final LinkedList<ColladaInputPipe> pipesStore) {
		int maxOffset = 0;
		int texCount = 0;
		for (final Element input : (List<Element>) inputsParent.getChildren("input")) {
			maxOffset = Math.max(maxOffset, _colladaDOMUtil.getAttributeIntValue(input, "offset", 0));
			try {
				final ColladaInputPipe.Type type = ColladaInputPipe.Type.valueOf(input.getAttributeValue("semantic"));
				if (type == ColladaInputPipe.Type.VERTEX) {
					final Element vertexElement = _colladaDOMUtil.findTargetWithId(input.getAttributeValue("source"));
					for (final Element vertexInput : (List<Element>) vertexElement.getChildren("input")) {
						vertexInput.setAttribute("offset", input.getAttributeValue("offset"));
						vertexInput.setAttribute("isVertexDefined", "true");
						pipesStore.add(new ColladaInputPipe(_colladaDOMUtil, vertexInput));
					}
				} else {
					final ColladaInputPipe pipe = new ColladaInputPipe(_colladaDOMUtil, input);
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

	/**
	 * Push the values at the given indices of currentVal onto the buffers defined in pipes.
	 *
	 * @param pipes
	 * @param currentVal
	 * @return the vertex index referenced in the given indices based on the pipes. Integer.MIN_VALUE is returned if no
	 *         vertex pipe is found.
	 */
	private int processPipes(final LinkedList<ColladaInputPipe> pipes, final int[] currentVal) {
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

	/**
	 * Extract name from xml element, some exporters don't support 'name' attribute, so we better use the material
	 * instead of a generic name.
	 *
	 * @param element
	 * @return value from 'name' or 'material' attribute
	 */
	private String extractName(final Element colladaGeometry, final Element element) {
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
}
