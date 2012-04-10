package se.fearlessgames.fear.camera;

import se.fearlessgames.fear.Transformation;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class RotationCamera implements Camera {
	private Quaternion rotation = Quaternion.IDENTITY;
	private Vector3 location = Vector3.ZERO;
	private CameraPerspective perspective;

	public RotationCamera(CameraPerspective perspective) {
		this.perspective = perspective;
	}

	public CameraPerspective getPerspective() {
		return perspective;
	}

	public void setPerspective(CameraPerspective cameraPerspective) {
		this.perspective = cameraPerspective;
	}

	@Override
	public Vector3 getLocation() {
		return location;
	}

	public void setLocation(Vector3 location) {
		this.location = location;
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

	public Matrix4 asMatrix() {
		return new Transformation(location, rotation, Vector3.ONE).asMatrix();
	}
}
