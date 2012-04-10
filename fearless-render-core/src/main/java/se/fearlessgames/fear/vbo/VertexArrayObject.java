package se.fearlessgames.fear.vbo;

import se.fearlessgames.fear.gl.BufferTarget;
import se.fearlessgames.fear.gl.BufferUsage;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.shader.ShaderAttribute;
import se.fearlessgames.fear.shader.ShaderProgram;

import java.nio.IntBuffer;

public class VertexArrayObject {
	private final VertexIndexMode vertexIndexMode;
	private final ShaderProgram shaderProgram;
	private final int vaoId;
	private final int vboId;
	private final int indicesId;
	private final int indicesCount;

	public VertexArrayObject(FearGl fearGl, InterleavedBuffer interleavedBuffer, IntBuffer indices, VertexIndexMode vertexIndexMode, ShaderProgram shaderProgram) {
		this.vertexIndexMode = vertexIndexMode;
		this.shaderProgram = shaderProgram;
		indicesCount = indices.limit();

		vaoId = fearGl.glGenVertexArrays();
		fearGl.glBindVertexArray(vaoId);

		vboId = fearGl.glGenBuffers();
		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vboId);
		fearGl.glBufferData(BufferTarget.GL_ARRAY_BUFFER, interleavedBuffer.getBuffer(), BufferUsage.GL_STATIC_DRAW);

		//setup how to send the vertex data to the shader
		int stride = interleavedBuffer.getStride();
		int offset = 0;

		shaderProgram.attribute(ShaderAttribute.VERTEX).setAttribute(3, stride, offset);

		offset = 3 * 4;

		if (interleavedBuffer.isNormals()) {
			shaderProgram.attribute(ShaderAttribute.NORMAL).setAttribute(3, stride, offset);
			offset += (3 * 4);
		}

		if (interleavedBuffer.isColors()) {
			shaderProgram.attribute(ShaderAttribute.COLOR).setAttribute(4, stride, offset);
			offset += (4 * 4);
		}

		if (interleavedBuffer.isTextureCords()) {
			shaderProgram.attribute(ShaderAttribute.TEXTURE_COORD).setAttribute(2, stride, offset);
		}

		//create the indices id
		indicesId = fearGl.glGenBuffers();
		//then bind it
		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indicesId);
		//and copy data into it
		fearGl.glBufferData(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indices, BufferUsage.GL_STATIC_DRAW);

		//remove the vao binding
		fearGl.glBindVertexArray(0);
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVboId() {
		return vboId;
	}

	public int getIndicesId() {
		return indicesId;
	}

	public VertexIndexMode getVertexIndexMode() {
		return vertexIndexMode;
	}

	public int getIndicesCount() {
		return indicesCount;
	}
}
