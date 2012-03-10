package se.fearlessgames.fear.math;

import org.junit.Ignore;
import org.junit.Test;
import se.fearlessgames.fear.Transformation;

import static org.junit.Assert.assertEquals;

public class TransformationTest {
	@Test
	public void testVectorTransformFromOrigo() {
		Vector3 transformVector = new Vector3(1, 2, 3);
		Vector3 originalVector = new Vector3(0, 0, 0);
		Vector3 vector = new Transformation(transformVector, Quaternion.IDENTITY, Vector3.ONE).transform(originalVector);

		assertEquals(transformVector, vector);

	}

	@Test
	public void testVectorTransformFromOfsett() {
		Vector3 transformVector = new Vector3(1, 2, 3);
		Vector3 originalVector = new Vector3(1, 1, 1);
		Vector3 vector = new Transformation(transformVector, Quaternion.IDENTITY, Vector3.ONE).transform(originalVector);

		assertEquals(new Vector3(2, 3, 4), vector);
	}

	@Test
	public void testZeroTranform() {

		Vector3 originalVector = new Vector3(3, 4, 5);
		Vector3 vector = new Transformation(Vector3.ZERO, Quaternion.IDENTITY, Vector3.ONE).transform(originalVector);

		assertEquals(originalVector, vector);
	}

	@Test
	@Ignore
	public void testRotateTransform() {
		Vector3 originalVector = new Vector3(0, 1, 0);
		Vector3 vector = new Transformation(Vector3.ZERO, Quaternion.fromEulerAngles(0, 90, 0), Vector3.ONE).transform(originalVector);

		assertEquals(new Vector3(0, 0, 1), vector);
	}


	/*


	 @Test
	 public void testRotateTransform() {

		 Vector3 originalVertex = new Vector3(0, 0, -1);
		 Vector3 vector = transform(originalVertex, Quaternion.fromEulerAngles(90, 0, 0));

		 assertEquals(new Vector3(0, 0, 1), vector);
	 }
 */
}
