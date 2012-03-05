package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.BufferTarget;
import se.fearlessgames.fear.gl.Culling;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.IndexDataType;
import se.fearlessgames.fear.math.GlMatrixBuilder;
import se.fearlessgames.fear.math.Matrix3;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.vbo.InterleavedBuffer;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.Collection;
import java.util.List;

public class MeshRenderer {

	// TODO: replace this reference with an output to keep rendering and OpenGL calls in different threads
	public final FearGl fearGl;
	// TODO: probably move this
	private final PerspectiveBuilder perspectiveBuilder;

	public MeshRenderer(FearGl fearGl, PerspectiveBuilder perspectiveBuilder) {
		this.fearGl = fearGl;
		this.perspectiveBuilder = perspectiveBuilder;
	}

	public void render(Mesh mesh, Matrix4 modelView) {
		MeshType meshType = mesh.getMeshType();
		ShaderProgram shader = meshType.getShaderProgram();
		List<RenderState> renderStates = meshType.getRenderStates();

		prepareTransform(modelView, shader);
		prepareShader(shader);
		enableStates(shader, renderStates);
		updateVBOStates(shader, mesh.getVbo());
		drawElements(mesh.getVbo());
		disableStates(shader, renderStates);
	}

	public void renderMeshes(Collection<Renderer.AddedMesh> meshes, boolean renderBackFaces) {
		fearGl.glCullFace(Culling.FRONT);
		MeshType prev = null;
		ShaderProgram shader = null;
		List<RenderState> renderStates = null;
		VertexBufferObject prevVBO = null;
		for (Renderer.AddedMesh mesh : meshes) {
			MeshType meshType = mesh.mesh.getMeshType();
			if (prev != meshType) {
				if (prev != null) {
					disableStates(shader, renderStates);
				}
				shader = meshType.getShaderProgram();
				renderStates = meshType.getRenderStates();
				prev = meshType;
				prepareShader(shader);
				enableStates(shader, renderStates);
				prevVBO = null;
			}
			prepareTransform(mesh.transform, shader);
			VertexBufferObject curVBO = mesh.mesh.getVbo();
			if (prevVBO != curVBO) {

				updateVBOStates(shader, curVBO);
				prevVBO = curVBO;
			}
			if (renderBackFaces) {
				fearGl.glCullFace(Culling.BACK);
				drawElements(curVBO);
				fearGl.glCullFace(Culling.FRONT);
			}
			drawElements(curVBO);
		}
		if (prev != null) {
			disableStates(shader, renderStates);
		}
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

	private void prepareShader(ShaderProgram shader) {
		fearGl.glUseProgram(shader.getShaderProgram());
		fearGl.glBindFragDataLocation(shader.getShaderProgram(), 0, "fragColor");
	}

	private void prepareTransform(Matrix4 modelView, ShaderProgram shader) {
		Matrix3 normalMatrix = new Matrix3(modelView).invert().transpose();

		shader.setUniformMatrix4("projectionMatrix", perspectiveBuilder.getMatrixAsBuffer());
		shader.setUniformMatrix4("modelViewMatrix", GlMatrixBuilder.convert(modelView));
		shader.setUniformMatrix3("normalMatrix", GlMatrixBuilder.convert(normalMatrix));
	}


	private void drawElements(VertexBufferObject vbo) {
		fearGl.glDrawElements(vbo.getDrawMode(), vbo.getIndexBufferSize(), IndexDataType.GL_UNSIGNED_INT, 0);
	}

	private void updateVBOStates(ShaderProgram shader, VertexBufferObject vbo) {

		InterleavedBuffer interleavedBuffer = vbo.getInterleavedBuffer();
		int vertexBufferId = vbo.getVertexBufferId();

		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vertexBufferId);
		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, vbo.getIndexBufferId());

		int stride = interleavedBuffer.getStride();
		int offset = 0;

		shader.setVertexAttribute("vertex", 3, stride, offset);

		offset = 3 * 4;

		if (interleavedBuffer.isNormals()) {
			shader.setVertexAttribute("normal", 3, stride, offset);
			offset += (3 * 4);
		}

		if (interleavedBuffer.isColors()) {
			shader.setVertexAttribute("color", 4, stride, offset);
			offset += (4 * 4);
		}

		if (interleavedBuffer.isTextureCords()) {
			shader.setVertexAttribute("textureCoord", 2, stride, offset);
		}
	}


}
