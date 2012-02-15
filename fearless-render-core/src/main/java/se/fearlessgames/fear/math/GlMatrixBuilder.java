package se.fearlessgames.fear.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class GlMatrixBuilder {

	private GlMatrixBuilder() {
	}

	public static FloatBuffer convert(Matrix4 matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);

		buffer.put(0, (float) matrix.getValue(0, 0));
		buffer.put(1, (float) matrix.getValue(1, 0));
		buffer.put(2, (float) matrix.getValue(2, 0));
		buffer.put(3, (float) matrix.getValue(3, 0));
		buffer.put(4, (float) matrix.getValue(0, 1));
		buffer.put(5, (float) matrix.getValue(1, 1));
		buffer.put(6, (float) matrix.getValue(2, 1));
		buffer.put(7, (float) matrix.getValue(3, 1));
		buffer.put(8, (float) matrix.getValue(0, 2));
		buffer.put(9, (float) matrix.getValue(1, 2));
		buffer.put(10, (float) matrix.getValue(2, 2));
		buffer.put(11, (float) matrix.getValue(3, 2));
		buffer.put(12, (float) matrix.getValue(0, 3));
		buffer.put(13, (float) matrix.getValue(1, 3));
		buffer.put(14, (float) matrix.getValue(2, 3));
		buffer.put(15, (float) matrix.getValue(3, 3));

		buffer.rewind();
		return buffer;

	}

	public static FloatBuffer convert(Matrix3 matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3*3);

		buffer.put(0, (float) matrix.getValue(0, 0));
		buffer.put(1, (float) matrix.getValue(1, 0));
		buffer.put(2, (float) matrix.getValue(2, 0));

		buffer.put(3, (float) matrix.getValue(0, 1));
		buffer.put(4, (float) matrix.getValue(1, 1));
		buffer.put(5, (float) matrix.getValue(2, 1));

		buffer.put(6, (float) matrix.getValue(0, 2));
		buffer.put(7, (float) matrix.getValue(1, 2));
		buffer.put(8, (float) matrix.getValue(2, 2));

		return buffer;

	}
}
