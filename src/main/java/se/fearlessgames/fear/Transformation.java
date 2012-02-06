package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

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
		return new Transformation(this.position.add(position), this.rotation.applyTo(rotation), this.scale.multiply(scale));
	}

	public Matrix4 asMatrix() {
		double[][] matrix = rotation.getMatrix();

		return new Matrix4(
				matrix[0][0] * scale.getX(), matrix[0][1] * scale.getY(), matrix[0][2] * scale.getZ(), position.getX(),
				matrix[1][0] * scale.getX(), matrix[1][1] * scale.getY(), matrix[1][2] * scale.getZ(), position.getY(),
				matrix[2][0] * scale.getX(), matrix[2][1] * scale.getY(), matrix[2][2] * scale.getZ(), position.getZ(),
				0, 0, 0, 1);
	}
}
