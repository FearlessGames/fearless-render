package se.fearlessgames.fear;

import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Mesh {
	private final VertexBufferObject vbo;

	public Mesh(VertexBufferObject vbo) {
		this.vbo = vbo;
	}

	public boolean isOpaque() {
		return true;
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}
}
