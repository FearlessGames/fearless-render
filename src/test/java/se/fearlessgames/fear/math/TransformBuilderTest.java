package se.fearlessgames.fear.math;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;

public class TransformBuilderTest {

	private static final float EPSILON = 1e-6f;

	@Test
	public void identity() throws Exception {
		TransformBuilder builder = new TransformBuilder();
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

	@Test
	public void translate() throws Exception {
		TransformBuilder builder = new TransformBuilder();
		builder.translate(new Vector3D(10, 20, 30));
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

		assertEquals(10.0f, buffer.get(12), EPSILON);
		assertEquals(20.0f, buffer.get(13), EPSILON);
		assertEquals(30.0f, buffer.get(14), EPSILON);
		assertEquals(1.0f, buffer.get(15), EPSILON);
	}

	@Test
	public void rotateAroundZ() throws Exception {
		TransformBuilder builder = new TransformBuilder();
		builder.rotate(new Rotation(Vector3D.PLUS_K, Math.PI));

		FloatBuffer buffer = builder.asFloatBuffer();
		assertEquals(-1.0f, buffer.get(0), EPSILON);
		assertEquals(0.0f, buffer.get(1), EPSILON);
		assertEquals(0.0f, buffer.get(2), EPSILON);
		assertEquals(0.0f, buffer.get(3), EPSILON);

		assertEquals(0.0f, buffer.get(4), EPSILON);
		assertEquals(-1.0f, buffer.get(5), EPSILON);
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
	public void rotateAroundY() throws Exception {
		TransformBuilder builder = new TransformBuilder();
		builder.rotate(new Rotation(RotationOrder.XYZ, 0, Math.PI/2, 0));

		FloatBuffer buffer = builder.asFloatBuffer();

		debugPrintAsMatrix(buffer);

		assertEquals(0.0f, buffer.get(0), EPSILON);
		assertEquals(0.0f, buffer.get(1), EPSILON);
		assertEquals(-1.0f, buffer.get(2), EPSILON);
		assertEquals(0.0f, buffer.get(3), EPSILON);

		assertEquals(0.0f, buffer.get(4), EPSILON);
		assertEquals(1.0f, buffer.get(5), EPSILON);
		assertEquals(0.0f, buffer.get(6), EPSILON);
		assertEquals(0.0f, buffer.get(7), EPSILON);

		assertEquals(1.0f, buffer.get(8), EPSILON);
		assertEquals(0.0f, buffer.get(9), EPSILON);
		assertEquals(0.0f, buffer.get(10), EPSILON);
		assertEquals(0.0f, buffer.get(11), EPSILON);

		assertEquals(0.0f, buffer.get(12), EPSILON);
		assertEquals(0.0f, buffer.get(13), EPSILON);
		assertEquals(0.0f, buffer.get(14), EPSILON);
		assertEquals(1.0f, buffer.get(15), EPSILON);
	}


	private void debugPrintAsMatrix(FloatBuffer buffer) {
		for (int i = 0; i < 16; i++) {
			float val = buffer.get(i);
			if (Math.abs(val) < 1e-3) {
				System.out.print("0.0\t");
			} else {
				System.out.print(val + "\t");
			}
			if ((i + 1) % 4 == 0) {
				System.out.print("\n");
			}
		}
	}
}
