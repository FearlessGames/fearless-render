package se.fearlessgames.fear.math;

import org.junit.Test;

import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;

public class GlMatrixBuilderTest {

	private static final float EPSILON = 1e-6f;

	@Test
	public void identity() throws Exception {
		FloatBuffer buffer = GlMatrixBuilder.convert(Matrix4.IDENTITY);
		assertEquals(1.0f, buffer.get(0), EPSILON);
		assertEquals(0.0f, buffer.get(1), EPSILON);
		assertEquals(0.0f, buffer.get(2), EPSILON);
		assertEquals(0.0f, buffer.get(3), EPSILON);

		assertEquals(0.0f, buffer.get(4), EPSILON);
		assertEquals(1.0f, buffer.get(5), EPSILON);
		assertEquals(0.0f, buffer.get(6), EPSILON);
		assertEquals(0.0f, buffer.get(7), EPSILON);

		assertEquals(0.0f, buffer.get(8), EPSILON);
		assertEquals(0.0f, buffer.get(9), EPSILON);
		assertEquals(1.0f, buffer.get(10), EPSILON);
		assertEquals(0.0f, buffer.get(11), EPSILON);

		assertEquals(0.0f, buffer.get(12), EPSILON);
		assertEquals(0.0f, buffer.get(13), EPSILON);
		assertEquals(0.0f, buffer.get(14), EPSILON);
		assertEquals(1.0f, buffer.get(15), EPSILON);
	}

	@Test
	public void translate() throws Exception {
		Matrix4 matrix = new Matrix4(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
		FloatBuffer buffer = GlMatrixBuilder.convert(matrix);
		assertEquals(0.0f, buffer.get(0), EPSILON);
		assertEquals(4.0f, buffer.get(1), EPSILON);
		assertEquals(8.0f, buffer.get(2), EPSILON);
		assertEquals(12.0f, buffer.get(3), EPSILON);

		assertEquals(1.0f, buffer.get(4), EPSILON);
		assertEquals(5.0f, buffer.get(5), EPSILON);
		assertEquals(9.0f, buffer.get(6), EPSILON);
		assertEquals(13.0f, buffer.get(7), EPSILON);

		assertEquals(2.0f, buffer.get(8), EPSILON);
		assertEquals(6.0f, buffer.get(9), EPSILON);
		assertEquals(10.0f, buffer.get(10), EPSILON);
		assertEquals(14.0f, buffer.get(11), EPSILON);

		assertEquals(3.0f, buffer.get(12), EPSILON);
		assertEquals(7.0f, buffer.get(13), EPSILON);
		assertEquals(11.0f, buffer.get(14), EPSILON);
		assertEquals(15.0f, buffer.get(15), EPSILON);
	}
}
