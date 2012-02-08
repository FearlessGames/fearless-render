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

		double scaleX = scale.getX();
		double scaleY = scale.getY();
		double scaleZ = scale.getZ();
		return new Matrix4(
				matrix[0][0] * scaleX, matrix[0][1] * scaleY, matrix[0][2] * scaleZ, position.getX(),
				matrix[1][0] * scaleX, matrix[1][1] * scaleY, matrix[1][2] * scaleZ, position.getY(),
				matrix[2][0] * scaleX, matrix[2][1] * scaleY, matrix[2][2] * scaleZ, position.getZ(),
				0, 0, 0, 1);
	}
}
