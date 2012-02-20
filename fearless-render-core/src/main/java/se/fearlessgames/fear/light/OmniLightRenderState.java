package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;

public class OmniLightRenderState implements RenderState {
	public final static OmniLightRenderState DEFAULT = new OmniLightRenderState(new DefaultAmbientOmniLight());

	private final OmniLight omniLight;

	public OmniLightRenderState(OmniLight omniLight) {
		this.omniLight = omniLight;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		shaderProgram.setUniformVector3("omniLight.location", omniLight.getLocation());
		shaderProgram.setUniformVector3("omniLight.lightingColor", omniLight.getLightColor().toVector3());
		shaderProgram.setUniformVector3("omniLight.ambientColor", omniLight.getAmbientColor().toVector3());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}
}
