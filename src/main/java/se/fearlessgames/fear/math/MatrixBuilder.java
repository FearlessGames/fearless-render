package se.fearlessgames.fear.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class MatrixBuilder {

	private final FloatBuffer buffer;

	public MatrixBuilder() {
		buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(0, 1.0f);
		buffer.put(5, 1.0f);
		buffer.put(10, 1.0f);
		buffer.put(15, 1.0f);
	}

	public FloatBuffer asFloatBuffer() {
		buffer.rewind();
		return buffer;
	}
}
