package se.fearlessgames.fear.math;

public class Quaternion {
	public static final Quaternion IDENTITY = new Quaternion(0, 0, 0, 1);

	private final double x;
	private final double y;
	private final double z;
	private final double w;

	public Quaternion(final Quaternion source) {
		this(source.getX(), source.getY(), source.getZ(), source.getW());
	}

	public Quaternion(final double x, final double y, final double z, final double w) {
		this(x, y, z, w, false);
	}

	public Quaternion(double x, double y, double z, double w, boolean needsNormalization) {
		if (needsNormalization) {
			// normalization preprocessing
			double inv = 1.0 / MathUtils.sqrt(w * w + x * x + y * y + z * z);
			w *= inv;
			x *= inv;
			y *= inv;
			z *= inv;
		}
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;


	}

	public static Quaternion fromAngleAxis(final double angle, final Vector3 axis) {
		return fromAngleNormalAxis(angle, axis.normalize());
	}

	public static Quaternion fromAngleNormalAxis(final double angle, final Vector3 axis) {
		if (axis.equals(Vector3.ZERO)) {
			return IDENTITY;
		}

		final double halfAngle = 0.5 * angle;
		final double sin = MathUtils.sin(halfAngle);
		final double w = MathUtils.cos(halfAngle);
		final double x = sin * axis.getX();
		final double y = sin * axis.getY();
		final double z = sin * axis.getZ();
		return new Quaternion(x, y, z, w);
	}

	public static Quaternion fromAxes(final Vector3[] axes) {
		if (axes.length < 3) {
			throw new IllegalArgumentException("axes array must have at least three elements");
		}
		return fromAxes(axes[0], axes[1], axes[2]);
	}

	public static Quaternion fromAxes(final Vector3 xAxis, final Vector3 yAxis, final Vector3 zAxis) {
		return fromRotationMatrix(xAxis.getX(), yAxis.getX(), zAxis.getX(), xAxis.getY(), yAxis.getY(), zAxis.getY(),
				xAxis.getZ(), yAxis.getZ(), zAxis.getZ());
	}

	public static Quaternion lookAt(final Vector3 direction, final Vector3 up) {
		Vector3 zAxis = direction.normalize();
		Vector3 xAxis = up.normalize().cross(zAxis);
		Vector3 yAxis = zAxis.cross(xAxis);

		return fromAxes(xAxis, yAxis, zAxis).normalize();

	}


	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getW() {
		return w;
	}

	public double[] toArray(final double[] store) {
		double[] result = store;
		if (result == null) {
			result = new double[4];
		} else if (result.length < 4) {
			throw new IllegalArgumentException("store array must have at least three elements");
		}
		result[0] = getX();
		result[1] = getY();
		result[2] = getZ();
		result[3] = getW();
		return result;
	}

	public double[][] getMatrix() {

		// products
		double q0q0 = w * w;
		double q0q1 = w * x;
		double q0q2 = w * y;
		double q0q3 = w * z;
		double q1q1 = x * x;
		double q1q2 = x * y;
		double q1q3 = x * z;
		double q2q2 = y * y;
		double q2q3 = y * z;
		double q3q3 = z * z;

		// create the matrix
		double[][] m = new double[3][];
		m[0] = new double[3];
		m[1] = new double[3];
		m[2] = new double[3];

		m[0][0] = 2.0 * (q0q0 + q1q1) - 1.0;
		m[1][0] = 2.0 * (q1q2 - q0q3);
		m[2][0] = 2.0 * (q1q3 + q0q2);

		m[0][1] = 2.0 * (q1q2 + q0q3);
		m[1][1] = 2.0 * (q0q0 + q2q2) - 1.0;
		m[2][1] = 2.0 * (q2q3 - q0q1);

		m[0][2] = 2.0 * (q1q3 - q0q2);
		m[1][2] = 2.0 * (q2q3 + q0q1);
		m[2][2] = 2.0 * (q0q0 + q3q3) - 1.0;

		return m;

	}

