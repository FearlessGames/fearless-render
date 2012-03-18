package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum TextureMagnificationFilter {
	NEAREST(GL11.GL_NEAREST),
	LINEAR(GL11.GL_LINEAR);

	private final int type;

	TextureMagnificationFilter(int type) {
		this.type = type;
	}

	public int getGlType() {
		return type;
	}
}
