package se.fearlessgames.fear.collada.data;

import se.fearlessgames.fear.mesh.MeshData;

public class MeshVertPairs {
	private final MeshData meshData;

	/**
	 * The Collada indices. This array should be as big as the vertex count of the Mesh.
	 */
	private final int[] indices;

	public MeshVertPairs(final MeshData meshData, final int[] indices) {
		this.meshData = meshData;
		this.indices = indices;
	}

	public MeshData getMeshData() {
		return meshData;
	}

	public int[] getIndices() {
		return indices;
	}
}
