package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum TextureType {
	TEXTURE_2D(GL11.GL_TEXTURE_2D);

	private final int type;

	TextureType(int type) {
		this.type = type;
	}

	public int getGlType() {
		return type;
	}
}
