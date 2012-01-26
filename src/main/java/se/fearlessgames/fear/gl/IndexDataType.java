package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum IndexDataType {
	GL_UNSIGNED_BYTE(GL11.GL_UNSIGNED_BYTE),
	GL_UNSIGNED_SHORT(GL11.GL_UNSIGNED_SHORT),
	GL_UNSIGNED_INT(GL11.GL_UNSIGNED_INT);

	private final int type;

	IndexDataType(int type) {
		this.type = type;
	}

	public int getGlType() {
		return type;
	}
}
