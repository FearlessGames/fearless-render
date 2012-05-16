package se.fearlessgames.fear.camera;


import se.fearlessgames.fear.math.MathUtils;
import se.fearlessgames.fear.math.Matrix4;

import java.nio.FloatBuffer;

public class CameraPerspective {
	private final Matrix4 matrix;
	private final float fovInDegrees;
	private final float aspect;
	private final float zNear;
	private final float zFar;

	public CameraPerspective(float fovInDegrees, float aspect, float zNear, float zFar) {
		this.fovInDegrees = fovInDegrees;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
		matrix = buildPerspectiveMatrix(fovInDegrees, aspect, zNear, zFar);
	}


	private Matrix4 buildPerspectiveMatrix(float fovInDegrees, float aspect, float znear, float zfar) {
		Matrix4 matrix = new Matrix4();
		float xymax = (float) (znear * MathUtils.tan(fovInDegrees * MathUtils.PI_OVER_360));
		float ymin = -xymax;
		float xmin = -xymax;

		float width = xymax - xmin;
		float height = xymax - ymin;

		float depth = zfar - znear;
		float q = -(zfar + znear) / depth;
		float qn = -2 * (zfar * znear) / depth;

		float w = 2 * znear / width;
		w = w / aspect;
		float h = 2 * znear / height;

		matrix.setValue(0, 0,  w);
		matrix.setValue(0, 1, 0);
		matrix.setValue(0, 2, 0);
		matrix.setValue(0, 3, 0);

		matrix.setValue(1, 0, 0);
		matrix.setValue(1, 1, h);
		matrix.setValue(1, 2, 0);
		matrix.setValue(1, 3, 0);

		matrix.setValue(2, 0, 0);
		matrix.setValue(2, 1, 0);
		matrix.setValue(2, 2, -q);
		matrix.setValue(2, 3, 1);

		matrix.setValue(3, 0, 0);
		matrix.setValue(3, 1, 0);
		matrix.setValue(3, 2, qn);
		matrix.setValue(3, 3, 0);
		return matrix;
	}

	public FloatBuffer getMatrixAsBuffer() {
		return matrix.toFloatBuffer();
	}

	public float getFovInDegrees() {
		return fovInDegrees;
	}

	public float getAspect() {
		return aspect;
	}

	public float getzNear() {
		return zNear;
	}

	public float getzFar() {
		return zFar;
	}

	public Matrix4 getMatrix() {
		return matrix;
	}
}
