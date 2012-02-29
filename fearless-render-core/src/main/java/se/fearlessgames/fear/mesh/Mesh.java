package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.RenderBucket;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Mesh {
	private final VertexBufferObject vbo;
    private final MeshType meshType;

    public Mesh(VertexBufferObject vbo, MeshType meshType) {
		this.vbo = vbo;
        this.meshType = meshType;
	}


    public VertexBufferObject getVbo() {
		return vbo;
	}

	public ShaderProgram getShaderProgram() {
		return meshType.getShaderProgram();
	}

	public Iterable<RenderState> getRenderStates() {
		return meshType.getRenderStates();
	}

    public RenderBucket getBucket() {
		return meshType.getBucket();
	}
}
