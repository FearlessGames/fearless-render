package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.vbo.InterleavedBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class MeshData {
	private String name;
	private VertexIndexMode vertexIndexMode;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer colorBuffer;
	private HashMap<Integer, FloatBuffer> textureCoordsMap = new HashMap<Integer, FloatBuffer>();
	private IntBuffer indices;

	public MeshData(String name) {
		this.name = name;
	}

	public MeshData(String name, FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer colorBuffer, FloatBuffer textureCoordinates, IntBuffer indices, VertexIndexMode vertexIndexMode) {
		this.name = name;
		this.vertexBuffer = BufferUtils.duplicate(vertexBuffer);
		this.normalBuffer = BufferUtils.duplicate(normalBuffer);
		this.colorBuffer = BufferUtils.duplicate(colorBuffer);
		textureCoordsMap.put(0, BufferUtils.duplicate(textureCoordinates));
		this.indices = BufferUtils.duplicate(indices);
		this.vertexIndexMode = vertexIndexMode;
	}

	public int getVertexCount() {
		if (vertexBuffer == null) {
			return 0;
		}
		return vertexBuffer.limit() / vertexIndexMode.getVertexCount();
	}

	public MeshData duplicate() {
		MeshData meshData = new MeshData(name);
		meshData.vertexIndexMode = vertexIndexMode;
		meshData.vertexBuffer = BufferUtils.duplicate(vertexBuffer);
		meshData.normalBuffer = BufferUtils.duplicate(normalBuffer);
		meshData.colorBuffer = BufferUtils.duplicate(colorBuffer);
		meshData.indices = BufferUtils.duplicate(indices);
		for (Map.Entry<Integer, FloatBuffer> entry : textureCoordsMap.entrySet()) {
			meshData.textureCoordsMap.put(entry.getKey(), BufferUtils.duplicate(entry.getValue()));
		}
		return meshData;

	}

	public InterleavedBuffer createInterleavedBuffer() {
		int verticsSize = getSize(vertexBuffer);
		int normalsSize = getSize(normalBuffer);
		int colorsSize = getSize(colorBuffer);


		int texturesSize = getSize(textureCoordsMap.get(0));


		FloatBuffer buffer;

		if (normalsSize == 0 && colorsSize == 0 && texturesSize == 0) {
			buffer = vertexBuffer.duplicate();
		} else {
			FloatBuffer vertexBuffer = BufferUtils.duplicate(this.vertexBuffer);
			FloatBuffer normalBuffer = BufferUtils.duplicate(this.normalBuffer);
			FloatBuffer colorBuffer = BufferUtils.duplicate(this.colorBuffer);
			FloatBuffer textureCoordsBuffer = BufferUtils.duplicate(this.textureCoordsMap.get(0));

			//using V[xyz]N[xyz]C[rgba]T1[st] format for the interleaved buffer
			buffer = BufferUtils.createFloatBuffer(verticsSize + normalsSize + colorsSize + texturesSize);
			for (int i = 0; i < verticsSize / 3; i++) {
				buffer.put(vertexBuffer.get()); //x
				buffer.put(vertexBuffer.get()); //y
				buffer.put(vertexBuffer.get()); //z

				if (normalsSize != 0) {
					buffer.put(normalBuffer.get()); //x
					buffer.put(normalBuffer.get()); //y
					buffer.put(normalBuffer.get()); //z
				}

				if (colorsSize != 0) {
					buffer.put(colorBuffer.get()); //r
					buffer.put(colorBuffer.get()); //g
					buffer.put(colorBuffer.get()); //b
					buffer.put(colorBuffer.get()); //a
				}

				if (texturesSize != 0) {
					buffer.put(textureCoordsBuffer.get()); //s
					buffer.put(textureCoordsBuffer.get()); //t
				}
			}
			buffer.flip();
		}

		return new InterleavedBuffer(buffer,
				normalsSize != 0,
				colorsSize != 0,
				texturesSize != 0);
	}

	private int getSize(FloatBuffer buffer) {
		if (buffer == null) {
			return 0;
		}
		return buffer.limit();
	}


	public void setTextureCoords(int texCoord, FloatBuffer buffer) {
		textureCoordsMap.put(texCoord, buffer);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VertexIndexMode getVertexIndexMode() {
		return vertexIndexMode;
	}

	public void setVertexIndexMode(VertexIndexMode vertexIndexMode) {
		this.vertexIndexMode = vertexIndexMode;
	}

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public void setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}

	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}

	public void setNormalBuffer(FloatBuffer normalBuffer) {
		this.normalBuffer = normalBuffer;
	}

	public HashMap<Integer, FloatBuffer> getTextureCoordsMap() {
		return textureCoordsMap;
	}

	public IntBuffer getIndices() {
		return indices;
	}

	public void setIndices(IntBuffer indices) {
		this.indices = indices;
	}

	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	public void setColorBuffer(FloatBuffer colorBuffer) {
		this.colorBuffer = colorBuffer;
	}
}
