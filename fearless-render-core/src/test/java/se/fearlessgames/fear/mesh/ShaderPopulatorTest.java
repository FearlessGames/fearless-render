package se.fearlessgames.fear.mesh;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.common.mock.MockUtil;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.camera.MatrixBasedCamera;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.shader.ShaderProgram;
import se.fearlessgames.fear.shader.ShaderUniform;
import se.mockachino.MockSettings;
import se.mockachino.matchers.matcher.ArgumentCatcher;

import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.Matchers.match;
import static se.mockachino.matchers.MatchersBase.mAny;
import static se.mockachino.matchers.MatchersBase.matchEq;

public class ShaderPopulatorTest {

	private ShaderPopulator shaderPopulator;
	private static final double EPSILON = 1e-6;

	@Before
	public void setUp() throws Exception {
		shaderPopulator = new ShaderPopulator();
	}

	@Test
	public void projectionMatrix() throws Exception {

		Camera camera = new MatrixBasedCamera(new CameraPerspective(45, (4.0f/3.0f), 1, 1000));
		ShaderProgram shaderProgram = MockUtil.deepMock(ShaderProgram.class);
		shaderPopulator.pushTransforms(Matrix4.IDENTITY, shaderProgram, camera);


		ArgumentCatcher<FloatBuffer> catcher = ArgumentCatcher.create(mAny(FloatBuffer.class));
		verifyOnce().on(shaderProgram.uniform(ShaderUniform.PROJECTION_MATRIX)).setMatrix4(match(catcher));
		FloatBuffer matrix = catcher.getValue();

		assertEquals(1.81066017f, matrix.get(0), EPSILON);
		assertEquals(0f, matrix.get(1), EPSILON);
		assertEquals(0f, matrix.get(2), EPSILON);
		assertEquals(0f, matrix.get(3), EPSILON);

		assertEquals(0f, matrix.get(4), EPSILON);
		assertEquals(2.41421356f, matrix.get(5), EPSILON);
		assertEquals(0f, matrix.get(6), EPSILON);
		assertEquals(0f, matrix.get(7), EPSILON);

		assertEquals(0f, matrix.get(8), EPSILON);
		assertEquals(0f, matrix.get(9), EPSILON);
		assertEquals(1.0020020f, matrix.get(10), EPSILON);
		assertEquals(1f, matrix.get(11), EPSILON);

		assertEquals(0f, matrix.get(12), EPSILON);
		assertEquals(0f, matrix.get(13), EPSILON);
		assertEquals(-2.0020020f, matrix.get(14), EPSILON);
		assertEquals(0f, matrix.get(15), EPSILON);
	}

	@Test
	public void simpleModelMatrix() throws Exception {
		Camera camera = MockUtil.deepMock(Camera.class);
		ShaderProgram shaderProgram = MockUtil.deepMock(ShaderProgram.class);
		Matrix4 model = new Matrix4(1, 0, 0, 10,
									0, 1, 0, -20,
									0, 0, 1, 30,
									0, 0, 0, 1);

		shaderPopulator.pushTransforms(model, shaderProgram, camera);

		ArgumentCatcher<FloatBuffer> catcher = ArgumentCatcher.create(mAny(FloatBuffer.class));
		verifyOnce().on(shaderProgram.uniform(ShaderUniform.MODEL_MATRIX)).setMatrix4(match(catcher));
		FloatBuffer matrix = catcher.getValue();

		assertEquals(1f, matrix.get(0), EPSILON);
		assertEquals(0f, matrix.get(1), EPSILON);
		assertEquals(0f, matrix.get(2), EPSILON);
		assertEquals(10f, matrix.get(3), EPSILON);

		assertEquals(0f, matrix.get(4), EPSILON);
		assertEquals(1f, matrix.get(5), EPSILON);
		assertEquals(0f, matrix.get(6), EPSILON);
		assertEquals(-20f, matrix.get(7), EPSILON);

		assertEquals(0f, matrix.get(8), EPSILON);
		assertEquals(0f, matrix.get(9), EPSILON);
		assertEquals(1f, matrix.get(10), EPSILON);
		assertEquals(30f, matrix.get(11), EPSILON);

		assertEquals(0f, matrix.get(12), EPSILON);
		assertEquals(0f, matrix.get(13), EPSILON);
		assertEquals(0f, matrix.get(14), EPSILON);
		assertEquals(1f, matrix.get(15), EPSILON);

	}

