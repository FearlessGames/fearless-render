package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum Capability {
	GL_DEPTH_TEST(GL11.GL_DEPTH_TEST),
	GL_BLEND(GL11.GL_BLEND);

	private final int cap;

	Capability(int cap) {
		this.cap = cap;
	}

	public int getGlCap() {
		return cap;
	}
}
