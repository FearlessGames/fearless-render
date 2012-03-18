package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum TextureMinificationFilter {
	NEAREST(GL11.GL_NEAREST, false),
	LINEAR(GL11.GL_LINEAR, false),
	NEAREST_MIPMAP_NEAREST(GL11.GL_NEAREST_MIPMAP_NEAREST, true),
	LINEAR_MIPMAP_NEAREST(GL11.GL_LINEAR_MIPMAP_NEAREST, true),
	NEAREST_MIPMAP_LINEAR(GL11.GL_NEAREST_MIPMAP_LINEAR, true),
	LINEAR_MIPMAP_LINEAR(GL11.GL_LINEAR_MIPMAP_LINEAR, true);

	private final int type;
	private final boolean useMipMap;

	TextureMinificationFilter(int type, boolean useMipMap) {
		this.type = type;
		this.useMipMap = useMipMap;
	}

	public int getGlType() {
		return type;
	}

	public boolean isUseMipMap() {
		return useMipMap;
	}
}
