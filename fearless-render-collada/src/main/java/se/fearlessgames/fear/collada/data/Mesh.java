package se.fearlessgames.fear.collada.data;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class Mesh {
	private String name;
	private Node parent;
	private IndexMode indexMode;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private HashMap<Integer, FloatBuffer> textureCoordsMap = new HashMap<Integer, FloatBuffer>();
	private IntBuffer indices;

	public Mesh(String name) {
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

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
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
}
