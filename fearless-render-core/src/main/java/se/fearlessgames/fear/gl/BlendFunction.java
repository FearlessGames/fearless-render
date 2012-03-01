package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum BlendFunction {
	SRC_ALPHA(GL11.GL_SRC_ALPHA),
	ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
	ONE(GL11.GL_ONE),
	ZERO(GL11.GL_ZERO),
	DST_ALPHA(GL11.GL_DST_ALPHA);

	private final int glValue;

	private BlendFunction(int glValue) {
		this.glValue = glValue;
	}

	public int getGlValue() {
		return glValue;
	}
}
