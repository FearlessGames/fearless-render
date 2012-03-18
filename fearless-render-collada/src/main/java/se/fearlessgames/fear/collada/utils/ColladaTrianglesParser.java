package se.fearlessgames.fear.collada.utils;

import org.jdom.Element;
import se.fearlessgames.fear.collada.data.ColladaInputPipe;
import se.fearlessgames.fear.collada.data.DataCache;
import se.fearlessgames.fear.collada.data.MeshVertPairs;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.IntBuffer;
import java.util.LinkedList;

public class ColladaTrianglesParser extends ColladaPrimitiveParser {
	private final static String ELEMENT_NAME = "triangles";

	public ColladaTrianglesParser(ColladaDOMUtil colladaDOMUtil, DataCache dataCache) {
		super(colladaDOMUtil, dataCache);
	}

	@Override
	protected String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	public MeshData buildMesh(Element colladaGeometry, Element tris) {
		if (tris == null || tris.getChild("input") == null || tris.getChild("p") == null) {
			return null;
		}
		final MeshData triMeshData = new MeshData(extractName(colladaGeometry, tris));
		triMeshData.setVertexIndexMode(VertexIndexMode.TRIANGLES);

		final LinkedList<ColladaInputPipe> pipes = new LinkedList<ColladaInputPipe>();
		final int maxOffset = extractPipes(tris, pipes);
		final int interval = maxOffset + 1;

		// use interval & size of p array to determine buffer sizes.
		final int[] vals = colladaDOMUtil.parseIntArray(tris.getChild("p"));
		final int numEntries = vals.length / interval;

		// Construct nio buffers for specified inputs.
		for (final ColladaInputPipe pipe : pipes) {
			pipe.setupBuffer(numEntries, triMeshData);
		}

		// Add to vert mapping
		final int[] indices = new int[numEntries];
		final MeshVertPairs mvp = new MeshVertPairs(triMeshData, vals);
		dataCache.getVertMappings().put(colladaGeometry, mvp);


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
		for (int indice : indices) {
			System.out.print(indice + " ");
		}
		IntBuffer meshIndices = createIndexBufferData(indices.length);
		meshIndices.put(indices);
		meshIndices.rewind();

		triMeshData.setIndices(meshIndices);
		return triMeshData;
	}
}
