package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.renderbucket.RenderBucket;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.List;

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

	public List<RenderState> getRenderStates() {
		return meshType.getRenderStates();
	}

    public RenderBucket getBucket() {
		return meshType.getBucket();
	}

    public MeshType getMeshType() {
        return meshType;
    }
}
