package se.fearlessgames.fear.vbo;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VboBuilder {
	private final FloatBuffer vertices;
	private IntBuffer indices;
	private VertexDrawMode drawMode = VertexDrawMode.TRIANGLES;

	private VboBuilder(FloatBuffer vertices) {
		this.vertices = vertices;
	}

	public static VboBuilder fromBuffer(FloatBuffer vertices) {
		return new VboBuilder(vertices);
	}

	public static VboBuilder fromArray(float[] vertices) {
		return new VboBuilder(createFloatBuffer(vertices));
	}

	public VboBuilder indices(IntBuffer indices) {
		this.indices = indices;
		return this;
	}

	public VboBuilder indices(int[] indices) {
		return indices(createIntBuffer(indices));
	}

	public VboBuilder triangles() {
		drawMode = VertexDrawMode.TRIANGLES;
		return this;
	}

	public VboBuilder quads() {
		drawMode = VertexDrawMode.QUADS;
		return this;
	}

	public VboBuilder triangleStrips() {
		drawMode = VertexDrawMode.TRIANGLE_STRIP;
		return this;
	}

	public VertexBufferObject build() {
		if (indices == null) {
			int[] idx = new int[vertices.capacity()];
			for (int i = 0; i < idx.length; i++) {
				idx[i] = i;
			}
			indices = createIntBuffer(idx);
		}
		return new VertexBufferObject(vertices, indices, drawMode);
	}

	static IntBuffer createIntBuffer(int[] indices) {
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
		indexBuffer.put(indices);
		indexBuffer.flip();
		return indexBuffer;
	}

	static FloatBuffer createFloatBuffer(float[] vertices) {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * 3);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();
		return vertexBuffer;
	}
}
