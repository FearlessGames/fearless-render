package se.fearlessgames.fear.camera;

import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;

import static org.junit.Assert.assertEquals;

public class CameraPerspectiveTest {

	public static final double EPSILON = 1e-6;

	@Test
	public void matrix() throws Exception {
		CameraPerspective perspective = new CameraPerspective(45f, 4.0f/3.0f, 1f, 1000f);
		Matrix4 matrix = perspective.getMatrix();
		System.out.println(matrix);
		assertEquals(1.81066017f, matrix.getValue(0, 0), EPSILON);
		assertEquals(0f, matrix.getValue(0, 1), EPSILON);
		assertEquals(0f, matrix.getValue(0, 2), EPSILON);
		assertEquals(0f, matrix.getValue(0, 3), EPSILON);

		assertEquals(0f, matrix.getValue(1, 0), EPSILON);
		assertEquals(2.41421356f, matrix.getValue(1, 1), EPSILON);
		assertEquals(0f, matrix.getValue(1, 2), EPSILON);
		assertEquals(0f, matrix.getValue(1, 3), EPSILON);

		assertEquals(0f, matrix.getValue(2, 0), EPSILON);
		assertEquals(0f, matrix.getValue(2, 1), EPSILON);
		assertEquals(1.0020020f, matrix.getValue(2, 2), EPSILON);
		assertEquals(1f, matrix.getValue(2, 3), EPSILON);

		assertEquals(0f, matrix.getValue(3, 0), EPSILON);
		assertEquals(0f, matrix.getValue(3, 1), EPSILON);
		assertEquals(-2.0020020f, matrix.getValue(3, 2), EPSILON);
		assertEquals(0f, matrix.getValue(3, 3), EPSILON);
	}
}
