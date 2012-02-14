package se.fearlessgames.fear;

import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

import static org.junit.Assert.assertEquals;

public class TransformationTest {

	private static final double EPSILON = 1e-5;

	@Test
	public void identity() throws Exception {
		Transformation transformation = new Transformation(Vector3.ZERO, Quaternion.IDENTITY, Vector3.ONE);
		Matrix4 identity = transformation.asMatrix();
		assertEquals(Matrix4.IDENTITY, identity);
	}

	@Test
	public void translate() throws Exception {
		Transformation transformation = new Transformation(new Vector3(30, 20, 10), Quaternion.IDENTITY, Vector3.ONE);

		Matrix4 matrix4 = transformation.asMatrix();

		assertEquals(1, matrix4.getValue(0, 0), EPSILON);
		assertEquals(0, matrix4.getValue(0, 1), EPSILON);
		assertEquals(0, matrix4.getValue(0, 2), EPSILON);
		assertEquals(30, matrix4.getValue(0, 3), EPSILON);

		assertEquals(0, matrix4.getValue(1, 0), EPSILON);
		assertEquals(1, matrix4.getValue(1, 1), EPSILON);
		assertEquals(0, matrix4.getValue(1, 2), EPSILON);
		assertEquals(20, matrix4.getValue(1, 3), EPSILON);

		assertEquals(0, matrix4.getValue(2, 0), EPSILON);
		assertEquals(0, matrix4.getValue(2, 1), EPSILON);
		assertEquals(1, matrix4.getValue(2, 2), EPSILON);
		assertEquals(10, matrix4.getValue(2, 3), EPSILON);

		assertEquals(0, matrix4.getValue(3, 0), EPSILON);
		assertEquals(0, matrix4.getValue(3, 1), EPSILON);
		assertEquals(0, matrix4.getValue(3, 2), EPSILON);
		assertEquals(1, matrix4.getValue(3, 3), EPSILON);
	}

	@Test
	public void rotateAroundZ() throws Exception {
		Transformation transformation = new Transformation(new Vector3(0, 0, 0), Quaternion.fromEulerAngles(0, Math.PI / 2, 0), Vector3.ONE);
		Matrix4 matrix4 = transformation.asMatrix();

		assertEquals(0, matrix4.getValue(2, 0), EPSILON);
		assertEquals(0, matrix4.getValue(2, 1), EPSILON);
		assertEquals(1, matrix4.getValue(2, 2), EPSILON);
		assertEquals(0, matrix4.getValue(2, 3), EPSILON);

		assertEquals(0, matrix4.getValue(3, 0), EPSILON);
		assertEquals(0, matrix4.getValue(3, 1), EPSILON);
		assertEquals(0, matrix4.getValue(3, 2), EPSILON);
		assertEquals(1, matrix4.getValue(3, 3), EPSILON);

		assertEquals(0, matrix4.getValue(0, 0), EPSILON);
		assertEquals(1, matrix4.getValue(0, 1), EPSILON);
		assertEquals(0, matrix4.getValue(0, 2), EPSILON);
		assertEquals(0, matrix4.getValue(0, 3), EPSILON);

		assertEquals(-1, matrix4.getValue(1, 0), EPSILON);
		assertEquals(0, matrix4.getValue(1, 1), EPSILON);
		assertEquals(0, matrix4.getValue(1, 2), EPSILON);
		assertEquals(0, matrix4.getValue(1, 3), EPSILON);
	}
}
