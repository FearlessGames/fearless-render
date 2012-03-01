package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum Culling {
	FRONT(GL11.GL_FRONT), BACK(GL11.GL_BACK), FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK);

	private final int value;

	Culling(int value) {
		this.value = value;
	}

	public int getGlValue() {
		return value;
	}
}