	@Test
	public void modelViewMatrixOnlyTranspose() throws Exception {
		MatrixBasedCamera camera = new MatrixBasedCamera(new CameraPerspective(45, 4.0f/3.0f, 1, 1000));
		ShaderProgram shaderProgram = MockUtil.deepMock(ShaderProgram.class);
		Matrix4 model = new Matrix4(1, 0, 0, 3,
				0, 1, 0, 2,
				0, 0, 1, -1,
				0, 0, 0, 1);

		camera.translate(new Vector3(5, -1, 10));

		shaderPopulator.pushTransforms(model, shaderProgram, camera);

		ArgumentCatcher<FloatBuffer> catcher = ArgumentCatcher.create(mAny(FloatBuffer.class));
		verifyOnce().on(shaderProgram.uniform(ShaderUniform.MODEL_VIEW_MATRIX)).setMatrix4(match(catcher));
		FloatBuffer matrix = catcher.getValue();

		assertEquals(1f, matrix.get(0), EPSILON);
		assertEquals(0f, matrix.get(1), EPSILON);
		assertEquals(0f, matrix.get(2), EPSILON);
		assertEquals(-2f, matrix.get(3), EPSILON);

		assertEquals(0f, matrix.get(4), EPSILON);
		assertEquals(1f, matrix.get(5), EPSILON);
		assertEquals(0f, matrix.get(6), EPSILON);
		assertEquals(3f, matrix.get(7), EPSILON);

		assertEquals(0f, matrix.get(8), EPSILON);
		assertEquals(0f, matrix.get(9), EPSILON);
		assertEquals(1f, matrix.get(10), EPSILON);
		assertEquals(-11f, matrix.get(11), EPSILON);

		assertEquals(0f, matrix.get(12), EPSILON);
		assertEquals(0f, matrix.get(13), EPSILON);
		assertEquals(0f, matrix.get(14), EPSILON);
		assertEquals(1f, matrix.get(15), EPSILON);
	}

	@Test
	public void modelViewMatrixTransposeAndRoll() throws Exception {
		MatrixBasedCamera camera = new MatrixBasedCamera(new CameraPerspective(45, 4.0f/3.0f, 1, 1000));
		ShaderProgram shaderProgram = MockUtil.deepMock(ShaderProgram.class);
		Matrix4 model = new Matrix4(1, 0, 0, 3,
				0, 1, 0, 2,
				0, 0, 1, -1,
				0, 0, 0, 1);

		camera.translate(new Vector3(5, -1, 10));
		Quaternion orientation = Quaternion.fromAngleAxis(Math.PI/4, Vector3.UNIT_Z);
		camera.setOrientation(orientation);

		shaderPopulator.pushTransforms(model, shaderProgram, camera);

		ArgumentCatcher<FloatBuffer> catcher = ArgumentCatcher.create(mAny(FloatBuffer.class));
		verifyOnce().on(shaderProgram.uniform(ShaderUniform.MODEL_VIEW_MATRIX)).setMatrix4(match(catcher));
		FloatBuffer matrix = catcher.getValue();

		assertEquals(0.7071067690849304f, matrix.get(0), EPSILON);
		assertEquals(0.7071067690849304f, matrix.get(1), EPSILON);
		assertEquals(0f, matrix.get(2), EPSILON);
		assertEquals(-1.4644660949707031f, matrix.get(3), EPSILON);

		assertEquals(-0.7071067690849304f, matrix.get(4), EPSILON);
		assertEquals(0.7071067690849304f, matrix.get(5), EPSILON);
		assertEquals(0f, matrix.get(6), EPSILON);
		assertEquals(0.2928932309150696f, matrix.get(7), EPSILON);

		assertEquals(0f, matrix.get(8), EPSILON);
		assertEquals(0f, matrix.get(9), EPSILON);
		assertEquals(1f, matrix.get(10), EPSILON);
		assertEquals(-11f, matrix.get(11), EPSILON);

		assertEquals(0f, matrix.get(12), EPSILON);
		assertEquals(0f, matrix.get(13), EPSILON);
		assertEquals(0f, matrix.get(14), EPSILON);
		assertEquals(1f, matrix.get(15), EPSILON);
	}

}
