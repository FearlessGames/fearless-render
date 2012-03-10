package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum VertexIndexMode {
	TRIANGLES(GL11.GL_TRIANGLES, 3),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP, 3),
	TRIANGLES_FAN(GL11.GL_TRIANGLE_FAN, 3),
	QUADS(GL11.GL_QUADS, 4),
	QUAD_STRIP(GL11.GL_QUAD_STRIP, 4),
	LINES(GL11.GL_LINES, 2),
	LINE_STRIP(GL11.GL_LINE_STRIP, 2),
	LINE_LOOP(GL11.GL_LINE_LOOP, 2),
	POINT(GL11.GL_POINT, 1);

	private final int glMode;
	private final int vertexCount;

	VertexIndexMode(int glMode, int vertexCount) {
		this.glMode = glMode;
		this.vertexCount = vertexCount;
	}

	public int getGlMode() {
		return glMode;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
