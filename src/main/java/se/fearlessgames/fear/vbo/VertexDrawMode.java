package se.fearlessgames.fear.vbo;

import org.lwjgl.opengl.GL11;

public enum VertexDrawMode {
	TRIANGLES(GL11.GL_TRIANGLES),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
	QUADS(GL11.GL_QUADS);

	private final int glMode;

	VertexDrawMode(int glMode) {
		this.glMode = glMode;
	}

	public int getGlMode() {
		return glMode;
	}
}
