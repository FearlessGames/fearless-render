package se.fearlessgames.fear.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class MeshData {
	private String name;
	private IndexMode indexMode;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer colorBuffer;
	private HashMap<Integer, FloatBuffer> textureCoordsMap = new HashMap<Integer, FloatBuffer>();
	private IntBuffer indices;

	public MeshData(String name) {
		this.name = name;
	}

	public int getVertexCount() {
		if (vertexBuffer == null) {
			return 0;
		}
		return vertexBuffer.limit() / indexMode.getVertexCount();
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

	public IndexMode getIndexMode() {
		return indexMode;
	}

	public void setIndexMode(IndexMode indexMode) {
		this.indexMode = indexMode;
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
