package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;

public interface RenderState {
	public void enable(FearGl fearGl, ShaderProgram shaderProgram);

	public void disable(FearGl fearGl, ShaderProgram shaderProgram);
}
