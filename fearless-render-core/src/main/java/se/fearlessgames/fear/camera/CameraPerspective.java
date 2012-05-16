package se.fearlessgames.fear.camera;


import se.fearlessgames.fear.BufferUtils;
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
		matrix = new Matrix4(buildPerspectiveMatrix(fovInDegrees, aspect, zNear, zFar));
	}


	private FloatBuffer buildPerspectiveMatrix(float fovInDegrees, float aspect, float znear, float zfar) {
		FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
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

		matrix.put(0, w);
		matrix.put(1, 0);
		matrix.put(2, 0);
		matrix.put(3, 0);

		matrix.put(4, 0);
		matrix.put(5, h);
		matrix.put(6, 0);
		matrix.put(7, 0);

		matrix.put(8, 0);
		matrix.put(9, 0);
		matrix.put(10, -q);
		matrix.put(11, 1);

		matrix.put(12, 0);
		matrix.put(13, 0);
		matrix.put(14, qn);
		matrix.put(15, 0);
		matrix.rewind();
		return matrix;
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
