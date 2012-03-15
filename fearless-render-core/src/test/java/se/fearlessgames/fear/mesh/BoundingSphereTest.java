package se.fearlessgames.fear.mesh;

import org.junit.Test;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.shape.SphereFactory;

import static org.junit.Assert.assertEquals;

public class BoundingSphereTest {
	private static final double EPSILON = 1e-6;

	@Test
	public void testCreateBoundingSphere() {
		SphereFactory sphereFactory = new SphereFactory(100, 100, 1, SphereFactory.TextureMode.PROJECTED);
		MeshData meshData = sphereFactory.create();
		BoundingSphere boundingSphere = BoundingSphere.create(meshData);
		assertEquals(0.0, boundingSphere.getCenter().getX(), 0.01);
		assertEquals(0.0, boundingSphere.getCenter().getY(), 0.01);
		assertEquals(0.0, boundingSphere.getCenter().getZ(), 0.01);
		assertEquals(1, boundingSphere.getRadius(), 0.01);
	}

	@Test
	public void testMergeBoundingSpheresInside() throws Exception {
		BoundingSphere a = new BoundingSphere(2.0, new Vector3(0, 0, 0));
		BoundingSphere b = new BoundingSphere(3.0, new Vector3(0, 0, 0));
		BoundingSphere merged = a.merge(b);
		assertEquals(new BoundingSphere(3.0, new Vector3(0, 0, 0)), merged);
		assertEquals(3.0, merged.getRadius(), EPSILON);
		assertEquals(0, merged.getCenter().distance(new Vector3(0, 0, 0)), EPSILON);
	}

	@Test
	public void testMergeBoundingSpheresAdjacent() throws Exception {
		BoundingSphere a = new BoundingSphere(2.0, new Vector3(0, 0, 0));
		BoundingSphere b = new BoundingSphere(2.0, new Vector3(4.0, 0, 0));
		BoundingSphere merged = a.merge(b);
		assertEquals(new BoundingSphere(4.0, new Vector3(2, 0, 0)), merged);
		assertEquals(4.0, merged.getRadius(), EPSILON);
		assertEquals(0, merged.getCenter().distance(new Vector3(2, 0, 0)), EPSILON);
	}

	@Test
	public void testMergeBoundingSpheresIntersecting() throws Exception {
		BoundingSphere a = new BoundingSphere(2.0, new Vector3(2.0, 0, 0));
		BoundingSphere b = new BoundingSphere(3.0, new Vector3(4.0, 0, 0));
		BoundingSphere merged = a.merge(b);
		assertEquals(new BoundingSphere(3.5, new Vector3(3.5, 0, 0)), merged);
	}
}
