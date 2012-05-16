package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.TransformedMesh;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.gl.Culling;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.IndexDataType;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.shader.ShaderProgram;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.util.Collection;
import java.util.List;

public class MeshRenderer {
	public final FearGl fearGl;
	private final ShaderPopulator shaderPopulator;

	public MeshRenderer(FearGl fearGl, ShaderPopulator shaderPopulator) {
		this.fearGl = fearGl;
		this.shaderPopulator = shaderPopulator;
	}

	public void render(Mesh mesh, Matrix4 transformMatrix, Camera camera) {
		MeshType meshType = mesh.getMeshType();
		ShaderProgram shader = meshType.getShaderProgram();
		List<RenderState> renderStates = meshType.getRenderStates();

		shaderPopulator.pushTransforms(transformMatrix, shader, camera);
		useShader(shader);
		enableStates(shader, renderStates);

		drawElements(mesh.getVao());

		disableStates(shader, renderStates);
	}

	public void renderMeshes(Collection<TransformedMesh> meshes, boolean renderBackFaces, Camera camera) {
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

			shaderPopulator.pushTransforms(mesh.transform, shader, camera);

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




	private void drawElements(VertexArrayObject vao) {
		fearGl.glDrawElements(vao.getVertexIndexMode(), vao.getIndicesCount(), IndexDataType.GL_UNSIGNED_INT, 0);
	}


}

