package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.shader.ShaderProgram;

public interface RenderState {
	public void enable(FearGl fearGl, ShaderProgram shaderProgram);

	public void disable(FearGl fearGl, ShaderProgram shaderProgram);
}
