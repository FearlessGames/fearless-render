package se.fearlessgames.fear.mesh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.math.Matrix3;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.shader.ShaderProgram;
import se.fearlessgames.fear.shader.ShaderUniform;

public class ShaderPopulator {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public void pushTransforms(Matrix4 modelTransform, ShaderProgram shader, Camera camera) {
		Matrix3 normalMatrix = new Matrix3(modelTransform).invert().transpose();

		//Matrix4 modelViewProjection = modelTransform.multiply(camera.getViewProjectionMatrix());
		Matrix4 viewMatrix = camera.getViewMatrix();
		Matrix4 modelView = viewMatrix.multiply(modelTransform);
		Matrix4 projectionMatrix = camera.getPerspective().getMatrix();
		Matrix4 modelViewProjection = projectionMatrix.multiply(modelView);

		log.info("model: " + modelTransform);
		log.info("projection: " + projectionMatrix);
		log.info("modelView: " + modelView);
		log.info("modelViewProjection: " + modelViewProjection);

		shader.uniform(ShaderUniform.MODEL_VIEW_PROJECTION_MATRIX).setMatrix4(modelViewProjection.toFloatBuffer());
		shader.uniform(ShaderUniform.MODEL_VIEW_MATRIX).setMatrix4(modelView.toFloatBuffer());
		shader.uniform(ShaderUniform.PROJECTION_MATRIX).setMatrix4(projectionMatrix.toFloatBuffer());
		shader.uniform(ShaderUniform.MODEL_MATRIX).setMatrix4(modelTransform.toFloatBuffer());
		shader.uniform(ShaderUniform.NORMAL_MATRIX).setMatrix3(normalMatrix.toFloatBuffer());
	}
}
