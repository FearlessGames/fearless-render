package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum DepthFunction {
	GL_NEVER(GL11.GL_NEVER),
	GL_LESS(GL11.GL_LESS),
	GL_EQUAL(GL11.GL_EQUAL),
	GL_LEQUAL(GL11.GL_LEQUAL),
	GL_GREATER(GL11.GL_GREATER),
	GL_NOTEQUAL(GL11.GL_NOTEQUAL),
	GL_GEQUAL(GL11.GL_GEQUAL),
	GL_ALWAYS(GL11.GL_ALWAYS);

	private final int function;

	DepthFunction(int function) {
		this.function = function;
	}

	public int getGlFunction() {
		return function;
	}
}
