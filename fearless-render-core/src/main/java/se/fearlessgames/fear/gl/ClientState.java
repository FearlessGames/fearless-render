package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;

public enum ClientState {
	GL_NORMAL_ARRAY(GL11.GL_NORMAL_ARRAY),
	GL_COLOR_ARRAY(GL11.GL_COLOR_ARRAY),
	GL_TEXTURE_COORD_ARRAY(GL11.GL_TEXTURE_COORD_ARRAY),
	GL_VERTEX_ARRAY(GL11.GL_VERTEX_ARRAY);
	private final int state;

	ClientState(int state) {
		this.state = state;
	}

	public int getGlState() {
		return state;
	}
}
