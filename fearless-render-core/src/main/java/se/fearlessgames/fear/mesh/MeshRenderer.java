package se.fearlessgames.fear.mesh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.TransformedMesh;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.gl.Culling;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.IndexDataType;
import se.fearlessgames.fear.math.GlMatrixBuilder;
import se.fearlessgames.fear.math.Matrix3;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.shader.ShaderProgram;
import se.fearlessgames.fear.shader.ShaderUniform;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.util.Collection;
import java.util.List;

public class MeshRenderer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	public final FearGl fearGl;

	public MeshRenderer(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public void render(Mesh mesh, Matrix4 modelView, CameraPerspective cameraPerspective) {
		MeshType meshType = mesh.getMeshType();
		ShaderProgram shader = meshType.getShaderProgram();
		List<RenderState> renderStates = meshType.getRenderStates();

		pushTransforms(modelView, shader, cameraPerspective);
		useShader(shader);
		enableStates(shader, renderStates);

		drawElements(mesh.getVao());

		disableStates(shader, renderStates);
	}

	public void renderMeshes(Collection<TransformedMesh> meshes, boolean renderBackFaces, CameraPerspective cameraPerspective) {
		fearGl.glCullFace(Culling.FRONT);
		MeshType prev = null;
		ShaderProgram shader = null;
		List<RenderState> renderStates = null;
		VertexArrayObject prevVao = null;
		for (TransformedMesh mesh : meshes) {
			MeshType meshType = mesh.mesh.getMeshType();
			if (prev != meshType) {
				if (prev != null) {
					disableStates(shader, renderStates);
				}
				shader = meshType.getShaderProgram();
				renderStates = meshType.getRenderStates();
				prev = meshType;
				useShader(shader);
				enableStates(shader, renderStates);
				prevVao = null;
			}

			pushTransforms(mesh.transform, shader, cameraPerspective);

			VertexArrayObject curVao = mesh.mesh.getVao();
			if (prevVao != curVao) {
				enableVao(curVao);
				prevVao = curVao;
			}

			if (renderBackFaces) {
				fearGl.glCullFace(Culling.FRONT_AND_BACK);
				drawElements(curVao);
				fearGl.glCullFace(Culling.FRONT);
			} else {
				drawElements(curVao);
			}
		}

		if (prev != null) {
			disableStates(shader, renderStates);
		}
	}

	private void enableVao(VertexArrayObject vao) {
		fearGl.glBindVertexArray(vao.getVaoId());
	}

	private void disableStates(ShaderProgram shader, List<RenderState> renderStates) {
		for (int i = renderStates.size() - 1; i >= 0; i--) {
			renderStates.get(i).disable(fearGl, shader);
		}
	}

	private void enableStates(ShaderProgram shader, List<RenderState> renderStates) {
		for (RenderState renderState : renderStates) {
			renderState.enable(fearGl, shader);
		}
	}

	private void useShader(ShaderProgram shader) {
		fearGl.glUseProgram(shader.getShaderProgram());
	}

	private void pushTransforms(Matrix4 modelView, ShaderProgram shader, CameraPerspective cameraPerspective) {

		Matrix4 projection = cameraPerspective.getMatrix();
		Matrix4 modelViewProjection = projection.multiply(modelView);
		log.info("projection: " + projection);
		log.info("modelView: " + modelView);
		log.info("modelViewProjection: " + modelViewProjection);
		Matrix3 normalMatrix = new Matrix3(modelView).invert().transpose();
		shader.uniform(ShaderUniform.MODEL_VIEW_PROJECTION_MATRIX).setMatrix4(GlMatrixBuilder.convert(modelViewProjection));
		shader.uniform(ShaderUniform.MODEL_VIEW_MATRIX).setMatrix4(GlMatrixBuilder.convert(modelView));
		shader.uniform(ShaderUniform.PROJECTION_MATRIX).setMatrix4(cameraPerspective.getMatrixAsBuffer());
		shader.uniform(ShaderUniform.NORMAL_MATRIX).setMatrix3(GlMatrixBuilder.convert(normalMatrix));
	}


	private void drawElements(VertexArrayObject vao) {
		fearGl.glDrawElements(vao.getVertexIndexMode(), vao.getIndicesCount(), IndexDataType.GL_UNSIGNED_INT, 0);
	}


}

