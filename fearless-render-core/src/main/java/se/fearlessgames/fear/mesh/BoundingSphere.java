package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class BoundingSphere {
	private final static double radiusEpsilon = 1.00001d;
	private final double radius;
	private final Vector3 center;

	public BoundingSphere(double radius, Vector3 center) {
		this.radius = radius;
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public Vector3 getCenter() {
		return center;
	}

    // TODO: Calculate bounding sphere better than just using gravitational center
	public static BoundingSphere create(MeshData data) {
		data = data.duplicate();

		List<Vector3> vertexes = new ArrayList<Vector3>();

		while (data.getVertexBuffer().hasRemaining()) {
			Vector3 vertex = new Vector3(data.getVertexBuffer());
			vertexes.add(vertex);
		}

		Vector3 center = vertexes.get(0);

		for (Vector3 point : vertexes) {
			center = center.add(point);
		}

		final double quantity = 1.0 / vertexes.size();
		center = center.multiply(quantity);

		double maxRadiusSqr = 0;
		for (Vector3 point : vertexes) {
			final Vector3 diff = point.subtract(center);
			final double radiusSqr = diff.lengthSquared();
			if (radiusSqr > maxRadiusSqr) {
				maxRadiusSqr = radiusSqr;
			}
		}
		return new BoundingSphere(Math.sqrt(maxRadiusSqr) + radiusEpsilon - 1f, center);
	}

	/**
	 * @param other
	 * @return the smallest bounding sphere that contains both this sphere and other.
	 */
	public BoundingSphere merge(BoundingSphere other) {
		Vector3 centerDiff = other.center.subtract(center);

		double centerDistance = centerDiff.length();
		double newRadius = (centerDistance + radius + other.radius) * 0.5;
		if (centerDistance >= radiusEpsilon) {
			double f = (centerDistance + other.radius - radius) / (2*centerDistance);
			if (0 <= f && f <= 1) {
				Vector3 newCenter = center.add(centerDiff.multiply(f));
				return new BoundingSphere(newRadius, newCenter);
			}
		}
		if (radius > other.radius) {
			return this;
		} else {
			return other;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BoundingSphere that = (BoundingSphere) o;

		if (Double.compare(that.radius, radius) != 0) return false;
		if (center != null ? !center.equals(that.center) : that.center != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = radius != +0.0d ? Double.doubleToLongBits(radius) : 0L;
		result = (int) (temp ^ (temp >>> 32));
		result = 31 * result + (center != null ? center.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "BoundingSphere{" +
				"radius=" + radius +
				", center=" + center +
				'}';
	}
}