	public Quaternion fromEulerAngles(final double[] angles) {
		if (angles.length != 3) {
			throw new IllegalArgumentException("Angles array must have three elements");
		}

		return fromEulerAngles(angles[0], angles[1], angles[2]);
	}

	public static Quaternion fromEulerAngles(final double heading, final double attitude, final double bank) {
		double angle = heading * 0.5;
		final double sinHeading = MathUtils.sin(angle);
		final double cosHeading = MathUtils.cos(angle);
		angle = attitude * 0.5;
		final double sinAttitude = MathUtils.sin(angle);
		final double cosAttitude = MathUtils.cos(angle);
		angle = bank * 0.5;
		final double sinBank = MathUtils.sin(angle);
		final double cosBank = MathUtils.cos(angle);

		// variables used to reduce multiplication calls.
		final double cosHeadingXcosAttitude = cosHeading * cosAttitude;
		final double sinHeadingXsinAttitude = sinHeading * sinAttitude;
		final double cosHeadingXsinAttitude = cosHeading * sinAttitude;
		final double sinHeadingXcosAttitude = sinHeading * cosAttitude;

		final double w = (cosHeadingXcosAttitude * cosBank - sinHeadingXsinAttitude * sinBank);
		final double x = (cosHeadingXcosAttitude * sinBank + sinHeadingXsinAttitude * cosBank);
		final double y = (sinHeadingXcosAttitude * cosBank + cosHeadingXsinAttitude * sinBank);
		final double z = (cosHeadingXsinAttitude * cosBank - sinHeadingXcosAttitude * sinBank);

		return new Quaternion(x, y, z, w).normalize();
	}

	public double[] toEulerAngles(final double[] store) {
		double[] result = store;
		if (result == null) {
			result = new double[3];
		} else if (result.length < 3) {
			throw new IllegalArgumentException("store array must have at least three elements");
		}

		final double sqw = getW() * getW();
		final double sqx = getX() * getX();
		final double sqy = getY() * getY();
		final double sqz = getZ() * getZ();
		final double unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise
		// is correction factor
		final double test = getX() * getY() + getZ() * getW();
		if (test > 0.499 * unit) { // singularity at north pole
			result[0] = 2 * Math.atan2(getX(), getW());
			result[1] = MathUtils.HALF_PI;
			result[2] = 0;
		} else if (test < -0.499 * unit) { // singularity at south pole
			result[0] = -2 * Math.atan2(getX(), getW());
			result[1] = -MathUtils.HALF_PI;
			result[2] = 0;
		} else {
			result[0] = Math.atan2(2 * getY() * getW() - 2 * getX() * getZ(), sqx - sqy - sqz + sqw);
			result[1] = Math.asin(2 * test / unit);
			result[2] = Math.atan2(2 * getX() * getW() - 2 * getY() * getZ(), -sqx + sqy - sqz + sqw);
		}
		return result;
	}

	public static Quaternion fromRotationMatrix(final Matrix3 matrix) {
		return fromRotationMatrix(matrix.getValue(0, 0), matrix.getValue(0, 1), matrix.getValue(0, 2), matrix.getValue(
				1, 0), matrix.getValue(1, 1), matrix.getValue(1, 2), matrix.getValue(2, 0), matrix.getValue(2, 1),
				matrix.getValue(2, 2));
	}

