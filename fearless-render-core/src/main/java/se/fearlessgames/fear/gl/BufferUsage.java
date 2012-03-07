package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL15;

public enum BufferUsage {
	GL_STREAM_DRAW(GL15.GL_STREAM_DRAW),
	GL_STREAM_READ(GL15.GL_STREAM_READ),
	GL_STREAM_COPY(GL15.GL_STREAM_COPY),
	GL_STATIC_DRAW(GL15.GL_STATIC_DRAW),
	GL_STATIC_READ(GL15.GL_STATIC_READ),
	GL_STATIC_COPY(GL15.GL_STATIC_COPY),
	GL_DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
	GL_DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
	GL_DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY);

	private final int usage;

	BufferUsage(int usage) {
		this.usage = usage;
	}

	public int getGlUsage() {
		return usage;
	}
}
