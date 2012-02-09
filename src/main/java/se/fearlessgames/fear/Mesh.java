package se.fearlessgames.fear;

import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Mesh {
	private final VertexBufferObject vbo;
	private Texture texture;

	public Mesh(VertexBufferObject vbo) {
		this.vbo = vbo;
	}

	public boolean isOpaque() {
		return true;
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean hasTexture() {
		return texture != null;
	}

}
