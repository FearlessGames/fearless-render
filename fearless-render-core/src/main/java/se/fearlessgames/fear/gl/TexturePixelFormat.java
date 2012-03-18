package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum TexturePixelFormat {
	GL_RGBA(GL11.GL_RGBA),
	GL_RGB(GL11.GL_RGB);


	private final int type;

	TexturePixelFormat(int type) {
		this.type = type;
	}

	public int getGlType() {
		return type;
	}
}
