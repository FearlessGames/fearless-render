package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL20;

public enum ShaderType {
	VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
	FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER);

	private final int type;

	private ShaderType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
