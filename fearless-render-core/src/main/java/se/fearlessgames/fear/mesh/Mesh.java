package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.renderbucket.RenderBucket;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.util.List;

public class Mesh {
	private final VertexArrayObject vao;
	private final MeshType meshType;

	public Mesh(VertexArrayObject vao, MeshType meshType) {
		this.vao = vao;
		this.meshType = meshType;
	}


	public VertexArrayObject getVao() {
		return vao;
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
