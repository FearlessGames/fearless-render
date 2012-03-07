package se.fearlessgames.fear.math;


public class Vector2 {
	public static final Vector2 ZERO = new Vector2(0, 0);
	public static final Vector2 ONE = new Vector2(1, 1);
	public static final Vector2 NEG_ONE = new Vector2(-1, -1);
	public static final Vector2 UNIT_X = new Vector2(1, 0);
	public static final Vector2 NEG_UNIT_X = new Vector2(-1, 0);
	public static final Vector2 UNIT_Y = new Vector2(0, 1);
	public static final Vector2 NEG_UNIT_Y = new Vector2(0, -1);

	private final double x;
	private final double y;

	public Vector2(final Vector2 src) {
		this(src.getX(), src.getY());
	}

	public Vector2(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double[] toArray(double[] store) {
		if (store == null) {
			store = new double[2];
		}
		// do last first to ensure size is correct before any edits occur.
		store[1] = getY();
		store[0] = getX();
		return store;
	}

	public Vector2 add(final double x, final double y) {
		return new Vector2(getX() + x, getY() + y);
	}

	public Vector2 add(final Vector2 source) {
		return add(source.getX(), source.getY());
	}

	public Vector2 subtract(final double x, final double y) {
		return new Vector2(getX() - x, getY() - y);
	}

	public Vector2 subtract(final Vector2 source) {
		return subtract(source.getX(), source.getY());
	}

	public Vector2 multiply(final double scalar) {
		return new Vector2(getX() * scalar, getY() * scalar);
	}

	public Vector2 multiply(final Vector2 scale) {
		return new Vector2(getX() * scale.getX(), getY() * scale.getY());
	}

	public Vector2 divide(final double scalar) {
		return new Vector2(getX() / scalar, getY() / scalar);
	}

	public Vector2 divide(final Vector2 scale) {
		return new Vector2(getX() / scale.getX(), getY() / scale.getY());
	}

	public Vector2 scaleAdd(final double scale, final Vector2 add) {
		return new Vector2(x * scale + add.getX(), y * scale + add.getY());
	}

	public Vector2 negate() {
		return multiply(-1);
	}

	public Vector2 normalize() {
		final double lengthSq = lengthSquared();
		if (Math.abs(lengthSq) > MathUtils.EPSILON) {
			return multiply(MathUtils.inverseSqrt(lengthSq));
		}

		return new Vector2(this);
	}

	public Vector2 rotateAroundOrigin(double angle, final boolean clockwise) {
		if (clockwise) {
			angle = -angle;
		}
		final double newX = MathUtils.cos(angle) * getX() - MathUtils.sin(angle) * getY();
		final double newY = MathUtils.sin(angle) * getX() + MathUtils.cos(angle) * getY();
		return new Vector2(newX, newY);
	}

	public Vector2 lerp(final Vector2 endVec, final double scalar) {
		final double x = (1.0 - scalar) * getX() + scalar * endVec.getX();
		final double y = (1.0 - scalar) * getY() + scalar * endVec.getY();
		return new Vector2(x, y);
	}

	public static Vector2 lerp(final Vector2 beginVec, final Vector2 endVec, final double scalar) {
		final double x = (1.0 - scalar) * beginVec.getX() + scalar * endVec.getX();
		final double y = (1.0 - scalar) * beginVec.getY() + scalar * endVec.getY();
		return new Vector2(x, y);
	}

	public double length() {
		return MathUtils.sqrt(lengthSquared());
	}

	public double lengthSquared() {
		return getX() * getX() + getY() * getY();
	}

	public double distanceSquared(final double x, final double y) {
		final double dx = getX() - x;
		final double dy = getY() - y;
		return dx * dx + dy * dy;
	}

	/**
	 * @param destination the target destination
	 * @return the squared distance between the point described by this vector and the given destination point. When
	 *         comparing the relative distance between two points it is usually sufficient to compare the squared
	 *         distances, thus avoiding an expensive square root operation.
	 */
	public double distanceSquared(final Vector2 destination) {
		return distanceSquared(destination.getX(), destination.getY());
	}

	public double distance(final double x, final double y) {
		return MathUtils.sqrt(distanceSquared(x, y));
	}

	public double distance(final Vector2 destination) {
		return MathUtils.sqrt(distanceSquared(destination));
	}

	public double dot(final double x, final double y) {
		return getX() * x + getY() * y;
	}

	public double dot(final Vector2 vec) {
		return dot(vec.getX(), vec.getY());
	}

	/**
	 * @return the angle - in radians [-pi, pi) - represented by this Vector2 as expressed by a conversion from
	 *         rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>) to polar coordinates
	 *         (r,&nbsp;<i>theta</i>).
	 */
	public double getPolarAngle() {
		return -Math.atan2(getY(), getX());
	}

	/**
	 * @param otherVector the "destination" unit vector
	 * @return the angle (in radians) required to rotate a ray represented by this vector to lie co-linear to a ray
	 *         described by the given vector. It is assumed that both this vector and the given vector are unit vectors
	 *         (normalized).
	 * @throws NullPointerException if otherVector is null.
	 */
	public double angleBetween(final Vector2 otherVector) {
		return Math.atan2(otherVector.getY(), otherVector.getX()) - Math.atan2(getY(), getX());
	}

	/**
	 * @param otherVector a unit vector to find the angle against
	 * @return the minimum angle (in radians) between two vectors. It is assumed that both this vector and the given
	 *         vector are unit vectors (normalized).
	 * @throws NullPointerException if otherVector is null.
	 */
	public double smallestAngleBetween(final Vector2 otherVector) {
		final double dotProduct = dot(otherVector);
		return MathUtils.acos(dotProduct);
	}


	public static boolean isValid(final Vector2 vector) {
		if (vector == null) {
			return false;
		}
		if (Double.isNaN(vector.getX()) || Double.isNaN(vector.getY())) {
			return false;
		}
		if (Double.isInfinite(vector.getX()) || Double.isInfinite(vector.getY())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Vector2{" +
				"x=" + x +
				", y=" + y +
				'}';
	}

	/**
	 * @return returns a unique code for this vector object based on its values. If two vectors are numerically equal,
	 *         they will return the same hash code value.
	 */
	@Override
	public int hashCode() {
		int result = 17;

		final long x = Double.doubleToLongBits(getX());
		result += 31 * result + (int) (x ^ (x >>> 32));

		final long y = Double.doubleToLongBits(getY());
		result += 31 * result + (int) (y ^ (y >>> 32));

		return result;
	}

	/**
	 * @param o the object to compare for equality
	 * @return true if this vector and the provided vector have the same x and y values.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Vector2)) {
			return false;
		}
		final Vector2 comp = (Vector2) o;
		return getX() == comp.getX() && getY() == comp.getY();
	}
}
