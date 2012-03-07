package se.fearlessgames.fear.math;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuaternionTest {
	private static final double EPSILON = 1e-10;

	@Test
	public void testRotationOfUnitVectorX() {
		Vector3 ardor = new Vector3(1, 0, 0);
		Vector3 spacedVector = new Vector3(1, 0, 0);

		assertSameVectors(ardor, spacedVector);

		Quaternion ardorRot = new Quaternion(0, 0, 0.70710678118654752440084436210485, 0.70710678118654752440084436210485);

		Quaternion spacedRot = new Quaternion(0, 0, 0.70710678118654752440084436210485, 0.70710678118654752440084436210485);
		assertSameRotations(ardorRot, spacedRot);

		Vector3 ardorResult = ardorRot.applyTo(ardor);

		Vector3 spacedResult = spacedRot.applyTo(spacedVector);
		assertSameVectors(ardorResult, spacedResult);

		assertEquals(0.0, spacedResult.getX(), EPSILON);
		assertEquals(1.0, spacedResult.getY(), EPSILON);
		assertEquals(0.0, spacedResult.getZ(), EPSILON);
	}


	@Test
	public void testRotationOfVector() {
		Vector3 ardor = new Vector3(30, 40, 50);
		Vector3 spacedVector = new Vector3(30, 40, 50);

		assertSameVectors(ardor, spacedVector);

		Quaternion ardorRot = new Quaternion(3, 4, 5, 1).normalize();


		Quaternion spacedRot = new Quaternion(3, 4, 5, 1, true);
		assertSameRotations(ardorRot, spacedRot);

		Vector3 ardorResult = ardorRot.applyTo(ardor);

		Vector3 spacedResult = spacedRot.applyTo(spacedVector);
		assertSameVectors(ardorResult, spacedResult);
	}

	@Test
	public void testRotationOfVector2() {
		Vector3 ardor = new Vector3(0, 0, 9);
		Vector3 spacedVector = new Vector3(0, 0, 9);

		assertSameVectors(ardor, spacedVector);

		Quaternion ardorRot = new Quaternion(0.0, 0.19866933079506122, 0.0, 0.9800665778412416);

		Quaternion spacedRot = new Quaternion(0.0, 0.19866933079506122, 0.0, 0.9800665778412416);
		assertSameRotations(ardorRot, spacedRot);

		Vector3 ardorResult = ardorRot.applyTo(ardor);

		Vector3 spacedResult = spacedRot.applyTo(spacedVector);
		assertSameVectors(ardorResult, spacedResult);
	}


	@Test
	public void testChainingRotations() {
		Vector3 ardor = new Vector3(30, 40, 50);
		Vector3 spacedVector = new Vector3(30, 40, 50);

		assertSameVectors(ardor, spacedVector);

		Quaternion ardorRot1 = new Quaternion(3, 4, 5, 1).normalize();


		Quaternion ardorRot2 = new Quaternion(25, 7, 6, 8).normalize();

		Quaternion ardorChained = ardorRot1.multiply(ardorRot2);

		Quaternion spacedRot1 = new Quaternion(3, 4, 5, 1, true);
		Quaternion spacedRot2 = new Quaternion(25, 7, 6, 8, true);

		Quaternion spacedChained = spacedRot1.applyTo(spacedRot2);

		assertSameRotations(ardorChained, spacedChained);
	}

	@Test
	public void testChainingInverse() {
		Quaternion ardorRot1 = new Quaternion(3, 4, 5, 1).normalize();


		Quaternion ardorRot2 = ardorRot1.invert();

		Quaternion ardorChained = ardorRot1.multiply(ardorRot2);

		Quaternion spacedRot1 = new Quaternion(3, 4, 5, 1, true);


		Quaternion spacedChained = spacedRot1.applyInverseTo(spacedRot1.applyTo(Quaternion.IDENTITY));

		assertSameRotations(ardorChained, spacedChained);
	}


	@Test
	public void fromEuler() {
		Quaternion ardorRot = Quaternion.fromEulerAngles(radFromDeg(50), radFromDeg(30), radFromDeg(20));

		assertEquals(0.259, ardorRot.getX(), 1e-3);

		Quaternion spacedRotation = fromEulerAngles(radFromDeg(50), radFromDeg(30), radFromDeg(20));
		assertEquals(0.259, spacedRotation.getX(), 1e-3);

//		Quaternion spacedRotation2 = new Quaternion(QuaternionOrder.ZYX, radFromDeg(50),radFromDeg(30),radFromDeg(20));
//		for (QuaternionOrder spacedRotationOrder : QuaternionOrder.values()) {
//			Quaternion spacedRot = new Quaternion(spacedRotationOrder, radFromDeg(50),radFromDeg(30),radFromDeg(20));
//			System.out.println(spacedRotationOrder + ": " + spacedRot);
//		}
//		assertSameRotations(ardorRot, spacedRotation2);

		assertSameRotations(ardorRot, spacedRotation);
	}

	public static Quaternion fromEulerAngles(double heading, double attitude, double bank) {
		double angle = heading * 0.5;
		final double sinHeading = MathUtils.sin(angle);
		final double cosHeading = MathUtils.cos(angle);
		angle = attitude * 0.5;
		final double sinAttitude = MathUtils.sin(angle);
		final double cosAttitude = MathUtils.cos(angle);
		angle = bank * 0.5;
		final double sinBank = MathUtils.sin(angle);
		final double cosBank = MathUtils.cos(angle);

		// variables used to reduce multiplication calls.
		final double cosHeadingXcosAttitude = cosHeading * cosAttitude;
		final double sinHeadingXsinAttitude = sinHeading * sinAttitude;
		final double cosHeadingXsinAttitude = cosHeading * sinAttitude;
		final double sinHeadingXcosAttitude = sinHeading * cosAttitude;

		final double w = (cosHeadingXcosAttitude * cosBank - sinHeadingXsinAttitude * sinBank);
		final double x = (cosHeadingXcosAttitude * sinBank + sinHeadingXsinAttitude * cosBank);
		final double y = (sinHeadingXcosAttitude * cosBank + cosHeadingXsinAttitude * sinBank);
		final double z = (cosHeadingXsinAttitude * cosBank - sinHeadingXcosAttitude * sinBank);

		return new Quaternion(x, y, z, w);
	}


	private double radFromDeg(int deg) {
		return deg * Math.PI / 180;
	}

	private void assertSameRotations(Quaternion ardorRot, Quaternion spacedRot) {
		assertEquals("x", ardorRot.getX(), spacedRot.getX(), EPSILON);
		assertEquals("y", ardorRot.getY(), spacedRot.getY(), EPSILON);
		assertEquals("z", ardorRot.getZ(), spacedRot.getZ(), EPSILON);
		assertEquals("w", ardorRot.getW(), spacedRot.getW(), EPSILON);

		Matrix3 matrix3 = ardorRot.toRotationMatrix3();

		double[][] matrix = spacedRot.getMatrix();
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				assertEquals("Failed @ " + row + ", " + column, matrix3.getValue(row, column), matrix[column][row], EPSILON);
			}
		}
	}

	private void assertSameVectors(Vector3 ardor, Vector3 spacedVector) {
		assertEquals(ardor.getX(), spacedVector.getX(), EPSILON);
		assertEquals(ardor.getY(), spacedVector.getY(), EPSILON);
		assertEquals(ardor.getZ(), spacedVector.getZ(), EPSILON);
	}
}
