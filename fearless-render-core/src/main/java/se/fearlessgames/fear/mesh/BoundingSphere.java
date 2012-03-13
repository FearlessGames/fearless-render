package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class BoundingSphere {
	private final static double radiusEpsilon = 1.00001d;
	private final double radius;
	private final Vector3 center;

	public BoundingSphere(MeshData data) {
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
		this.center = center;
		this.radius = (Math.sqrt(maxRadiusSqr) + radiusEpsilon - 1f);
	}

	public double getRadius() {
		return radius;
	}

	public Vector3 getCenter() {
		return center;
	}
}
