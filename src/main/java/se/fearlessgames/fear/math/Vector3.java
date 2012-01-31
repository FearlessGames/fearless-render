package se.fearlessgames.fear.math;

public class Vector3 {
	public final static Vector3 ZERO = new Vector3(0, 0, 0);
	public final static Vector3 ONE = new Vector3(1, 1, 1);
	public final static Vector3 NEG_ONE = new Vector3(-1, -1, -1);
	public final static Vector3 UNIT_X = new Vector3(1, 0, 0);
	public final static Vector3 NEG_UNIT_X = new Vector3(-1, 0, 0);
	public final static Vector3 UNIT_Y = new Vector3(0, 1, 0);
	public final static Vector3 NEG_UNIT_Y = new Vector3(0, -1, 0);
	public final static Vector3 UNIT_Z = new Vector3(0, 0, 1);
	public final static Vector3 NEG_UNIT_Z = new Vector3(0, 0, -1);

	private final double x;
	private final double y;
	private final double z;


	public Vector3(final Vector3 src) {
		this(src.getX(), src.getY(), src.getZ());
	}

	public Vector3(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public double getValue(final int index) {
		switch (index) {
			case 0:
				return getX();
			case 1:
				return getY();
			case 2:
				return getZ();
		}
		throw new IllegalArgumentException("index must be either 0, 1 or 2");
	}


	public double[] toArray(double[] store) {
		if (store == null) {
			store = new double[3];
		}

		// do last first to ensure size is correct before any edits occur.
		store[2] = getZ();
		store[1] = getY();
		store[0] = getX();
		return store;
	}

	public Vector3 add(final double x, final double y, final double z) {
		return new Vector3(getX() + x, getY() + y, getZ() + z);
	}

	public Vector3 add(final Vector3 source) {
		return add(source.getX(), source.getY(), source.getZ());
	}

	public Vector3 subtract(final double x, final double y, final double z) {
		return new Vector3(getX() - x, getY() - y, getZ() - z);
	}

	public Vector3 subtract(final Vector3 source) {
		return subtract(source.getX(), source.getY(), source.getZ());
	}

	public Vector3 multiply(final double scalar) {
		return new Vector3(getX() * scalar, getY() * scalar, getZ() * scalar);
	}

	public Vector3 multiply(final Vector3 scale) {
		return new Vector3(getX() * scale.getX(), getY() * scale.getY(), getZ() * scale.getZ());
	}

	public Vector3 divide(final double scalar) {
		return new Vector3(getX() / scalar, getY() / scalar, getZ() / scalar);
	}

	public Vector3 divide(final Vector3 scale) {
		return new Vector3(getX() / scale.getX(), getY() / scale.getY(), getZ() / scale.getZ());
	}

	public Vector3 scaleAdd(final double scale, final Vector3 add) {
		return new Vector3(x * scale + add.getX(), y * scale + add.getY(), z * scale + add.getZ());
	}

	public Vector3 negate() {
		return multiply(-1);
	}

	public Vector3 normalize() {
		final double lengthSq = lengthSquared();
		if (Math.abs(lengthSq) > MathUtils.EPSILON) {
			return multiply(MathUtils.inverseSqrt(lengthSq));
		}

		return ZERO;
	}

	public Vector3 lerp(final Vector3 endVec, final double scalar) {
		final double x = (1.0 - scalar) * getX() + scalar * endVec.getX();
		final double y = (1.0 - scalar) * getY() + scalar * endVec.getY();
		final double z = (1.0 - scalar) * getZ() + scalar * endVec.getZ();
		return new Vector3(x, y, z);
	}

	public static Vector3 lerp(final Vector3 beginVec, final Vector3 endVec, final double scalar) {

		// Check for equality and skip operation if possible.
		if (!beginVec.equals(endVec)) {
			final double x = (1.0 - scalar) * beginVec.getX() + scalar * endVec.getX();
			final double y = (1.0 - scalar) * beginVec.getY() + scalar * endVec.getY();
			final double z = (1.0 - scalar) * beginVec.getZ() + scalar * endVec.getZ();
			return new Vector3(x, y, z);
		} else {
			return new Vector3(beginVec);
		}
	}

	public double length() {
		return MathUtils.sqrt(lengthSquared());
	}

	public double lengthSquared() {
		return getX() * getX() + getY() * getY() + getZ() * getZ();
	}

	public double distanceSquared(final double x, final double y, final double z) {
		final double dx = getX() - x;
		final double dy = getY() - y;
		final double dz = getZ() - z;
		return dx * dx + dy * dy + dz * dz;
	}

	public double distanceSquared(final Vector3 destination) {
		return distanceSquared(destination.getX(), destination.getY(), destination.getZ());
	}

	public double distance(final double x, final double y, final double z) {
		return MathUtils.sqrt(distanceSquared(x, y, z));
	}

	public double distance(final Vector3 destination) {
		return MathUtils.sqrt(distanceSquared(destination));
	}

	public double dot(final double x, final double y, final double z) {
		return (getX() * x) + (getY() * y) + (getZ() * z);
	}

	public double dot(final Vector3 vec) {
		return dot(vec.getX(), vec.getY(), vec.getZ());
	}

	public Vector3 cross(final double x, final double y, final double z) {
		final double newX = (getY() * z) - (getZ() * y);
		final double newY = (getZ() * x) - (getX() * z);
		final double newZ = (getX() * y) - (getY() * x);
		return new Vector3(newX, newY, newZ);
	}

	public Vector3 cross(final Vector3 vec) {
		return cross(vec.getX(), vec.getY(), vec.getZ());
	}

	public double determinant(final double x, final double y, final double z) {
		return (getX() * x) - (getY() * y) - (getZ() * z);
	}

	public double determinant(final Vector3 vec) {
		return determinant(vec.getX(), vec.getY(), vec.getZ());
	}

	public double smallestAngleBetween(final Vector3 otherVector) {
		return MathUtils.acos(dot(otherVector));
	}

	public static boolean isValid(final Vector3 vector) {
		if (vector == null) {
			return false;
		}
		if (Double.isNaN(vector.getX()) || Double.isNaN(vector.getY()) || Double.isNaN(vector.getZ())) {
			return false;
		}
		if (Double.isInfinite(vector.getX()) || Double.isInfinite(vector.getY()) || Double.isInfinite(vector.getZ())) {
			return false;
		}
		return true;
	}

	public static boolean isInfinite(final Vector3 vector) {
		if (vector == null) {
			throw new IllegalArgumentException("vector is null");
		}
		if (Double.isInfinite(vector.getX()) || Double.isInfinite(vector.getY()) || Double.isInfinite(vector.getZ())) {
			return true;
		}
		return false;
	}


	@Override
	public String toString() {
		return "Vector3{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}

	public int hashCode() {
		int result = 17;

		final long x = Double.doubleToLongBits(getX());
		result += 31 * result + (int) (x ^ (x >>> 32));

		final long y = Double.doubleToLongBits(getY());
		result += 31 * result + (int) (y ^ (y >>> 32));

		final long z = Double.doubleToLongBits(getZ());
		result += 31 * result + (int) (z ^ (z >>> 32));

		return result;
	}

	/**
	 * @param o the object to compare for equality
	 * @return true if this vector and the provided vector have the same x, y and z values.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Vector3)) {
			return false;
		}
		final Vector3 comp = (Vector3) o;
		return getX() == comp.getX() && getY() == comp.getY() && getZ() == comp.getZ();
	}


}
