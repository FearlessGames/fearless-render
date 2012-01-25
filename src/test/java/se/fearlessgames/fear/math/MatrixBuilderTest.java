package se.fearlessgames.fear.math;

import org.junit.Test;

import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;

public class MatrixBuilderTest {

	private static final float EPSILON = 1e-6f;

	@Test
	public void identity() throws Exception {
		MatrixBuilder builder = new MatrixBuilder();
		FloatBuffer buffer = builder.asFloatBuffer();
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
}