	public static Quaternion fromRotationMatrix(final double m00, final double m01, final double m02, final double m10,
												final double m11, final double m12, final double m20, final double m21, final double m22) {
		// Uses the Graphics Gems code, from
		// ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z
		// *NOT* the "Matrix and Quaternions FAQ", which has errors!

		// the trace is the sum of the diagonal elements; see
		// http://mathworld.wolfram.com/MatrixTrace.html
		final double t = m00 + m11 + m22;

		// we protect the division by s by ensuring that s>=1
		double x, y, z, w;
		if (t >= 0) { // |w| >= .5
			double s = Math.sqrt(t + 1); // |s|>=1 ...
			w = 0.5 * s;
			s = 0.5 / s; // so this division isn't bad
			x = (m21 - m12) * s;
			y = (m02 - m20) * s;
			z = (m10 - m01) * s;
		} else if ((m00 > m11) && (m00 > m22)) {
			double s = Math.sqrt(1.0 + m00 - m11 - m22); // |s|>=1
			x = s * 0.5; // |x| >= .5
			s = 0.5 / s;
			y = (m10 + m01) * s;
			z = (m02 + m20) * s;
			w = (m21 - m12) * s;
		} else if (m11 > m22) {
			double s = Math.sqrt(1.0 + m11 - m00 - m22); // |s|>=1
			y = s * 0.5; // |y| >= .5
			s = 0.5 / s;
			x = (m10 + m01) * s;
			z = (m21 + m12) * s;
			w = (m02 - m20) * s;
		} else {
			double s = Math.sqrt(1.0 + m22 - m00 - m11); // |s|>=1
			z = s * 0.5; // |z| >= .5
			s = 0.5 / s;
			x = (m02 + m20) * s;
			y = (m21 + m12) * s;
			w = (m10 - m01) * s;
		}

		return new Quaternion(x, y, z, w);
	}

	public Matrix3 toRotationMatrix3() {

		final double norm = magnitudeSquared();
		final double s = (norm > 0.0 ? 2.0 / norm : 0.0);

		// compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
		// will be used 2-4 times each.
		final double xs = getX() * s;
		final double ys = getY() * s;
		final double zs = getZ() * s;
		final double xx = getX() * xs;
		final double xy = getX() * ys;
		final double xz = getX() * zs;
		final double xw = getW() * xs;
		final double yy = getY() * ys;
		final double yz = getY() * zs;
		final double yw = getW() * ys;
		final double zz = getZ() * zs;
		final double zw = getW() * zs;

		// using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here
		double m00 = 1.0 - (yy + zz);
		double m01 = xy - zw;
		double m02 = xz + yw;
		double m10 = xy + zw;
		double m11 = 1.0 - (xx + zz);
		double m12 = yz - xw;
		double m20 = xz - yw;
		double m21 = yz + xw;
		double m22 = 1.0 - (xx + yy);

		return new Matrix3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}

	public Matrix4 toRotationMatrix4() {


		final double norm = magnitudeSquared();
		final double s = (norm == 1.0 ? 2.0 : (norm > 0.0 ? 2.0 / norm : 0));

		// compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
		// will be used 2-4 times each.
		final double xs = getX() * s;
		final double ys = getY() * s;
		final double zs = getZ() * s;
		final double xx = getX() * xs;
		final double xy = getX() * ys;
		final double xz = getX() * zs;
		final double xw = getW() * xs;
		final double yy = getY() * ys;
		final double yz = getY() * zs;
		final double yw = getW() * ys;
		final double zz = getZ() * zs;
		final double zw = getW() * zs;

		// using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here

		double m00 = (1.0 - (yy + zz));
		double m01 = (xy - zw);
		double m02 = (xz + yw);
		double m03 = 0;

		double m10 = (xy + zw);
		double m11 = (1.0 - (xx + zz));
		double m12 = (yz - xw);
		double m13 = 0;

		double m20 = (xz - yw);
		double m21 = (yz + xw);
		double m22 = (1.0 - (xx + yy));
		double m23 = 0;

		double m30 = 0;
		double m31 = 0;
		double m32 = 0;
		double m33 = 0;

		return new Matrix4(
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33);
	}


