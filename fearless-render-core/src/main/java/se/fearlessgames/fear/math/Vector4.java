package se.fearlessgames.fear.math;

public class Vector4 {
	public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);
	public static final Vector4 ONE = new Vector4(1, 1, 1, 1);
	public static final Vector4 NEG_ONE = new Vector4(-1, -1, -1, -1);
	public static final Vector4 UNIT_X = new Vector4(1, 0, 0, 0);
	public static final Vector4 NEG_UNIT_X = new Vector4(-1, 0, 0, 0);
	public static final Vector4 UNIT_Y = new Vector4(0, 1, 0, 0);
	public static final Vector4 NEG_UNIT_Y = new Vector4(0, -1, 0, 0);
	public static final Vector4 UNIT_Z = new Vector4(0, 0, 1, 0);
	public static final Vector4 NEG_UNIT_Z = new Vector4(0, 0, -1, 0);
	public static final Vector4 UNIT_W = new Vector4(0, 0, 0, 1);
	public static final Vector4 NEG_UNIT_W = new Vector4(0, 0, 0, -1);

	private final double x;
	private final double y;
	private final double z;
	private final double w;

	public Vector4(final Vector4 src) {
		this(src.getX(), src.getY(), src.getZ(), src.getW());
	}

	public Vector4(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public double[] toArray(double[] store) {
		if (store == null) {
			store = new double[4];
		}
		// do last first to ensure size is correct before any edits occur.
		store[3] = getW();
		store[2] = getZ();
		store[1] = getY();
		store[0] = getX();
		return store;
	}

	public Vector4 add(final double x, final double y, final double z, final double w) {
		return new Vector4(getX() + x, getY() + y, getZ() + z, getW() + w);
	}

	public Vector4 add(final Vector4 source) {
		return add(source.getX(), source.getY(), source.getZ(), source.getW());
	}

	public Vector4 subtract(final double x, final double y, final double z, final double w) {
		return new Vector4(getX() - x, getY() - y, getZ() - z, getW() - w);
	}

	public Vector4 subtract(final Vector4 source) {
		return subtract(source.getX(), source.getY(), source.getZ(), source.getW());
	}

	public Vector4 multiply(final double scalar) {
		return new Vector4(getX() * scalar, getY() * scalar, getZ() * scalar, getW() * scalar);
	}

	public Vector4 multiply(final Vector4 scale) {
		return new Vector4(getX() * scale.getX(), getY() * scale.getY(), getZ() * scale.getZ(), getW() * scale.getW());
	}

	public Vector4 divide(final double scalar) {
		return new Vector4(getX() / scalar, getY() / scalar, getZ() / scalar, getW() / scalar);
	}

	public Vector4 divide(final Vector4 scale) {
		return new Vector4(getX() / scale.getX(), getY() / scale.getY(), getZ() / scale.getZ(), getW() / scale.getW());
	}

	public Vector4 scaleAdd(final double scale, final Vector4 add) {
		double x = (this.x * scale + add.getX());
		double y = (this.y * scale + add.getY());
		double z = (this.z * scale + add.getZ());
		double w = (this.w * scale + add.getW());
		return new Vector4(x, y, z, w);
	}

	public Vector4 negate() {
		return multiply(-1);
	}

	public Vector4 normalize() {
		final double lengthSq = lengthSquared();
		if (Math.abs(lengthSq) > MathUtils.EPSILON) {
			return multiply(MathUtils.inverseSqrt(lengthSq));
		}

		return ZERO;
	}

	public Vector4 lerp(final Vector4 endVec, final double scalar) {
		final double x = (1.0 - scalar) * getX() + scalar * endVec.getX();
		final double y = (1.0 - scalar) * getY() + scalar * endVec.getY();
		final double z = (1.0 - scalar) * getZ() + scalar * endVec.getZ();
		final double w = (1.0 - scalar) * getW() + scalar * endVec.getW();
		return new Vector4(x, y, z, w);
	}

	public static Vector4 lerp(final Vector4 beginVec, final Vector4 endVec, final double scalar) {
		final double x = (1.0 - scalar) * beginVec.getX() + scalar * endVec.getX();
		final double y = (1.0 - scalar) * beginVec.getY() + scalar * endVec.getY();
		final double z = (1.0 - scalar) * beginVec.getZ() + scalar * endVec.getZ();
		final double w = (1.0 - scalar) * beginVec.getW() + scalar * endVec.getW();
		return new Vector4(x, y, z, w);
	}

	public double length() {
		return MathUtils.sqrt(lengthSquared());
	}

	public double lengthSquared() {
		return getX() * getX() + getY() * getY() + getZ() * getZ() + getW() * getW();
	}

	public double distanceSquared(final double x, final double y, final double z, final double w) {
		final double dx = getX() - x;
		final double dy = getY() - y;
		final double dz = getZ() - z;
		final double dw = getW() - w;
		return dx * dx + dy * dy + dz * dz + dw * dw;
	}

	public double distanceSquared(final Vector4 destination) {
		return distanceSquared(destination.getX(), destination.getY(), destination.getZ(), destination.getW());
	}

	public double distance(final double x, final double y, final double z, final double w) {
		return MathUtils.sqrt(distanceSquared(x, y, z, w));
	}

	public double distance(final Vector4 destination) {
		return MathUtils.sqrt(distanceSquared(destination));
	}

	public double dot(final double x, final double y, final double z, final double w) {
		return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
	}

	public double determinant(final double x, final double y, final double z, final double w) {
		return (getX() * x) - (getY() * y) - (getZ() * z) - (getW() * w);
	}

	public double determinant(final Vector4 vec) {
		return determinant(vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}

	public static boolean isValid(final Vector4 vector) {
		if (vector == null) {
			return false;
		}
		if (Double.isNaN(vector.getX()) || Double.isNaN(vector.getY()) || Double.isNaN(vector.getZ())
				|| Double.isNaN(vector.getW())) {
			return false;
		}
		if (Double.isInfinite(vector.getX()) || Double.isInfinite(vector.getY()) || Double.isInfinite(vector.getZ())
				|| Double.isInfinite(vector.getW())) {
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
	 * @return true if this vector and the provided vector have the same x, y, z and w values.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Vector4)) {
			return false;
		}
		final Vector4 comp = (Vector4) o;
		return getX() == comp.getX() && getY() == comp.getY() && getZ() == comp.getZ() && getW() == comp.getW();
	}
}
