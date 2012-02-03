package se.fearlessgames.fear;

import org.lwjgl.BufferUtils;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

import java.nio.FloatBuffer;

public class Transformation {
	private final Vector3 position;
	private final Quaternion rotation;
	private final Vector3 scale;

	public Transformation(Vector3 position, Quaternion rotation, Vector3 scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Transformation transformTo(Vector3 position, Quaternion rotation, Vector3 scale) {
		//todo: this should probebly not be add or multiply but rather some hot matrix function...or??
		return new Transformation(this.position.add(position), this.rotation.add(rotation), this.scale.multiply(scale));
	}

	public FloatBuffer asFloatBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(0, 1.0f);
		buffer.put(5, 1.0f);
		buffer.put(10, 1.0f);
		buffer.put(15, 1.0f);

		buffer.put(12, (float) position.getX());
		buffer.put(13, (float) position.getY());
		buffer.put(14, (float) position.getZ());

		double[][] matrix = rotation.getMatrix();

		buffer.put(0, (float) (matrix[0][0] * scale.getX()));
		buffer.put(1, (float) (matrix[1][0] * scale.getX()));
		buffer.put(2, (float) (matrix[2][0] * scale.getX()));
		buffer.put(4, (float) (matrix[0][1] * scale.getY()));
		buffer.put(5, (float) (matrix[1][1] * scale.getY()));
		buffer.put(6, (float) (matrix[2][1] * scale.getY()));
		buffer.put(8, (float) (matrix[0][2] * scale.getZ()));
		buffer.put(9, (float) (matrix[1][2] * scale.getZ()));
		buffer.put(10, (float) (matrix[2][2] * scale.getZ()));

		buffer.rewind();
		return buffer;
	}
}
