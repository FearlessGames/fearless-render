package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.VertexIndexMode;

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
		this.vertexBuffer = vertexBuffer;
		this.normalBuffer = normalBuffer;
		this.colorBuffer = colorBuffer;
		textureCoordsMap.put(0, textureCoordinates);
		this.indices = indices;
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
