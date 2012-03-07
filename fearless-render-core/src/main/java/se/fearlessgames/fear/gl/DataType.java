package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum DataType {
	GL_SHORT(GL11.GL_SHORT),
	GL_INT(GL11.GL_INT),
	GL_FLOAT(GL11.GL_FLOAT),
	GL_DOUBLE(GL11.GL_DOUBLE);


	private final int type;

	DataType(int type) {
		this.type = type;
	}

	public int getGlType() {
		return type;
	}
}
