package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.RenderBucket;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Mesh {
	private final VertexBufferObject vbo;
    private final MeshType meshType;

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram) {
		this.vbo = vbo;
        this.meshType = new MeshType(shaderProgram, RenderBucket.OPAQUE);
	}

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram, RenderBucket bucket) {
		this.vbo = vbo;
        this.meshType = new MeshType(shaderProgram, bucket);
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

	public void addRenderState(RenderState renderState) {
        meshType.addRenderState(renderState);
	}

    public RenderBucket getBucket() {
		return meshType.getBucket();
	}
}
