package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import se.fearlessgames.fear.collada.data.ColladaInputPipe;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.collada.data.MeshVertPairs;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.IntBuffer;
import java.util.LinkedList;

public class ColladaPolyListParser extends ColladaPrimitiveParser {
	private final static String ELEMENT_NAME = "polylist";

	public ColladaPolyListParser(ColladaDOMUtil colladaDOMUtil, DataCache dataCache) {
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

		// use interval & sum of sizes of vcount to determine buffer sizes.
		int numEntries = 0;
		int numIndices = 0;
		for (final int length : colladaDOMUtil.parseIntArray(polys.getChild("vcount"))) {
			numEntries += length;
			numIndices += (length - 2) * 3;
		}

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
		int firstIndex = 0;
		int vecIndex;
		final int[] vals = colladaDOMUtil.parseIntArray(polys.getChild("p"));
		for (final int length : colladaDOMUtil.parseIntArray(polys.getChild("vcount"))) {
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
		return polyMeshData;
	}
}
