package se.fearlessgames.fear.vbo;

import se.fearlessgames.fear.gl.*;

import java.nio.IntBuffer;

public class VertexBufferObject {

	private final int vertexBufferId;
	private final int indexBufferId;
	private final FearGl fearGl;
	private final InterleavedBuffer interleavedBuffer;

	private final IntBuffer indices;
	private final VertexDrawMode drawMode;

	public VertexBufferObject(
			FearGl fearGl,
			InterleavedBuffer interleavedBuffer,
			IntBuffer indices,
			VertexDrawMode drawMode) {
		this.fearGl = fearGl;
		this.interleavedBuffer = interleavedBuffer;
		this.indices = indices;
		this.drawMode = drawMode;

		vertexBufferId = fearGl.glGenBuffers();
		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vertexBufferId);
		fearGl.glBufferData(BufferTarget.GL_ARRAY_BUFFER, interleavedBuffer.getBuffer(), BufferUsage.GL_STATIC_DRAW);

		indexBufferId = fearGl.glGenBuffers();

		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		fearGl.glBufferData(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indices, BufferUsage.GL_STATIC_DRAW);

		fearGl.glReleaseBuffer(BufferTarget.GL_ARRAY_BUFFER);
		fearGl.glReleaseBuffer(BufferTarget.GL_ARRAY_BUFFER);
	}


	public void draw() {
		enableStates();

		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vertexBufferId);

		int stride = interleavedBuffer.getStride();
		int offset = 0;

		fearGl.glVertexPointer(3, DataType.GL_FLOAT, stride, offset);
		offset = 3 * 4;

		if (interleavedBuffer.isNormals()) {
			fearGl.glNormalPointer(DataType.GL_FLOAT, stride, offset);
			offset += (3 * 4);
		}

		if (interleavedBuffer.isColors()) {
			fearGl.glColorPointer(4, DataType.GL_FLOAT, stride, offset);
			offset += (4 * 4);
		}

		if (interleavedBuffer.isTextureCords()) {
			fearGl.glTexCoordPointer(2, DataType.GL_FLOAT, stride, offset);
		}

		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);

		fearGl.glDrawElements(drawMode, indices.limit(), IndexDataType.GL_UNSIGNED_INT, 0);


		disableStates();
	}


	private void enableStates() {
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

	private void disableStates() {
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

	public int getVertexBufferId() {
		return vertexBufferId;
	}

	public InterleavedBuffer getInterleavedBuffer() {
		return interleavedBuffer;
	}

	public int getIndexBufferId() {
		return indexBufferId;
	}

	public VertexDrawMode getDrawMode() {
		return drawMode;
	}

	public int getIndexBufferSize() {
		return indices.limit();
	}
}
