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
	private final ShaderProgram shaderProgram;
	private final Collection<RenderState> renderStates = new ArrayList<RenderState>();
	private final RenderBucket bucket;

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram) {
		this.vbo = vbo;
		this.shaderProgram = shaderProgram;
		bucket = RenderBucket.OPAQUE;
	}

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram, RenderBucket bucket) {
		this.vbo = vbo;
		this.shaderProgram = shaderProgram;
		this.bucket = bucket;
	}


	public VertexBufferObject getVbo() {
		return vbo;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public Iterable<RenderState> getRenderStates() {
		return renderStates;
	}

	public <T extends RenderState> void addRenderState(T renderState) {
        renderStates.add(renderState);
	}

    public RenderBucket getBucket() {
		return bucket;
	}
}
