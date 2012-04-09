package se.fearlessgames.fear.camera;

import se.fearlessgames.fear.Transformation;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class Camera {
	private Quaternion rotation = Quaternion.IDENTITY;
	private Vector3 position = Vector3.ZERO;
	private CameraPerspective perspective;

	public Camera(CameraPerspective perspective) {
		this.perspective = perspective;
	}

	public CameraPerspective getPerspective() {
		return perspective;
	}

	public void setPerspective(CameraPerspective cameraPerspective) {
		this.perspective = cameraPerspective;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void lookAt(Vector3 position, Vector3 up) {
		rotation = Quaternion.lookAt(position, up);
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Transformation asTransformation() {
		return new Transformation(position, rotation, Vector3.ONE);
	}

	public Matrix4 asMatrix() {
		return asTransformation().asMatrix();
	}
}
