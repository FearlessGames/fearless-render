package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import se.fearlessgames.fear.collada.data.ColladaInputPipe;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.collada.data.MeshVertPairs;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public class ColladaPolygonsParser extends ColladaPrimitiveParser {
	private final static String ELEMENT_NAME = "polygons";

	public ColladaPolygonsParser(ColladaDOMUtil colladaDOMUtil, DataCache dataCache) {
		super(colladaDOMUtil, dataCache);
	}

	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MeshData buildMesh(Element colladaGeometry, Element polys) {
		if (polys == null || polys.getChild("input") == null) {
			return null;
		}
		final MeshData polyMeshData = new MeshData(extractName(colladaGeometry, polys));
		polyMeshData.setVertexIndexMode(VertexIndexMode.TRIANGLES);


		final LinkedList<ColladaInputPipe> pipes = new LinkedList<ColladaInputPipe>();
		final int maxOffset = extractPipes(polys, pipes);
		final int interval = maxOffset + 1;

		// use interval & sum of sizes of p entries to determine buffer sizes.
		int numEntries = 0;
		int numIndices = 0;
		for (final Element vals : (List<Element>) polys.getChildren("p")) {
			final int length = colladaDOMUtil.parseIntArray(vals).length;
			numEntries += length;
			numIndices += (length / interval - 2) * 3;
		}
		numEntries /= interval;

		// Construct nio buffers for specified inputs.
		for (final ColladaInputPipe pipe : pipes) {
			pipe.setupBuffer(numEntries, polyMeshData);
		}

		// Add to vert mapping
		final int[] indices = new int[numEntries];
		final MeshVertPairs mvp = new MeshVertPairs(polyMeshData, indices);
		dataCache.getVertMappings().put(colladaGeometry, mvp);

		// Prepare indices buffer

		IntBuffer meshIndices = createIndexBufferData(polyMeshData.getVertexCount());
		polyMeshData.setIndices(meshIndices);

		// go through the polygon entries
		int firstIndex = 0, vecIndex;
		final int[] currentVal = new int[interval];
		for (final Element dia : (List<Element>) polys.getChildren("p")) {
			// for each p, iterate using max offset
			final int[] vals = colladaDOMUtil.parseIntArray(dia);

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

		return polyMeshData;
	}
}
