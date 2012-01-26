package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum VertexDrawMode {
	TRIANGLES(GL11.GL_TRIANGLES),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
	TRIANGLES_FAN(GL11.GL_TRIANGLE_FAN),
	QUADS(GL11.GL_QUADS),
	QUAD_STRIP(GL11.GL_QUAD_STRIP),
	LINES(GL11.GL_LINES),
	LINE_STRIP(GL11.GL_LINE_STRIP),
	LINE_LOOP(GL11.GL_LINE_LOOP),
	POINT(GL11.GL_POINT);

	private final int glMode;

	VertexDrawMode(int glMode) {
		this.glMode = glMode;
	}

	public int getGlMode() {
		return glMode;
	}
}
