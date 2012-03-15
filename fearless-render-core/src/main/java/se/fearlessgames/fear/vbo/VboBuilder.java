package se.fearlessgames.fear.vbo;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VboBuilder {
	private final FloatBuffer vertices;
	private final FearGl fearGl;
	private FloatBuffer normals = FloatBuffer.allocate(0);
	private FloatBuffer colors = FloatBuffer.allocate(0);
	private FloatBuffer textureCoords = FloatBuffer.allocate(0);

	private IntBuffer indices;
	private VertexIndexMode indexMode = VertexIndexMode.TRIANGLES;


	private VboBuilder(FearGl fearGl, FloatBuffer vertices) {
		this.fearGl = fearGl;
		this.vertices = vertices;
		this.vertices.rewind();
	}

	public static VboBuilder fromBuffer(FearGl fearGl, FloatBuffer vertices) {
		return new VboBuilder(fearGl, vertices.duplicate());
	}

	public static VboBuilder fromArray(FearGl fearGl, float[] vertices) {
		return new VboBuilder(fearGl, createFloatBuffer(vertices));
	}

	public VboBuilder normals(float[] normals) {
		this.normals = createFloatBuffer(normals);
		return this;
	}

	public VboBuilder normals(FloatBuffer normals) {
		this.normals = normals.duplicate();
		this.normals.rewind();
		return this;
	}

	public VboBuilder colors(float[] colors) {
		this.colors = createFloatBuffer(colors);
		return this;
	}

	public VboBuilder colors(FloatBuffer colors) {
		this.colors = colors.duplicate();
		this.colors.rewind();
		return this;
	}

	public VboBuilder textureCoords(float[] textureCoords) {
		this.textureCoords = createFloatBuffer(textureCoords);
		return this;
	}

	public VboBuilder textureCoords(FloatBuffer textureCoords) {
		this.textureCoords = textureCoords.duplicate();
		this.textureCoords.rewind();
		return this;
	}

	public VboBuilder indices(IntBuffer indices) {
		this.indices = indices.duplicate();
		this.indices.rewind();
		return this;
	}

	public VboBuilder indices(int[] indices) {
		return indices(createIntBuffer(indices));
	}

	public VboBuilder triangles() {
		indexMode = VertexIndexMode.TRIANGLES;
		return this;
	}

	public VboBuilder quads() {
		indexMode = VertexIndexMode.QUADS;
		return this;
	}

	public VboBuilder triangleStrips() {
		indexMode = VertexIndexMode.TRIANGLE_STRIP;
		return this;
	}

	private void setVertexIndexMode(VertexIndexMode vertexIndexMode) {
		this.indexMode = vertexIndexMode;
	}

	public VertexBufferObject build() {

		if (indices == null) {
			int[] idx = new int[vertices.limit()];
			for (int i = 0; i < idx.length; i++) {
				idx[i] = i;
			}
			indices = createIntBuffer(idx);
		}

		return new VertexBufferObject(fearGl, buildInterleavedBuffer(), indices, indexMode);
	}

	public InterleavedBuffer buildInterleavedBuffer() {

		int verticsSize = vertices.limit();
		int normalsSize = normals.limit();
		int colorsSize = colors.limit();
		int texturesSize = textureCoords.limit();

		FloatBuffer buffer;

		if (normalsSize == 0 && colorsSize == 0 && texturesSize == 0) {
			buffer = vertices;
		} else {

			//using V[xyz]N[xyz]C[rgba]T1[st] format for the interleaved buffer
			buffer = BufferUtils.createFloatBuffer(verticsSize + normalsSize + colorsSize + texturesSize);
			for (int i = 0; i < verticsSize / 3; i++) {
				buffer.put(vertices.get()); //x
				buffer.put(vertices.get()); //y
				buffer.put(vertices.get()); //z

				if (normalsSize != 0) {
					buffer.put(normals.get()); //x
					buffer.put(normals.get()); //y
					buffer.put(normals.get()); //z
				}

				if (colorsSize != 0) {
					buffer.put(colors.get()); //r
					buffer.put(colors.get()); //g
					buffer.put(colors.get()); //b
					buffer.put(colors.get()); //a
				}

				if (texturesSize != 0) {
					buffer.put(textureCoords.get()); //s
					buffer.put(textureCoords.get()); //t
				}
			}
			buffer.flip();
		}

		InterleavedBuffer interleavedBuffer = new InterleavedBuffer(buffer,
				normalsSize != 0,
				colorsSize != 0,
				texturesSize != 0);

		return interleavedBuffer;
	}


	static IntBuffer createIntBuffer(int[] indices) {
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
		indexBuffer.put(indices);
		indexBuffer.flip();
		return indexBuffer;
	}

	static FloatBuffer createFloatBuffer(float[] vertices) {
		FloatBuffer vertexBuffer = BufferUtils.createFloat3Buffer(vertices.length);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();
		return vertexBuffer;
	}

	public static VboBuilder fromMeshData(FearGl fearGl, MeshData meshData) {
		VboBuilder vboBuilder = VboBuilder.fromBuffer(fearGl, meshData.getVertexBuffer());
		if (meshData.getIndices() != null) {
			vboBuilder.indices(meshData.getIndices());
		}

		if (meshData.getNormalBuffer() != null) {
			vboBuilder.normals(meshData.getNormalBuffer());
		}

		FloatBuffer texCoords = meshData.getTextureCoordsMap().get(0);

		if (texCoords != null) {
			vboBuilder.textureCoords(texCoords);
		}

		vboBuilder.setVertexIndexMode(meshData.getVertexIndexMode());

		return vboBuilder;
	}
}
