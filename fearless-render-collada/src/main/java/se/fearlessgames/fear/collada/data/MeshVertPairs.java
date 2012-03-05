package se.fearlessgames.fear.collada.data;

public class MeshVertPairs {
	private final Mesh mesh;

	/**
	 * The Collada indices. This array should be as big as the vertex count of the Mesh.
	 */
	private final int[] indices;

	public MeshVertPairs(final Mesh mesh, final int[] indices) {
		this.mesh = mesh;
		this.indices = indices;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public int[] getIndices() {
		return indices;
	}
}
