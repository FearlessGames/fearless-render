package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum ClearBit {
	GL_COLOR_BUFFER_BIT(GL11.GL_COLOR_BUFFER_BIT),
	GL_DEPTH_BUFFER_BIT(GL11.GL_DEPTH_BUFFER_BIT),
	GL_ACCUM_BUFFER_BIT(GL11.GL_ACCUM_BUFFER_BIT),
	GL_STENCIL_BUFFER_BIT(GL11.GL_STENCIL_BUFFER_BIT);

	private final int bit;

	ClearBit(int bit) {
		this.bit = bit;
	}

	public int getGlBit() {
		return bit;
	}
}
