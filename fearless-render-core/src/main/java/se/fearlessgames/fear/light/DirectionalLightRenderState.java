package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;

public class DirectionalLightRenderState implements RenderState {
	public final static DirectionalLightRenderState DEFAULT = new DirectionalLightRenderState(new DefaultAmbientDirectionalLight());

	private final DirectionalLight directionalLight;

	public DirectionalLightRenderState(DirectionalLight directionalLight) {
		this.directionalLight = directionalLight;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		shaderProgram.setUniformVector3("directionalLight.location", directionalLight.getLocation());
		shaderProgram.setUniformVector4("directionalLight.diffuse", directionalLight.getDiffuse());
		shaderProgram.setUniformVector4("directionalLight.ambient", directionalLight.getAmbient());
		shaderProgram.setUniformVector4("directionalLight.specular", directionalLight.getSpecular());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}
}
