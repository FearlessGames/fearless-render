package se.fearlessgames.fear.camera;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector3;

public class VectorCamera implements Camera {
	private Vector3 location = new Vector3(0, 0, 0);
	private Vector3 left = new Vector3(-1, 0, 0);
	private Vector3 up = new Vector3(0, 1, 0);
	private Vector3 direction = new Vector3(0, 0, -1);

	private CameraPerspective perspective;
	private Matrix4 matrix = Matrix4.IDENTITY;

	public VectorCamera(CameraPerspective perspective) {
		this.perspective = perspective;
	}

	@Override
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
		onChange();
	}

	public Vector3 getLeft() {
		return left;
	}

	public void setLeft(Vector3 left) {
		this.left = left;
		onChange();
	}

	public Vector3 getUp() {
		return up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
		onChange();
	}

	public Vector3 getDirection() {
		return direction;
	}

	public void setDirection(Vector3 direction) {
		this.direction = direction;
		onChange();
	}

	public void normalize() {
		left = left.normalize();
		up = up.normalize();
		direction = direction.normalize();
		onChange();
	}

	private void onChange() {
		double[][] rotation = Matrix4.IDENTITY.to2DArray();

		rotation[0][0] = -left.getX();
		rotation[1][0] = -left.getY();
		rotation[2][0] = -left.getZ();

		rotation[0][1] = up.getX();
		rotation[1][1] = up.getY();
		rotation[2][1] = up.getZ();

		rotation[0][2] = -direction.getX();
		rotation[1][2] = -direction.getY();
		rotation[2][2] = -direction.getZ();

		double[][] transMatrix = Matrix4.IDENTITY.to2DArray();
		transMatrix[3][0] = -location.getX();
		transMatrix[3][1] = -location.getY();
		transMatrix[3][2] = -location.getZ();

		matrix = new Matrix4(transMatrix).multiply(new Matrix4(rotation));
	}

	@Override
	public Matrix4 asMatrix() {
		return matrix;
	}
}
