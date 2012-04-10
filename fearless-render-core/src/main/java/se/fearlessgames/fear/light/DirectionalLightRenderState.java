package se.fearlessgames.fear.light;

import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;
import se.fearlessgames.fear.shader.ShaderProgram;

public class DirectionalLightRenderState implements RenderState {
	public final static DirectionalLightRenderState DEFAULT = new DirectionalLightRenderState(new DefaultAmbientDirectionalLight());

	private final DirectionalLight directionalLight;

	public DirectionalLightRenderState(DirectionalLight directionalLight) {
		this.directionalLight = directionalLight;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		shaderProgram.uniform("directionalLight.location").setVector3(directionalLight.getLocation());
		shaderProgram.uniform("directionalLight.diffuse").setVector4(directionalLight.getDiffuse());
		shaderProgram.uniform("directionalLight.ambient").setVector4(directionalLight.getAmbient());
		shaderProgram.uniform("directionalLight.specular").setVector4(directionalLight.getSpecular());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}
}
