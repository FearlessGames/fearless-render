package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.GlMatrixBuilder;
import se.fearlessgames.fear.math.Matrix3;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.vbo.InterleavedBuffer;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class MeshRenderer {

	// TODO: replace this reference with an output to keep rendering and OpenGL calls in different threads
	private final FearGl fearGl;
	// TODO: probably move this
	private final ShaderProgram shader;
	private final PerspectiveBuilder perspectiveBuilder;

	public MeshRenderer(FearGl fearGl, ShaderProgram shader, PerspectiveBuilder perspectiveBuilder) {
		this.fearGl = fearGl;
		this.shader = shader;
		this.perspectiveBuilder = perspectiveBuilder;
	}

	public void render(Mesh mesh, Matrix4 modelView) {

		fearGl.glUseProgram(shader.getShaderProgram());

		if (mesh.hasTexture()) {
			fearGl.glEnable(Capability.GL_TEXTURE_2D);
			fearGl.glBindTexture(TextureType.TEXTURE_2D, mesh.getTexture().getId());
		}

		VertexBufferObject vbo = mesh.getVbo();
		InterleavedBuffer interleavedBuffer = vbo.getInterleavedBuffer();

		Matrix3 normalMatrix = new Matrix3(modelView).invert().transpose();

		shader.setUniformMatrix4("projectionMatrix", perspectiveBuilder.getMatrixAsBuffer());
		shader.setUniformMatrix4("modelViewMatrix", GlMatrixBuilder.convert(modelView));
		shader.setUniformMatrix3("normalMatrix", GlMatrixBuilder.convert(normalMatrix));

		fearGl.glBindFragDataLocation(shader.getShaderProgram(), 0, "fragColor");

		enableStates(interleavedBuffer);

		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vbo.getVertexBufferId());


		int stride = interleavedBuffer.getStride();
		int offset = 0;

		fearGl.glVertexPointer(3, DataType.GL_FLOAT, stride, offset);
		shader.setVertexAttribute("vertex", 3, stride, offset);


		offset = 3 * 4;

		if (interleavedBuffer.isNormals()) {
			fearGl.glNormalPointer(DataType.GL_FLOAT, stride, offset);
			shader.setVertexAttribute("normal", 3, stride, offset);
			offset += (3 * 4);
		}

		if (interleavedBuffer.isColors()) {
			fearGl.glColorPointer(4, DataType.GL_FLOAT, stride, offset);
			shader.setVertexAttribute("color", 4, stride, offset);
			offset += (4 * 4);
		}

		if (interleavedBuffer.isTextureCords()) {
			fearGl.glTexCoordPointer(2, DataType.GL_FLOAT, stride, offset);
			shader.setVertexAttribute("textureCoord", 2, stride, offset);
		}


		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, vbo.getIndexBufferId());

		fearGl.glDrawElements(vbo.getDrawMode(), vbo.getIndexBufferSize(), IndexDataType.GL_UNSIGNED_INT, 0);

		disableStates(interleavedBuffer);
		fearGl.glUseProgram(0);
	}


	private void enableStates(InterleavedBuffer interleavedBuffer) {
		fearGl.glEnableClientState(ClientState.GL_VERTEX_ARRAY);

		if (interleavedBuffer.isNormals()) {
			fearGl.glEnableClientState(ClientState.GL_NORMAL_ARRAY);
		}

		if (interleavedBuffer.isColors()) {
			fearGl.glEnableClientState(ClientState.GL_COLOR_ARRAY);
		}

		if (interleavedBuffer.isTextureCords()) {
			fearGl.glEnableClientState(ClientState.GL_TEXTURE_COORD_ARRAY);
		}
	}

	private void disableStates(InterleavedBuffer interleavedBuffer) {
		if (interleavedBuffer.isNormals()) {
			fearGl.glDisableClientState(ClientState.GL_NORMAL_ARRAY);
		}

		if (interleavedBuffer.isColors()) {
			fearGl.glDisableClientState(ClientState.GL_COLOR_ARRAY);
		}

		if (interleavedBuffer.isTextureCords()) {
			fearGl.glDisableClientState(ClientState.GL_TEXTURE_COORD_ARRAY);
		}
		fearGl.glDisableClientState(ClientState.GL_VERTEX_ARRAY);
	}

}
