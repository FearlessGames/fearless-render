package se.fearlessgames.fear.mesh;

import org.junit.Test;
import se.fearlessgames.fear.shape.SphereFactory;

import static org.junit.Assert.assertEquals;

public class BoundingSphereTest {

	@Test
	public void testCreateBoundingSphere() {
		SphereFactory sphereFactory = new SphereFactory(100, 100, 1, SphereFactory.TextureMode.PROJECTED);
		MeshData meshData = sphereFactory.create();
		BoundingSphere boundingSphere = new BoundingSphere(meshData);
		assertEquals(0.0, boundingSphere.getCenter().getX(), 0.01);
		assertEquals(0.0, boundingSphere.getCenter().getY(), 0.01);
		assertEquals(0.0, boundingSphere.getCenter().getZ(), 0.01);
		assertEquals(1, boundingSphere.getRadius(), 0.01);
	}
}
