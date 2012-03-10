package se.fearlessgames.fear.vbo;

import se.fearlessgames.fear.gl.BufferTarget;
import se.fearlessgames.fear.gl.BufferUsage;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.VertexIndexMode;

import java.nio.IntBuffer;

public class VertexBufferObject {

	private final int vertexBufferId;
	private final int indexBufferId;
	private final InterleavedBuffer interleavedBuffer;

	private final IntBuffer indices;
	private final VertexIndexMode indexMode;

	public VertexBufferObject(
			FearGl fearGl,
			InterleavedBuffer interleavedBuffer,
			IntBuffer indices,
			VertexIndexMode indexMode) {
		this.interleavedBuffer = interleavedBuffer;
		this.indices = indices;
		this.indexMode = indexMode;

		vertexBufferId = fearGl.glGenBuffers();
		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vertexBufferId);
		fearGl.glBufferData(BufferTarget.GL_ARRAY_BUFFER, interleavedBuffer.getBuffer(), BufferUsage.GL_STATIC_DRAW);

		indexBufferId = fearGl.glGenBuffers();

		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		fearGl.glBufferData(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indices, BufferUsage.GL_STATIC_DRAW);

		fearGl.glReleaseBuffer(BufferTarget.GL_ARRAY_BUFFER);
		fearGl.glReleaseBuffer(BufferTarget.GL_ARRAY_BUFFER);
	}


	public int getVertexBufferId() {
		return vertexBufferId;
	}

	public InterleavedBuffer getInterleavedBuffer() {
		return interleavedBuffer;
	}

	public int getIndexBufferId() {
		return indexBufferId;
	}

	public VertexIndexMode getIndexMode() {
		return indexMode;
	}

	public int getIndexBufferSize() {
		return indices.limit();
	}
}