	public Vector3 getRotationColumn(final int index) {
		final double norm = magnitudeSquared();
		final double s = (norm == 1.0 ? 2.0 : (norm > 0.0 ? 2.0 / norm : 0));

		// compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
		// will be used 2-4 times each.
		final double xs = getX() * s;
		final double ys = getY() * s;
		final double zs = getZ() * s;
		final double xx = getX() * xs;
		final double xy = getX() * ys;
		final double xz = getX() * zs;
		final double xw = getW() * xs;
		final double yy = getY() * ys;
		final double yz = getY() * zs;
		final double yw = getW() * ys;
		final double zz = getZ() * zs;
		final double zw = getW() * zs;

		// using s=2/norm (instead of 1/norm) saves 3 multiplications by 2 here
		double x, y, z;
		switch (index) {
			case 0:
				x = 1.0 - (yy + zz);
				y = xy + zw;
				z = xz - yw;
				break;
			case 1:
				x = xy - zw;
				y = 1.0 - (xx + zz);
				z = yz + xw;
				break;
			case 2:
				x = xz + yw;
				y = yz - xw;
				z = 1.0 - (xx + yy);
				break;
			default:
				throw new IllegalArgumentException("Invalid column index. " + index);
		}

		return new Vector3(x, y, z);
	}


	public Vector3 toAngleAxis() {
		final double sqrLength = getX() * getX() + getY() * getY() + getZ() * getZ();
		if (Math.abs(sqrLength) <= MathUtils.EPSILON) { // length is ~0
			return new Vector3(1, 0, 0);

		} else {
			final double invLength = (1.0 / Math.sqrt(sqrLength));
			return new Vector3(
					getX() * invLength,
					getY() * invLength,
					getZ() * invLength);
		}
	}

	public double toAngle() {
		final double sqrLength = getX() * getX() + getY() * getY() + getZ() * getZ();
		double angle;
		if (Math.abs(sqrLength) <= MathUtils.EPSILON) { // length is ~0
			return 0.0;
		} else {
			return (2.0 * Math.acos(getW()));
		}
	}


	public Quaternion fromVectorToVector(final Vector3 from, final Vector3 to) {

		final double factor = from.length() * to.length();

		if (Math.abs(factor) > MathUtils.EPSILON) {
			// Vectors have length > 0
			//final Vector3 pivotVector = Vector3.fetchTempInstance();

			final double dot = from.dot(to) / factor;
			final double theta = Math.acos(Math.max(-1.0, Math.min(dot, 1.0)));
			Vector3 pivotVector = from.cross(to);

			if (dot < 0.0 && pivotVector.length() < MathUtils.EPSILON) {
				// Vectors parallel and opposite direction, therefore a rotation of 180 degrees about any vector
				// perpendicular to this vector will rotate vector a onto vector b.
				//
				// The following guarantees the dot-product will be 0.0.
				int dominantIndex;
				if (Math.abs(from.getX()) > Math.abs(from.getY())) {
					if (Math.abs(from.getX()) > Math.abs(from.getZ())) {
						dominantIndex = 0;
					} else {
						dominantIndex = 2;
					}
				} else {
					if (Math.abs(from.getY()) > Math.abs(from.getZ())) {
						dominantIndex = 1;
					} else {
						dominantIndex = 2;
					}
				}

				double[] pivotValue = new double[3];
				pivotValue[dominantIndex] = -from.getValue((dominantIndex + 1) % 3);
				pivotValue[(dominantIndex + 1) % 3] = from.getValue(dominantIndex);
				pivotValue[(dominantIndex + 2) % 3] = 0d;

				pivotVector = new Vector3(pivotValue[0], pivotValue[1], pivotValue[2]);

			}
			return fromAngleAxis(theta, pivotVector);

		} else {
			return IDENTITY;
		}
	}

	public Quaternion normalize() {
		final double n = 1.0 / magnitude();
		final double x = getX() * n;
		final double y = getY() * n;
		final double z = getZ() * n;
		final double w = getW() * n;
		return new Quaternion(x, y, z, w);
	}


	public Quaternion invert() {

		Quaternion quaternion = conjugate();
		// It is safe for store == this as a quaternion's conjugate has the same magnitude
		return quaternion.multiply(1.0d / magnitudeSquared());
	}

