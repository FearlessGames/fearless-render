package se.fearlessgames.fear.camera;

import se.fearlessgames.fear.Transformation;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.math.Matrix3;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class MatrixBasedCamera implements Camera {
	private final CameraPerspective cameraPerspective;

	private Quaternion orientation = Quaternion.IDENTITY;
	private Vector3 position = Vector3.ZERO;

	private Matrix4 view;
	private Matrix4 projection;
	private Matrix4 viewProjection;

	private boolean recalcViewProjection = true;
	private boolean recalcView = true;


	public MatrixBasedCamera(CameraPerspective cameraPerspective) {
		this.cameraPerspective = cameraPerspective;
		projection = cameraPerspective.getMatrix();
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
		recalcView = true;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public void translate(Vector3 translate) {
		Matrix3 matrix3 = orientation.toRotationMatrix3();
		Vector3 v = matrix3.applyPost(translate.negate());
		position = position.add(v);
		recalcView = true;
	}

	@Override
	public CameraPerspective getPerspective() {
		return cameraPerspective;
	}

	@Override
	public Vector3 getLocation() {
		return position;
	}

	@Override
	public Matrix4 getViewProjectionMatrix() {
		if (view == null) {
			getViewMatrix();
		}
		if (recalcViewProjection) {
			viewProjection = view.multiply(projection);
			recalcViewProjection = false;
		}
		return viewProjection;
	}

	@Override
	public Matrix4 getViewMatrix() {
		if (recalcView) {
			view = new Transformation(position, orientation, Vector3.ONE).asMatrix();
			recalcViewProjection = true;
			recalcView = false;
		}
		return view;
	}
}
