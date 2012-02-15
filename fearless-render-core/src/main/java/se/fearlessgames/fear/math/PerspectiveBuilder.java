package se.fearlessgames.fear.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class PerspectiveBuilder {
	private static final float PI_OVER_360 = (float) (Math.PI / 360.0f);

	private final FloatBuffer matrix;

	public PerspectiveBuilder(float fovInDegrees, float aspect, float znear, float zfar) {
		matrix = buildPerspectiveMatrix(fovInDegrees, aspect, znear, zfar);
	}

	public FloatBuffer getMatrixAsBuffer() {
		return matrix;
	}

	private static FloatBuffer buildPerspectiveMatrix(float fovInDegrees, float aspect, float znear, float zfar) {
		FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
		float xymax = (float) (znear * Math.tan(fovInDegrees * PI_OVER_360));
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
		matrix.put(10, q);
		matrix.put(11, -1);

		matrix.put(12, 0);
		matrix.put(13, 0);
		matrix.put(14, qn);
		matrix.put(15, 0);
		return matrix;
	}
}