	public Quaternion conjugate() {
		return new Quaternion(-getX(), -getY(), -getZ(), getW());
	}

	public Quaternion add(final Quaternion quat) {
		return new Quaternion(getX() + quat.getX(), getY() + quat.getY(), getZ() + quat.getZ(), getW() + quat.getW());
	}

	public Quaternion subtract(final Quaternion quat) {
		return new Quaternion(getX() - quat.getX(), getY() - quat.getY(), getZ() - quat.getZ(), getW() - quat.getW());
	}

	public Quaternion multiply(final double scalar) {
		return new Quaternion(scalar * getX(), scalar * getY(), scalar * getZ(), scalar * getW());
	}

	public Quaternion multiply(final Quaternion quat) {
		final double x = getX() * quat.getW() + getY() * quat.getZ() - getZ() * quat.getY() + getW() * quat.getX();
		final double y = -getX() * quat.getZ() + getY() * quat.getW() + getZ() * quat.getX() + getW() * quat.getY();
		final double z = getX() * quat.getY() - getY() * quat.getX() + getZ() * quat.getW() + getW() * quat.getZ();
		final double w = -getX() * quat.getX() - getY() * quat.getY() - getZ() * quat.getZ() + getW() * quat.getW();
		return new Quaternion(x, y, z, w);
	}


	public Vector3 applyTo(Vector3 p1) {
		double zSq = z * z;
		double xSq = x * x;
		double ySQ = y * y;
		double wSq = w * w;
		double resultX = 2 * (w * (y * p1.getZ() - z * p1.getY()) + x * (y * p1.getY() + z * p1.getZ())) - zSq * p1.getX() - ySQ * p1.getX() + wSq * p1.getX() + xSq * p1.getX();
		double resultY = 2 * (y * (x * p1.getX() + z * p1.getZ()) + w * (z * p1.getX() - x * p1.getZ())) - xSq * p1.getY() + ySQ * p1.getY() - zSq * p1.getY() + wSq * p1.getY();
		double resultZ = 2 * (z * (x * p1.getX() + y * p1.getY()) + w * (x * p1.getY() - y * p1.getX())) - xSq * p1.getZ() + wSq * p1.getZ() + zSq * p1.getZ() - ySQ * p1.getZ();
		return new Vector3(resultX, resultY, resultZ);
	}

	public Quaternion applyTo(Quaternion r) {
		double resultX = x * r.getW() + y * r.getZ() - z * r.getY() + w * r.getX();
		double resultY = -x * r.getZ() + y * r.getW() + z * r.getX() + w * r.getY();
		double resultZ = x * r.getY() - y * r.getX() + z * r.getW() + w * r.getZ();
		double resultW = -x * r.getX() - y * r.getY() - z * r.getZ() + w * r.getW();
		return new Quaternion(resultX, resultY, resultZ, resultW);
	}

	public Quaternion applyInverseTo(Quaternion r) {
		Quaternion inverse = getInverse();
		return inverse.applyTo(r);
	}

	private Quaternion getInverse() {
		return normalize().conjugate();
	}

	public Vector3[] toAxes() {
		final Matrix3 tempMat = toRotationMatrix3();
		Vector3[] axes = new Vector3[3];
		axes[0] = tempMat.getColumn(0);
		axes[1] = tempMat.getColumn(1);
		axes[2] = tempMat.getColumn(2);
		return axes;
	}

	public Quaternion slerp(final Quaternion endQuat, final double changeAmnt) {
		return slerp(this, endQuat, changeAmnt);
	}


