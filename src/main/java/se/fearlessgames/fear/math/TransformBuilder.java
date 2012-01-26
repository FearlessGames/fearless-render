package se.fearlessgames.fear.math;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class TransformBuilder {

	private final FloatBuffer buffer;

	public TransformBuilder() {
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

	public void translate(Vector3D offset) {
		buffer.put(12, (float) offset.getX());
		buffer.put(13, (float) offset.getY());
		buffer.put(14, (float) offset.getZ());
	}

	public void rotate(Rotation rotation) {
		double[][] matrix = rotation.getMatrix();

		buffer.put(0, (float) matrix[0][0]);
		buffer.put(1, (float) matrix[1][0]);
		buffer.put(2, (float) matrix[2][0]);
		buffer.put(4, (float) matrix[0][1]);
		buffer.put(5, (float) matrix[1][1]);
		buffer.put(6, (float) matrix[2][1]);
		buffer.put(8, (float) matrix[0][2]);
		buffer.put(9, (float) matrix[1][2]);
		buffer.put(10, (float) matrix[2][2]);
	}
}
