package se.fearlessgames.fear.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Matrix3 {
	public final static Matrix3 IDENTITY = new Matrix3(1, 0, 0, 0, 1, 0, 0, 0, 1);
	private final double[][] data = new double[3][3];

	public Matrix3(final double m00, final double m01, final double m02, final double m10, final double m11,
				   final double m12, final double m20, final double m21, final double m22) {

		data[0][0] = m00;
		data[0][1] = m01;
		data[0][2] = m02;
		data[1][0] = m10;
		data[1][1] = m11;
		data[1][2] = m12;
		data[2][0] = m20;
		data[2][1] = m21;
		data[2][2] = m22;
	}

	public Matrix3(final Matrix3 source) {
		// Unrolled for better performance.
		data[0][0] = source.getValue(0, 0);
		data[1][0] = source.getValue(1, 0);
		data[2][0] = source.getValue(2, 0);

		data[0][1] = source.getValue(0, 1);
		data[1][1] = source.getValue(1, 1);
		data[2][1] = source.getValue(2, 1);

		data[0][2] = source.getValue(0, 2);
		data[1][2] = source.getValue(1, 2);
		data[2][2] = source.getValue(2, 2);
	}

	public Matrix3() {
	}

	public Matrix3(final double[] source) {
		this(source, true);
	}

	public Matrix3(final double[] source, final boolean rowMajor) {
		if (rowMajor) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					data[i][j] = source[i * 3 + j];
				}
			}
		} else {
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
					data[i][j] = source[j * 3 + i];
				}
			}
		}
	}


	public static Matrix3 fromAxes(final Vector3 uAxis, final Vector3 vAxis, final Vector3 wAxis) {
		Matrix3 matrix3 = new Matrix3();
		matrix3.data[0][0] = uAxis.getX();
		matrix3.data[0][1] = uAxis.getY();
		matrix3.data[0][2] = uAxis.getZ();

		matrix3.data[1][0] = vAxis.getX();
		matrix3.data[1][1] = vAxis.getY();
		matrix3.data[1][2] = vAxis.getZ();

		matrix3.data[2][0] = wAxis.getX();
		matrix3.data[2][1] = wAxis.getY();
		matrix3.data[2][2] = wAxis.getZ();
		return matrix3;
	}

	public static Matrix3 fromAngleAxis(final double angle, final Vector3 axis) {
		final Vector3 normAxis = axis.normalize();
		return fromAngleNormalAxis(angle, normAxis);
	}

	public static Matrix3 fromAngleNormalAxis(final double angle, final Vector3 axis) {
		Matrix3 matrix3 = new Matrix3();
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

		matrix3.data[0][0] = fX2 * fOneMinusCos + fCos;
		matrix3.data[0][1] = fXYM - fZSin;
		matrix3.data[0][2] = fXZM + fYSin;
		matrix3.data[1][0] = fXYM + fZSin;
		matrix3.data[1][1] = fY2 * fOneMinusCos + fCos;
		matrix3.data[1][2] = fYZM - fXSin;
		matrix3.data[2][0] = fXZM - fYSin;
		matrix3.data[2][1] = fYZM + fXSin;
		matrix3.data[2][2] = fZ2 * fOneMinusCos + fCos;

		return matrix3;
	}

	public static Matrix3 fromAngles(final double yaw, final double roll, final double pitch) {
		Matrix3 matrix3 = new Matrix3();
		final double ch = Math.cos(roll);
		final double sh = Math.sin(roll);
		final double cp = Math.cos(pitch);
		final double sp = Math.sin(pitch);
		final double cy = Math.cos(yaw);
		final double sy = Math.sin(yaw);

		matrix3.data[0][0] = ch * cp;
		matrix3.data[0][1] = sh * sy - ch * sp * cy;
		matrix3.data[0][2] = ch * sp * sy + sh * cy;
		matrix3.data[1][0] = sp;
		matrix3.data[1][1] = cp * cy;
		matrix3.data[1][2] = -cp * sy;
		matrix3.data[2][0] = -sh * cp;
		matrix3.data[2][1] = sh * sp * cy + ch * sy;
		matrix3.data[2][2] = -sh * sp * sy + ch * cy;
		return matrix3;
	}


	public double getValue(final int row, final int column) {
		return data[row][column];
	}

	public boolean isIdentity() {
		return equals(IDENTITY);
	}


	public Matrix3 setColumn(final int columnIndex, final Vector3 columnData) {
		Matrix3 matrix3 = new Matrix3(this);
		matrix3.data[0][columnIndex] = columnData.getX();
		matrix3.data[1][columnIndex] = columnData.getY();
		matrix3.data[2][columnIndex] = columnData.getZ();
		return matrix3;
	}

	public Matrix3 setRow(final int rowIndex, final Vector3 rowData) {
		Matrix3 matrix3 = new Matrix3(this);
		matrix3.data[rowIndex][0] = rowData.getX();
		matrix3.data[rowIndex][1] = rowData.getY();
		matrix3.data[rowIndex][2] = rowData.getZ();
		return matrix3;
	}


	public Vector3 getColumn(final int index) {
		if (index < 0 || index > 2) {
			throw new IllegalArgumentException("Illegal column index: " + index);
		}
		return new Vector3(data[0][index], data[1][index], data[2][index]);
	}

	public Vector3 getRow(final int index) {
		if (index < 0 || index > 2) {
			throw new IllegalArgumentException("Illegal row index: " + index);
		}

		return new Vector3(data[index][0], data[index][1], data[index][2]);
	}

	public DoubleBuffer toDoubleBuffer() {
		return toDoubleBuffer(true);
	}

	public DoubleBuffer toDoubleBuffer(final boolean rowMajor) {
		DoubleBuffer result = ByteBuffer.allocateDirect(8 * 9).order(ByteOrder.nativeOrder()).asDoubleBuffer();
		result.clear();


		if (rowMajor) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					result.put(data[i][j]);
				}
			}
		} else {
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
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
		FloatBuffer result = ByteBuffer.allocateDirect(4 * 9).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.clear();


		if (rowMajor) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					result.put((float) data[i][j]);
				}
			}
		} else {
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
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
			result = new double[9];
		} else if (result.length < 9) {
			throw new IllegalArgumentException("store must be at least length 9.");
		}

		if (rowMajor) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					result[i * 3 + j] = data[i][j];
				}
			}
		} else {
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
					result[j * 3 + i] = data[i][j];
				}
			}
		}

		return result;
	}

	public double[] toAngles(final double[] store) {
		double[] result = store;
		if (result == null) {
			result = new double[3];
		} else if (result.length < 3) {
			throw new IllegalArgumentException("store array must have at least three elements");
		}

		double heading, attitude, bank;
		if (data[1][0] > 0.998) { // singularity at north pole
			heading = Math.atan2(data[0][2], data[2][2]);
			attitude = Math.PI / 2;
			bank = 0;
		} else if (data[1][0] < -0.998) { // singularity at south pole
			heading = Math.atan2(data[0][2], data[2][2]);
			attitude = -Math.PI / 2;
			bank = 0;
		} else {
			heading = Math.atan2(-data[2][0], data[0][0]);
			bank = Math.atan2(-data[1][2], data[1][1]);
			attitude = Math.asin(data[1][0]);
		}
		result[0] = bank;
		result[1] = heading;
		result[2] = attitude;

		return result;
	}

	public Matrix3 multiply(final Matrix3 matrix) {

		final double temp00 = data[0][0] * matrix.getValue(0, 0) + data[0][1] * matrix.getValue(1, 0) + data[0][2]
				* matrix.getValue(2, 0);
		final double temp01 = data[0][0] * matrix.getValue(0, 1) + data[0][1] * matrix.getValue(1, 1) + data[0][2]
				* matrix.getValue(2, 1);
		final double temp02 = data[0][0] * matrix.getValue(0, 2) + data[0][1] * matrix.getValue(1, 2) + data[0][2]
				* matrix.getValue(2, 2);
		final double temp10 = data[1][0] * matrix.getValue(0, 0) + data[1][1] * matrix.getValue(1, 0) + data[1][2]
				* matrix.getValue(2, 0);
		final double temp11 = data[1][0] * matrix.getValue(0, 1) + data[1][1] * matrix.getValue(1, 1) + data[1][2]
				* matrix.getValue(2, 1);
		final double temp12 = data[1][0] * matrix.getValue(0, 2) + data[1][1] * matrix.getValue(1, 2) + data[1][2]
				* matrix.getValue(2, 2);
		final double temp20 = data[2][0] * matrix.getValue(0, 0) + data[2][1] * matrix.getValue(1, 0) + data[2][2]
				* matrix.getValue(2, 0);
		final double temp21 = data[2][0] * matrix.getValue(0, 1) + data[2][1] * matrix.getValue(1, 1) + data[2][2]
				* matrix.getValue(2, 1);
		final double temp22 = data[2][0] * matrix.getValue(0, 2) + data[2][1] * matrix.getValue(1, 2) + data[2][2]
				* matrix.getValue(2, 2);

		return new Matrix3(temp00, temp01, temp02, temp10, temp11, temp12, temp20, temp21, temp22);
	}

	public Vector3 applyPre(final Vector3 vec) {

		double x = vec.getX();
		double y = vec.getY();
		double z = vec.getZ();

		double vX = (data[0][0] * x + data[1][0] * y + data[2][0] * z);
		double vY = (data[0][1] * x + data[1][1] * y + data[2][1] * z);
		double vZ = (data[0][2] * x + data[1][2] * y + data[2][2] * z);
		return new Vector3(vX, vY, vZ);
	}

	public Vector3 applyPost(final Vector3 vec) {
		final double x = vec.getX();
		final double y = vec.getY();
		final double z = vec.getZ();

		double vX = (data[0][0] * x + data[0][1] * y + data[0][2] * z);
		double vY = (data[1][0] * x + data[1][1] * y + data[1][2] * z);
		double vZ = (data[2][0] * x + data[2][1] * y + data[2][2] * z);

		return new Vector3(vX, vY, vZ);
	}

	public Matrix3 multiplyDiagonalPre(final Vector3 vec) {
		return new Matrix3(vec.getX() * data[0][0], vec.getX() * data[0][1], vec.getX() * data[0][2], vec.getY()
				* data[1][0], vec.getY() * data[1][1], vec.getY() * data[1][2], vec.getZ() * data[2][0], vec.getZ()
				* data[2][1], vec.getZ() * data[2][2]);

	}

	public Matrix3 multiplyDiagonalPost(final Vector3 vec) {
		return new Matrix3(vec.getX() * data[0][0], vec.getY() * data[0][1], vec.getZ() * data[0][2], vec.getX()
				* data[1][0], vec.getY() * data[1][1], vec.getZ() * data[1][2], vec.getX() * data[2][0], vec.getY()
				* data[2][1], vec.getZ() * data[2][2]);
	}

	public Matrix3 add(final Matrix3 matrix) {
		Matrix3 matrix3 = new Matrix3(this);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrix3.data[i][j] += matrix.getValue(i, j);
			}
		}
		return matrix3;
	}

	public Matrix3 scale(final Vector3 scale) {
		return new Matrix3(data[0][0] * scale.getX(), data[0][1] * scale.getY(), data[0][2] * scale.getZ(),
				data[1][0] * scale.getX(), data[1][1] * scale.getY(), data[1][2] * scale.getZ(), data[2][0]
				* scale.getX(), data[2][1] * scale.getY(), data[2][2] * scale.getZ());
	}

	/**
	 * transposes this matrix as a new matrix, basically flipping it across the diagonal
	 *
	 * @return the new transposed matrix.
	 * @see <a href="http://en.wikipedia.org/wiki/Transpose">wikipedia.org-Transpose</a>
	 */
	public Matrix3 transpose() {
		return new Matrix3(data[0][0], data[1][0], data[2][0], data[0][1], data[1][1], data[2][1], data[0][2], data[1][2], data[2][2]);
	}

	public Matrix3 invert() {
		final double det = determinant();
		if (Math.abs(det) <= MathUtils.EPSILON) {
			throw new ArithmeticException("This matrix cannot be inverted.");
		}

		final double temp00 = data[1][1] * data[2][2] - data[1][2] * data[2][1];
		final double temp01 = data[0][2] * data[2][1] - data[0][1] * data[2][2];
		final double temp02 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		final double temp10 = data[1][2] * data[2][0] - data[1][0] * data[2][2];
		final double temp11 = data[0][0] * data[2][2] - data[0][2] * data[2][0];
		final double temp12 = data[0][2] * data[1][0] - data[0][0] * data[1][2];
		final double temp20 = data[1][0] * data[2][1] - data[1][1] * data[2][0];
		final double temp21 = data[0][1] * data[2][0] - data[0][0] * data[2][1];
		final double temp22 = data[0][0] * data[1][1] - data[0][1] * data[1][0];
		Matrix3 matrix = new Matrix3(temp00, temp01, temp02, temp10, temp11, temp12, temp20, temp21, temp22);
		double scalar = 1.0d / det;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrix.data[i][j] *= scalar;
			}
		}

		return matrix;
	}

	public Matrix3 adjugate() {
		final double temp00 = data[1][1] * data[2][2] - data[1][2] * data[2][1];
		final double temp01 = data[0][2] * data[2][1] - data[0][1] * data[2][2];
		final double temp02 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		final double temp10 = data[1][2] * data[2][0] - data[1][0] * data[2][2];
		final double temp11 = data[0][0] * data[2][2] - data[0][2] * data[2][0];
		final double temp12 = data[0][2] * data[1][0] - data[0][0] * data[1][2];
		final double temp20 = data[1][0] * data[2][1] - data[1][1] * data[2][0];
		final double temp21 = data[0][1] * data[2][0] - data[0][0] * data[2][1];
		final double temp22 = data[0][0] * data[1][1] - data[0][1] * data[1][0];

		return new Matrix3(temp00, temp01, temp02, temp10, temp11, temp12, temp20, temp21, temp22);
	}

	/**
	 * A function for creating a rotation matrix that rotates a vector called "start" into another vector called "end".
	 *
	 * @param start normalized non-zero starting vector
	 * @param end   normalized non-zero ending vector
	 * @return the new rotation matrix
	 * @see "Tomas Müller, John Hughes \"Efficiently Building a Matrix to Rotate \ One Vector to
	 *      Another\" Journal of Graphics Tools, 4(4):1-4, 1999"
	 */
	public Matrix3 fromStartEndLocal(final Vector3 start, final Vector3 end) {
		Matrix3 matrix3 = new Matrix3(this);
		double e, h, f;
		Vector3 v = start.cross(end);

		e = start.dot(end);
		f = (e < 0) ? -e : e;

		// if "from" and "to" vectors are nearly parallel
		if (f > 1.0 - MathUtils.ZERO_TOLERANCE) {


			double c1, c2, c3; /* coefficients for later use */
			int i, j;

			double xX = ((start.getX() > 0.0) ? start.getX() : -start.getX());
			double xY = ((start.getY() > 0.0) ? start.getY() : -start.getY());
			double xZ = ((start.getZ() > 0.0) ? start.getZ() : -start.getZ());
			Vector3 x = new Vector3(xX, xY, xZ);

			if (x.getX() < x.getY()) {
				if (x.getX() < x.getZ()) {
					x = new Vector3(1.0, 0.0, 0.0);
				} else {
					x = new Vector3(0.0, 0.0, 1.0);
				}
			} else {
				if (x.getY() < x.getZ()) {
					x = new Vector3(0.0, 1.0, 0.0);
				} else {
					x = new Vector3(0.0, 0.0, 1.0);
				}
			}
			Vector3 u = x.subtract(start);
			v = x.subtract(end);


			c1 = 2.0 / u.dot(u);
			c2 = 2.0 / v.dot(v);
			c3 = c1 * c2 * u.dot(v);

			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					final double val = -c1 * u.getValue(i) * u.getValue(j) - c2 * v.getValue(i) * v.getValue(j) + c3
							* v.getValue(i) * u.getValue(j);
					matrix3.data[i][j] = val;
				}
				final double val = data[i][i];
				matrix3.data[i][i] = val + 1.0;
			}
		} else {
			// the most common case, unless "start"="end", or "start"=-"end"
			double hvx, hvz, hvxy, hvxz, hvyz;
			h = 1.0 / (1.0 + e);
			hvx = h * v.getX();
			hvz = h * v.getZ();
			hvxy = hvx * v.getY();
			hvxz = hvx * v.getZ();
			hvyz = hvz * v.getY();
			matrix3.data[0][0] = e + hvx * v.getX();
			matrix3.data[0][1] = hvxy - v.getZ();
			matrix3.data[0][2] = hvxz + v.getY();

			matrix3.data[1][0] = hvxy + v.getZ();
			matrix3.data[1][1] = e + h * v.getY() * v.getY();
			matrix3.data[1][2] = hvyz - v.getX();

			matrix3.data[2][0] = hvxz - v.getY();
			matrix3.data[2][1] = hvyz + v.getX();
			matrix3.data[2][2] = e + hvz * v.getZ();
		}

		return matrix3;
	}

	/**
	 * @return the determinate of this matrix
	 * @see <a href="http://en.wikipedia.org/wiki/Determinant">wikipedia.org-Determinant</a>
	 */
	public double determinant() {
		final double fCo00 = data[1][1] * data[2][2] - data[1][2] * data[2][1];
		final double fCo10 = data[1][2] * data[2][0] - data[1][0] * data[2][2];
		final double fCo20 = data[1][0] * data[2][1] - data[1][1] * data[2][0];
		final double fDet = data[0][0] * fCo00 + data[0][1] * fCo10 + data[0][2] * fCo20;
		return fDet;
	}


	/**
	 * Creates a new matrix to equal the rotation required to point the z-axis at 'direction' and the y-axis to 'up'.
	 *
	 * @param direction where to 'look' at
	 * @param up		a vector indicating the local up direction.
	 * @return the new matrix
	 */
	public Matrix3 lookAt(final Vector3 direction, final Vector3 up) {
		Vector3 zAxis = direction.normalize();
		Vector3 xAxis = up.normalize().cross(zAxis);
		Vector3 yAxis = zAxis.cross(xAxis);

		return fromAxes(xAxis, yAxis, zAxis);
	}

	public static boolean isValid(final Matrix3 matrix) {
		if (matrix == null) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				final double val = matrix.getValue(i, j);
				if (Double.isNaN(val) || Double.isInfinite(val)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @return true if this Matrix is orthonormal - its rows are orthogonal, unit vectors.
	 */
	public boolean isOrthonormal() {
		return transpose().equals(invert());
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder("Matrix3\n[\n");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
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

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				final long val = Double.doubleToLongBits(data[i][j]);
				result += 31 * result + (int) (val ^ (val >>> 32));
			}
		}

		return result;
	}

	/**
	 * @param o the object to compare for equality
	 * @return true if this matrix and the provided matrix have the double values that are within the
	 *         MathUtils.ZERO_TOLERANCE.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Matrix3)) {
			return false;
		}
		final Matrix3 comp = (Matrix3) o;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (Math.abs(getValue(i, j) - comp.getValue(i, j)) > MathUtils.ZERO_TOLERANCE) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean strictEquals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Matrix3)) {
			return false;
		}
		final Matrix3 comp = (Matrix3) o;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getValue(i, j) != comp.getValue(i, j)) {
					return false;
				}
			}
		}

		return true;
	}

}
