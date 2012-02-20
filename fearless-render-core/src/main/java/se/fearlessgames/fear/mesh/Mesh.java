package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.HashMap;
import java.util.Map;

public class Mesh {
	private final VertexBufferObject vbo;
	private final ShaderProgram shaderProgram;
	private final Map<Class<? extends RenderState>, RenderState> renderStateMap = new HashMap<Class<? extends RenderState>, RenderState>();

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram) {
		this.vbo = vbo;
		this.shaderProgram = shaderProgram;
	}


	public VertexBufferObject getVbo() {
		return vbo;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public Iterable<RenderState> getRenderStates() {
		return renderStateMap.values();
	}

	public <T extends RenderState> void addRenderState(T renderState) {
		renderStateMap.put(renderState.getClass(), renderState);
	}

	public void removeRenderState(Class<? extends RenderState> clazz) {
		renderStateMap.remove(clazz);
	}

	public boolean hasRenderState(Class<? extends RenderState> clazz) {
		return renderStateMap.containsKey(clazz);
	}
}