	/**
	 * Does a spherical linear interpolation between the given start and end quaternions by the given change amount.
	 * Returns the result as a new quaternion.
	 *
	 * @param startQuat
	 * @param endQuat
	 * @param changeAmnt
	 * @return the new quaternion
	 */
	public static Quaternion slerp(final Quaternion startQuat, final Quaternion endQuat, final double changeAmnt) {
		// check for weighting at either extreme
		if (changeAmnt == 0.0) {
			return new Quaternion(startQuat);
		} else if (changeAmnt == 1.0) {
			return new Quaternion(endQuat);
		}

		Quaternion q2 = new Quaternion(endQuat);
		// Check for equality and skip operation.
		if (startQuat.equals(q2)) {
			return new Quaternion(startQuat);
		}

		double dotP = startQuat.dot(q2);

		if (dotP < 0.0) {
			// Negate the second quaternion and the result of the dot product
			q2 = q2.multiply(-1d);
			dotP = -dotP;
		}

		// Set the first and second scale for the interpolation
		double scale0 = 1 - changeAmnt;
		double scale1 = changeAmnt;

		// Check if the angle between the 2 quaternions was big enough to
		// warrant such calculations
		if ((1 - dotP) > 0.1) {// Get the angle between the 2 quaternions,
			// and then store the sin() of that angle
			final double theta = Math.acos(dotP);
			final double invSinTheta = 1f / MathUtils.sin(theta);

			// Calculate the scale for q1 and q2, according to the angle and
			// it's sine value
			scale0 = MathUtils.sin((1 - changeAmnt) * theta) * invSinTheta;
			scale1 = MathUtils.sin((changeAmnt * theta)) * invSinTheta;
		}

		// Calculate the x, y, z and w values for the quaternion by using a
		// special form of linear interpolation for quaternions.
		final double x = (scale0 * startQuat.getX()) + (scale1 * q2.getX());
		final double y = (scale0 * startQuat.getY()) + (scale1 * q2.getY());
		final double z = (scale0 * startQuat.getZ()) + (scale1 * q2.getZ());
		final double w = (scale0 * startQuat.getW()) + (scale1 * q2.getW());

		// Return the interpolated quaternion
		return new Quaternion(x, y, z, w);
	}

	public double magnitudeSquared() {
		return getW() * getW() + getX() * getX() + getY() * getY() + getZ() * getZ();
	}

	public double magnitude() {
		final double magnitudeSQ = magnitudeSquared();
		if (magnitudeSQ == 1.0) {
			return 1.0;
		}

		return MathUtils.sqrt(magnitudeSQ);
	}

	public double dot(final double x, final double y, final double z, final double w) {
		return getX() * x + getY() * y + getZ() * z + getW() * w;
	}

	public double dot(final Quaternion quat) {
		return dot(quat.getX(), quat.getY(), quat.getZ(), quat.getW());
	}

	public boolean isIdentity() {
		return equals(IDENTITY);
	}

	public static boolean isValid(final Quaternion quat) {
		if (quat == null) {
			return false;
		}
		if (Double.isNaN(quat.getX()) || Double.isInfinite(quat.getX())) {
			return false;
		}
		if (Double.isNaN(quat.getY()) || Double.isInfinite(quat.getY())) {
			return false;
		}
		if (Double.isNaN(quat.getZ()) || Double.isInfinite(quat.getZ())) {
			return false;
		}
		if (Double.isNaN(quat.getW()) || Double.isInfinite(quat.getW())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;

		final long x = Double.doubleToLongBits(getX());
		result += 31 * result + (int) (x ^ (x >>> 32));

		final long y = Double.doubleToLongBits(getY());
		result += 31 * result + (int) (y ^ (y >>> 32));

		final long z = Double.doubleToLongBits(getZ());
		result += 31 * result + (int) (z ^ (z >>> 32));

		final long w = Double.doubleToLongBits(getW());
		result += 31 * result + (int) (w ^ (w >>> 32));

		return result;
	}

	/**
	 * @param o the object to compare for equality
	 * @return true if this quaternion and the provided quaternion have the same x, y, z and w values.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Quaternion)) {
			return false;
		}
		final Quaternion comp = (Quaternion) o;
		return getX() == comp.getX() && getY() == comp.getY() && getZ() == comp.getZ() && getW() == comp.getW();

	}

}



