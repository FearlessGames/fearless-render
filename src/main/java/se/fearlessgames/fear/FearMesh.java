package se.fearlessgames.fear;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class FearMesh {
	private Vector3D position = Vector3D.ZERO;
	private Rotation rotation = Rotation.IDENTITY;

	private final VertexBufferObject vbo;

	public FearMesh(VertexBufferObject vbo) {
		this.vbo = vbo;
	}

	public boolean isOpaque() {
		return true;
	}

	public Vector3D getPosition() {
		return position;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}
}
