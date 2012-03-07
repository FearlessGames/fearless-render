package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;

public class TransformedMesh {
	public final Mesh mesh;
	public final Matrix4 transform;

	public TransformedMesh(Mesh mesh, Matrix4 transform) {
		this.mesh = mesh;
		this.transform = transform;
	}
}
