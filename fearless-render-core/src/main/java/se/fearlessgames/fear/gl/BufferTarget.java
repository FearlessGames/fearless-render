package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;

public enum BufferTarget {
	GL_ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
	GL_ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
	GL_PIXEL_PACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER),
	GL_PIXEL_UNPACK_BUFFER(GL21.GL_PIXEL_UNPACK_BUFFER);

	private final int bufferTarget;

	BufferTarget(int bufferTarget) {
		this.bufferTarget = bufferTarget;
	}

	public int getGlBufferTarget() {
		return bufferTarget;
	}
}
