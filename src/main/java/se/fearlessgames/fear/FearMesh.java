package se.fearlessgames.fear;

import se.fearlessgames.fear.vbo.VertexBufferObject;

public class FearMesh {
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
}
