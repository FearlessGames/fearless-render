package se.fearlessgames.fear.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Matrix4 {
	public static final Matrix4 IDENTITY = new Matrix4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

	protected final double[][] data = new double[4][4];

	public Matrix4() {
		this(IDENTITY);
	}

	public Matrix4(final double m00, final double m01, final double m02, final double m03,
				   final double m10, final double m11, final double m12, final double m13,
				   final double m20, final double m21, final double m22, final double m23,
				   final double m30, final double m31, final double m32, final double m33) {

		data[0][0] = m00;
		data[0][1] = m01;
		data[0][2] = m02;
		data[0][3] = m03;
		data[1][0] = m10;
		data[1][1] = m11;
		data[1][2] = m12;
		data[1][3] = m13;
		data[2][0] = m20;
		data[2][1] = m21;
		data[2][2] = m22;
		data[2][3] = m23;
		data[3][0] = m30;
		data[3][1] = m31;
		data[3][2] = m32;
		data[3][3] = m33;
	}

	public Matrix4(final Matrix4 source) {
		data[0][0] = source.getValue(0, 0);
		data[1][0] = source.getValue(1, 0);
		data[2][0] = source.getValue(2, 0);
		data[3][0] = source.getValue(3, 0);

		data[0][1] = source.getValue(0, 1);
		data[1][1] = source.getValue(1, 1);
		data[2][1] = source.getValue(2, 1);
		data[3][1] = source.getValue(3, 1);

		data[0][2] = source.getValue(0, 2);
		data[1][2] = source.getValue(1, 2);
		data[2][2] = source.getValue(2, 2);
		data[3][2] = source.getValue(3, 2);

		data[0][3] = source.getValue(0, 3);
		data[1][3] = source.getValue(1, 3);
		data[2][3] = source.getValue(2, 3);
		data[3][3] = source.getValue(3, 3);
	}

	public Matrix4(final DoubleBuffer source) {
		this(source, true);
	}

	public Matrix4(final DoubleBuffer source, boolean rowMajor) {
		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					data[i][j] = source.get();
				}
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					data[i][j] = source.get();
				}
			}
		}
	}

	public Matrix4(final FloatBuffer source) {
		this(source, true);
	}

	public Matrix4(final FloatBuffer source, boolean rowMajor) {
		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					data[i][j] = source.get();
				}
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					data[i][j] = source.get();
				}
			}
		}
	}

	public Matrix4(final double[] source) {
		this(source, true);
	}

	public Matrix4(final double[] source, final boolean rowMajor) {
		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					data[i][j] = source[i * 4 + j];
				}
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					data[i][j] = source[j * 4 + i];
				}
			}
		}
	}

	public Matrix4(Matrix3 source) {
		this(IDENTITY);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				data[i][j] = source.getValue(i, j);
			}
		}
	}

	public static Matrix4 fromAngleAxis(final double angle, final Vector3 axis) {
		final Vector3 normAxis = axis.normalize();
		return fromAngleNormalAxis(angle, normAxis);
	}

	public static Matrix4 fromAngleNormalAxis(final double angle, final Vector3 axis) {
		final double fCos = MathUtils.cos(angle);
		final double fSin = MathUtils.sin(angle);
		final double fOneMinusCos = (1.0) - fCos;
		final double fX2 = axis.getX() * axis.getX();
		final double fY2 = axis.getY() * axis.getY();
		final double fZ2 = axis.getZ() * axis.getZ();
		final double fXYM = axis.getX() * axis.getY() * fOneMinusCos;
		final double fXZM = axis.getX() * axis.getZ() * fOneMinusCos;
		final double fYZM = axis.getY() * axis.getZ() * fOneMinusCos;
		final double fXSin = axis.getX() * fSin;
		final double fYSin = axis.getY() * fSin;
		final double fZSin = axis.getZ() * fSin;

		Matrix4 matrix4 = new Matrix4();
		matrix4.data[0][0] = fX2 * fOneMinusCos + fCos;
		matrix4.data[0][1] = fXYM - fZSin;
		matrix4.data[0][2] = fXZM + fYSin;
		matrix4.data[1][0] = fXYM + fZSin;
		matrix4.data[1][1] = fY2 * fOneMinusCos + fCos;
		matrix4.data[1][2] = fYZM - fXSin;
		matrix4.data[2][0] = fXZM - fYSin;
		matrix4.data[2][1] = fYZM + fXSin;
		matrix4.data[2][2] = fZ2 * fOneMinusCos + fCos;
		return matrix4;
	}


	public double getValue(final int row, final int column) {
		return data[row][column];
	}

	public Matrix4 setColumn(final int columnIndex, final double[] columnData) {
		Matrix4 matrix4 = new Matrix4(this);
		matrix4.data[0][columnIndex] = columnData[0];
		matrix4.data[1][columnIndex] = columnData[1];
		matrix4.data[2][columnIndex] = columnData[2];
		matrix4.data[3][columnIndex] = columnData[3];
		return matrix4;
	}

	public Matrix4 setRow(final int rowIndex, final double[] rowData) {
		Matrix4 matrix4 = new Matrix4(this);
		matrix4.data[rowIndex][0] = rowData[0];
		matrix4.data[rowIndex][1] = rowData[1];
		matrix4.data[rowIndex][2] = rowData[2];
		matrix4.data[rowIndex][3] = rowData[3];
		return this;
	}


	public Vector4 getColumn(final int index) {
		if (index < 0 || index > 3) {
			throw new IllegalArgumentException("Illegal column index: " + index);
		}

		return new Vector4(
				data[0][index],
				data[1][index],
				data[2][index],
				data[3][index]);

	}

	public Vector4 getRow(final int index) {
		if (index < 0 || index > 3) {
			throw new IllegalArgumentException("Illegal row index: " + index);
		}

		return new Vector4(
				data[index][0],
				data[index][1],
				data[index][2],
				data[index][3]);
	}

	public DoubleBuffer toDoubleBuffer() {
		return toDoubleBuffer(true);
	}

	public DoubleBuffer toDoubleBuffer(final boolean rowMajor) {
		DoubleBuffer result = ByteBuffer.allocateDirect(8 * 16).order(ByteOrder.nativeOrder()).asDoubleBuffer();
		result.clear();


		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				result.put(data[i]);
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					result.put(data[i][j]);
				}
			}
		}

		return result;
	}

	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(true);
	}

	public FloatBuffer toFloatBuffer(final boolean rowMajor) {
		FloatBuffer result = ByteBuffer.allocateDirect(4 * 16).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.clear();


		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					result.put((float) data[i][j]);
				}
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					result.put((float) data[i][j]);
				}
			}
		}

		return result;
	}

	public double[] toArray(final double[] store) {
		return toArray(store, true);
	}

	public double[] toArray(final double[] store, final boolean rowMajor) {
		double[] result = store;
		if (result == null) {
			result = new double[16];
		} else if (result.length < 16) {
			throw new IllegalArgumentException("store must be at least length 16.");
		}

		if (rowMajor) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					result[i * 4 + j] = data[i][j];
				}
			}
		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					result[j * 4 + i] = data[i][j];
				}
			}
		}

		return result;
	}


	public Matrix4 multiplyDiagonalPre(final Vector4 vec) {
		return new Matrix4(
				vec.getX() * data[0][0], vec.getX() * data[0][1], vec.getX() * data[0][2], vec.getX() * data[0][3],
				vec.getY() * data[1][0], vec.getY() * data[1][1], vec.getY() * data[1][2], vec.getY() * data[1][3],
				vec.getZ() * data[2][0], vec.getZ() * data[2][1], vec.getZ() * data[2][2], vec.getZ() * data[2][3],
				vec.getW() * data[3][0], vec.getW() * data[3][1], vec.getW() * data[3][2], vec.getW() * data[3][3]);
	}

	public Matrix4 multiplyDiagonalPost(final Vector4 vec) {

		return new Matrix4(
				vec.getX() * data[0][0], vec.getY() * data[0][1], vec.getZ() * data[0][2], vec.getW() * data[0][3],
				vec.getX() * data[1][0], vec.getY() * data[1][1], vec.getZ() * data[1][2], vec.getW() * data[1][3],
				vec.getX() * data[2][0], vec.getY() * data[2][1], vec.getZ() * data[2][2], vec.getW() * data[2][3],
				vec.getX() * data[3][0], vec.getY() * data[3][1], vec.getZ() * data[3][2], vec.getW() * data[3][3]);

	}

	public Matrix4 multiply(final Matrix4 matrix) {


		final double temp00 = data[0][0] * matrix.getValue(0, 0) + data[0][1] * matrix.getValue(1, 0) + data[0][2]
				* matrix.getValue(2, 0) + data[0][3] * matrix.getValue(3, 0);
		final double temp01 = data[0][0] * matrix.getValue(0, 1) + data[0][1] * matrix.getValue(1, 1) + data[0][2]
				* matrix.getValue(2, 1) + data[0][3] * matrix.getValue(3, 1);
		final double temp02 = data[0][0] * matrix.getValue(0, 2) + data[0][1] * matrix.getValue(1, 2) + data[0][2]
				* matrix.getValue(2, 2) + data[0][3] * matrix.getValue(3, 2);
		final double temp03 = data[0][0] * matrix.getValue(0, 3) + data[0][1] * matrix.getValue(1, 3) + data[0][2]
				* matrix.getValue(2, 3) + data[0][3] * matrix.getValue(3, 3);

		final double temp10 = data[1][0] * matrix.getValue(0, 0) + data[1][1] * matrix.getValue(1, 0) + data[1][2]
				* matrix.getValue(2, 0) + data[1][3] * matrix.getValue(3, 0);
		final double temp11 = data[1][0] * matrix.getValue(0, 1) + data[1][1] * matrix.getValue(1, 1) + data[1][2]
				* matrix.getValue(2, 1) + data[1][3] * matrix.getValue(3, 1);
		final double temp12 = data[1][0] * matrix.getValue(0, 2) + data[1][1] * matrix.getValue(1, 2) + data[1][2]
				* matrix.getValue(2, 2) + data[1][3] * matrix.getValue(3, 2);
		final double temp13 = data[1][0] * matrix.getValue(0, 3) + data[1][1] * matrix.getValue(1, 3) + data[1][2]
				* matrix.getValue(2, 3) + data[1][3] * matrix.getValue(3, 3);

		final double temp20 = data[2][0] * matrix.getValue(0, 0) + data[2][1] * matrix.getValue(1, 0) + data[2][2]
				* matrix.getValue(2, 0) + data[2][3] * matrix.getValue(3, 0);
		final double temp21 = data[2][0] * matrix.getValue(0, 1) + data[2][1] * matrix.getValue(1, 1) + data[2][2]
				* matrix.getValue(2, 1) + data[2][3] * matrix.getValue(3, 1);
		final double temp22 = data[2][0] * matrix.getValue(0, 2) + data[2][1] * matrix.getValue(1, 2) + data[2][2]
				* matrix.getValue(2, 2) + data[2][3] * matrix.getValue(3, 2);
		final double temp23 = data[2][0] * matrix.getValue(0, 3) + data[2][1] * matrix.getValue(1, 3) + data[2][2]
				* matrix.getValue(2, 3) + data[2][3] * matrix.getValue(3, 3);

		final double temp30 = data[3][0] * matrix.getValue(0, 0) + data[3][1] * matrix.getValue(1, 0) + data[3][2]
				* matrix.getValue(2, 0) + data[3][3] * matrix.getValue(3, 0);
		final double temp31 = data[3][0] * matrix.getValue(0, 1) + data[3][1] * matrix.getValue(1, 1) + data[3][2]
				* matrix.getValue(2, 1) + data[3][3] * matrix.getValue(3, 1);
		final double temp32 = data[3][0] * matrix.getValue(0, 2) + data[3][1] * matrix.getValue(1, 2) + data[3][2]
				* matrix.getValue(2, 2) + data[3][3] * matrix.getValue(3, 2);
		final double temp33 = data[3][0] * matrix.getValue(0, 3) + data[3][1] * matrix.getValue(1, 3) + data[3][2]
				* matrix.getValue(2, 3) + data[3][3] * matrix.getValue(3, 3);

		return new Matrix4(
				temp00, temp01, temp02, temp03,
				temp10, temp11, temp12, temp13,
				temp20, temp21, temp22, temp23,
				temp30, temp31, temp32, temp33);

	}

	public Matrix4 add(final Matrix4 matrix) {
		Matrix4 matrix4 = new Matrix4(this);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matrix4.data[i][j] += matrix.getValue(i, j);
			}
		}
		return matrix4;
	}


	public Matrix4 scale(final Vector4 scale) {
		return new Matrix4(
				data[0][0] * scale.getX(), data[0][1] * scale.getY(), data[0][2] * scale.getZ(), data[0][3] * scale.getW(),
				data[1][0] * scale.getX(), data[1][1] * scale.getY(), data[1][2] * scale.getZ(), data[1][3] * scale.getW(),
				data[2][0] * scale.getX(), data[2][1] * scale.getY(), data[2][2] * scale.getZ(), data[2][3] * scale.getW(),
				data[3][0] * scale.getX(), data[3][1] * scale.getY(), data[3][2] * scale.getZ(), data[3][3] * scale.getW());
	}

	/**
	 * transposes this matrix as a new matrix, basically flipping it across the diagonal
	 *
	 * @return this matrix for chaining.
	 * @see <a href="http://en.wikipedia.org/wiki/Transpose">wikipedia.org-Transpose</a>
	 */
	public Matrix4 transpose() {
		return new Matrix4(data[0][0], data[1][0], data[2][0], data[3][0], data[0][1], data[1][1], data[2][1],
				data[3][1], data[0][2], data[1][2], data[2][2], data[3][2], data[0][3], data[1][3], data[2][3],
				data[3][3]);
	}

	public Matrix4 invert() {
		final double dA0 = data[0][0] * data[1][1] - data[0][1] * data[1][0];
		final double dA1 = data[0][0] * data[1][2] - data[0][2] * data[1][0];
		final double dA2 = data[0][0] * data[1][3] - data[0][3] * data[1][0];
		final double dA3 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		final double dA4 = data[0][1] * data[1][3] - data[0][3] * data[1][1];
		final double dA5 = data[0][2] * data[1][3] - data[0][3] * data[1][2];
		final double dB0 = data[2][0] * data[3][1] - data[2][1] * data[3][0];
		final double dB1 = data[2][0] * data[3][2] - data[2][2] * data[3][0];
		final double dB2 = data[2][0] * data[3][3] - data[2][3] * data[3][0];
		final double dB3 = data[2][1] * data[3][2] - data[2][2] * data[3][1];
		final double dB4 = data[2][1] * data[3][3] - data[2][3] * data[3][1];
		final double dB5 = data[2][2] * data[3][3] - data[2][3] * data[3][2];
		final double det = dA0 * dB5 - dA1 * dB4 + dA2 * dB3 + dA3 * dB2 - dA4 * dB1 + dA5 * dB0;

		if (Math.abs(det) <= MathUtils.EPSILON) {
			throw new ArithmeticException("This matrix cannot be inverted");
		}

		final double temp00 = +data[1][1] * dB5 - data[1][2] * dB4 + data[1][3] * dB3;
		final double temp10 = -data[1][0] * dB5 + data[1][2] * dB2 - data[1][3] * dB1;
		final double temp20 = +data[1][0] * dB4 - data[1][1] * dB2 + data[1][3] * dB0;
		final double temp30 = -data[1][0] * dB3 + data[1][1] * dB1 - data[1][2] * dB0;
		final double temp01 = -data[0][1] * dB5 + data[0][2] * dB4 - data[0][3] * dB3;
		final double temp11 = +data[0][0] * dB5 - data[0][2] * dB2 + data[0][3] * dB1;
		final double temp21 = -data[0][0] * dB4 + data[0][1] * dB2 - data[0][3] * dB0;
		final double temp31 = +data[0][0] * dB3 - data[0][1] * dB1 + data[0][2] * dB0;
		final double temp02 = +data[3][1] * dA5 - data[3][2] * dA4 + data[3][3] * dA3;
		final double temp12 = -data[3][0] * dA5 + data[3][2] * dA2 - data[3][3] * dA1;
		final double temp22 = +data[3][0] * dA4 - data[3][1] * dA2 + data[3][3] * dA0;
		final double temp32 = -data[3][0] * dA3 + data[3][1] * dA1 - data[3][2] * dA0;
		final double temp03 = -data[2][1] * dA5 + data[2][2] * dA4 - data[2][3] * dA3;
		final double temp13 = +data[2][0] * dA5 - data[2][2] * dA2 + data[2][3] * dA1;
		final double temp23 = -data[2][0] * dA4 + data[2][1] * dA2 - data[2][3] * dA0;
		final double temp33 = +data[2][0] * dA3 - data[2][1] * dA1 + data[2][2] * dA0;

		Matrix4 matrix4 = new Matrix4(
				temp00, temp01, temp02, temp03,
				temp10, temp11, temp12, temp13,
				temp20, temp21, temp22, temp23,
				temp30, temp31, temp32, temp33);

		double scalar = 1.0 / det;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matrix4.data[i][j] *= scalar;
			}
		}

		return matrix4;
	}

	/**
	 * The matrix to store the result in. If null, a new matrix is created.
	 *
	 * @return The adjugate, or classical adjoint, of this matrix
	 * @see <a href="http://en.wikipedia.org/wiki/Adjugate_matrix">wikipedia.org-Adjugate_matrix</a>
	 */
	public Matrix4 adjugate() {
		final double dA0 = data[0][0] * data[1][1] - data[0][1] * data[1][0];
		final double dA1 = data[0][0] * data[1][2] - data[0][2] * data[1][0];
		final double dA2 = data[0][0] * data[1][3] - data[0][3] * data[1][0];
		final double dA3 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		final double dA4 = data[0][1] * data[1][3] - data[0][3] * data[1][1];
		final double dA5 = data[0][2] * data[1][3] - data[0][3] * data[1][2];
		final double dB0 = data[2][0] * data[3][1] - data[2][1] * data[3][0];
		final double dB1 = data[2][0] * data[3][2] - data[2][2] * data[3][0];
		final double dB2 = data[2][0] * data[3][3] - data[2][3] * data[3][0];
		final double dB3 = data[2][1] * data[3][2] - data[2][2] * data[3][1];
		final double dB4 = data[2][1] * data[3][3] - data[2][3] * data[3][1];
		final double dB5 = data[2][2] * data[3][3] - data[2][3] * data[3][2];

		final double temp00 = +data[1][1] * dB5 - data[1][2] * dB4 + data[1][3] * dB3;
		final double temp10 = -data[1][0] * dB5 + data[1][2] * dB2 - data[1][3] * dB1;
		final double temp20 = +data[1][0] * dB4 - data[1][1] * dB2 + data[1][3] * dB0;
		final double temp30 = -data[1][0] * dB3 + data[1][1] * dB1 - data[1][2] * dB0;
		final double temp01 = -data[0][1] * dB5 + data[0][2] * dB4 - data[0][3] * dB3;
		final double temp11 = +data[0][0] * dB5 - data[0][2] * dB2 + data[0][3] * dB1;
		final double temp21 = -data[0][0] * dB4 + data[0][1] * dB2 - data[0][3] * dB0;
		final double temp31 = +data[0][0] * dB3 - data[0][1] * dB1 + data[0][2] * dB0;
		final double temp02 = +data[3][1] * dA5 - data[3][2] * dA4 + data[3][3] * dA3;
		final double temp12 = -data[3][0] * dA5 + data[3][2] * dA2 - data[3][3] * dA1;
		final double temp22 = +data[3][0] * dA4 - data[3][1] * dA2 + data[3][3] * dA0;
		final double temp32 = -data[3][0] * dA3 + data[3][1] * dA1 - data[3][2] * dA0;
		final double temp03 = -data[2][1] * dA5 + data[2][2] * dA4 - data[2][3] * dA3;
		final double temp13 = +data[2][0] * dA5 - data[2][2] * dA2 + data[2][3] * dA1;
		final double temp23 = -data[2][0] * dA4 + data[2][1] * dA2 - data[2][3] * dA0;
		final double temp33 = +data[2][0] * dA3 - data[2][1] * dA1 + data[2][2] * dA0;

		return new Matrix4(
				temp00, temp01, temp02, temp03,
				temp10, temp11, temp12, temp13,
				temp20, temp21, temp22, temp23,
				temp30, temp31, temp32, temp33);
	}

	/**
	 * @return the determinate of this matrix
	 * @see <a href="http://en.wikipedia.org/wiki/Determinant">wikipedia.org-Determinant</a>
	 */
	public double determinant() {
		final double dA0 = data[0][0] * data[1][1] - data[0][1] * data[1][0];
		final double dA1 = data[0][0] * data[1][2] - data[0][2] * data[1][0];
		final double dA2 = data[0][0] * data[1][3] - data[0][3] * data[1][0];
		final double dA3 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		final double dA4 = data[0][1] * data[1][3] - data[0][3] * data[1][1];
		final double dA5 = data[0][2] * data[1][3] - data[0][3] * data[1][2];
		final double dB0 = data[2][0] * data[3][1] - data[2][1] * data[3][0];
		final double dB1 = data[2][0] * data[3][2] - data[2][2] * data[3][0];
		final double dB2 = data[2][0] * data[3][3] - data[2][3] * data[3][0];
		final double dB3 = data[2][1] * data[3][2] - data[2][2] * data[3][1];
		final double dB4 = data[2][1] * data[3][3] - data[2][3] * data[3][1];
		final double dB5 = data[2][2] * data[3][3] - data[2][3] * data[3][2];
		return dA0 * dB5 - dA1 * dB4 + dA2 * dB3 + dA3 * dB2 - dA4 * dB1 + dA5 * dB0;
	}

	/**
	 * Multiplies the given vector by this matrix (v * M).
	 *
	 * @param vector the vector to multiply this matrix by.
	 * @return a new vector
	 * @throws NullPointerException if vector is null
	 */
	public Vector4 applyPre(final Vector4 vector) {
		final double x = vector.getX();
		final double y = vector.getY();
		final double z = vector.getZ();
		final double w = vector.getW();

		double vX = (data[0][0] * x + data[1][0] * y + data[2][0] * z + data[3][0] * w);
		double vY = (data[0][1] * x + data[1][1] * y + data[2][1] * z + data[3][1] * w);
		double vZ = (data[0][2] * x + data[1][2] * y + data[2][2] * z + data[3][2] * w);
		double vW = (data[0][3] * x + data[1][3] * y + data[2][3] * z + data[3][3] * w);

		return new Vector4(vX, vY, vZ, vW);
	}


	/**
	 * Multiplies the given vector by this matrix (M * v).
	 *
	 * @param vector the vector to multiply this matrix by.
	 * @return a new vector .
	 * @throws NullPointerException if vector is null
	 */
	public Vector4 applyPost(final Vector4 vector) {

		final double x = vector.getX();
		final double y = vector.getY();
		final double z = vector.getZ();
		final double w = vector.getW();

		double vX = (data[0][0] * x + data[0][1] * y + data[0][2] * z + data[0][3] * w);
		double vY = (data[1][0] * x + data[1][1] * y + data[1][2] * z + data[1][3] * w);
		double vZ = (data[2][0] * x + data[2][1] * y + data[2][2] * z + data[2][3] * w);
		double vW = (data[3][0] * x + data[3][1] * y + data[3][2] * z + data[3][3] * w);

		return new Vector4(vX, vY, vZ, vW);
	}

	public static boolean isValid(final Matrix4 matrix) {
		if (matrix == null) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				final double val = matrix.getValue(i, j);
				if (Double.isNaN(val) || Double.isInfinite(val)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isOrthonormal() {
		return transpose().equals(invert());
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder("Matrix4\n[\n");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.append(" ");
				result.append(data[i][j]);
			}
			result.append(" \n");
		}
		result.append("]");
		return result.toString();
	}

	@Override
	public int hashCode() {
		// TODO: Probably worth caching this.
		int result = 17;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				final long val = Double.doubleToLongBits(data[i][j]);
				result += 31 * result + (int) (val ^ (val >>> 32));
			}
		}

		return result;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Matrix4)) {
			return false;
		}
		final Matrix4 comp = (Matrix4) o;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (Math.abs(getValue(i, j) - comp.getValue(i, j)) > MathUtils.ZERO_TOLERANCE) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param o the object to compare for equality
	 * @return true if this matrix and the provided matrix have the exact same double values.
	 */
	public boolean strictEquals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Matrix4)) {
			return false;
		}
		final Matrix4 comp = (Matrix4) o;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (getValue(i, j) != comp.getValue(i, j)) {
					return false;
				}
			}
		}

		return true;
	}
}
