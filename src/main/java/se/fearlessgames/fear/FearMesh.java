package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class FearMesh {
	private Vector3 position = Vector3.ZERO;
	private Quaternion rotation = Quaternion.IDENTITY;

	private final VertexBufferObject vbo;

	public FearMesh(VertexBufferObject vbo) {
		this.vbo = vbo;
	}

	public boolean isOpaque() {
		return true;
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
}
