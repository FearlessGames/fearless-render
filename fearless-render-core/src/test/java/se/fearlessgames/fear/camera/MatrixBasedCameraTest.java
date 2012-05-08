package se.fearlessgames.fear.camera;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

import static org.junit.Assert.assertEquals;

public class MatrixBasedCameraTest {

	private MatrixBasedCamera camera;
	private CameraPerspective perspective;

	@Before
	public void setUp() throws Exception {
		perspective = new CameraPerspective(45, 800.0f/600, 0.1f, 1000f);
		camera = new MatrixBasedCamera(perspective);
	}

	@Test
	public void noOp() throws Exception {
		Vector3 location = camera.getLocation();
		assertEquals(Vector3.ZERO, location);
		Matrix4 viewMatrix = camera.getViewMatrix();
		assertEquals(Matrix4.IDENTITY, viewMatrix);
	}

	@Test
	public void simpleTranslation() throws Exception {
		camera.translate(new Vector3(1, 0, 0));
		Matrix4 viewMatrix = camera.getViewMatrix();
		Matrix4 expected = new Matrix4(	1, 0, 0, -1,
										0, 1, 0, 0,
										0, 0, 1, 0,
										0, 0, 0, 1);
		assertEquals(expected, viewMatrix);
	}

	@Test
	public void compoundTranslation() throws Exception {
		camera.translate(new Vector3(1, 0, 0));
		camera.translate(new Vector3(1, 0, 0));
		Matrix4 viewMatrix = camera.getViewMatrix();
		Matrix4 expected = new Matrix4(	1, 0, 0, -2,
										0, 1, 0, 0,
										0, 0, 1, 0,
										0, 0, 0, 1);
		assertEquals(expected, viewMatrix);
	}

	@Test
	public void rotateBackAndForth() throws Exception {
		Quaternion orientation = Quaternion.fromEulerAngles(Math.PI / 4, 0, Math.PI / 6);
		camera.setOrientation(orientation);

		camera.setOrientation(Quaternion.IDENTITY);
		assertEquals(Matrix4.IDENTITY, camera.getViewMatrix());
	}

	@Test
	public void simpleRotate() throws Exception {
		Quaternion orientation = Quaternion.fromAngleAxis(Math.PI/2, Vector3.UNIT_Y);

		camera.setOrientation(orientation);
		Matrix4 viewMatrix = camera.getViewMatrix();
		Matrix4 expected = new Matrix4(	0, 0, -1, 0,
										0, 1, 0, 0,
										1, 0, 0, 0,
										0, 0, 0, 1);
		assertEquals(expected, viewMatrix);
	}

	@Test
	public void rotateAndTranslate() throws Exception {
		Quaternion orientation = Quaternion.fromAngleAxis(Math.PI/2, Vector3.UNIT_Y);
		camera.setOrientation(orientation);
		camera.translate(new Vector3(2, 0, 0));

		Matrix4 viewMatrix = camera.getViewMatrix();
		Matrix4 expected = new Matrix4(	0, 0, -1, -2,
										0, 1, 0, 0,
										1, 0, 0, 0,
										0, 0, 0, 1);
		assertEquals(expected, viewMatrix);
	}
}
