package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public enum StringName {
	VENDOR(GL11.GL_VENDOR),
	RENDERER(GL11.GL_RENDERER),
	SHADING_LANGUAGE_VERSION(GL20.GL_SHADING_LANGUAGE_VERSION),
	VERSION(GL11.GL_VERSION),
	EXTENSIONS(GL11.GL_EXTENSIONS);

	private final int name;

	StringName(int name) {
		this.name = name;
	}

	public int getName() {
		return name;
	}
}
