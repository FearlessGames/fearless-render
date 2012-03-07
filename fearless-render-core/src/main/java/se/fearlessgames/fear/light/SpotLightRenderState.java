package se.fearlessgames.fear.light;

import org.apache.commons.math.util.FastMath;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;

import java.util.List;

public class SpotLightRenderState implements RenderState {
	private final List<SpotLight> spotLights;

	public SpotLightRenderState(List<SpotLight> spotLights) {
		this.spotLights = spotLights;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		if (spotLights.isEmpty()) {
			return;
		}

		int size = spotLights.size();
		int index = 0;
		for (SpotLight spotLight : spotLights) {
			shaderProgram.setUniformVector3(getVar(index, "location"), spotLight.getLocation());
			shaderProgram.setUniformVector3(getVar(index, "lightingColor"), spotLight.getLightColor().toVector3());
			shaderProgram.setUniformVector3(getVar(index, "direction"), spotLight.getDirection());

			shaderProgram.setUniformFloat(getVar(index, "exponent"), spotLight.getExponent());
			shaderProgram.setUniformFloat(getVar(index, "spotCosCutoff"), (float) FastMath.cos(spotLight.getAngle()));
			shaderProgram.setUniformFloat(getVar(index, "constantAttenuation"), spotLight.getConstantAttenuation());
			shaderProgram.setUniformFloat(getVar(index, "linearAttenuation"), spotLight.getLinearAttenuation());
			shaderProgram.setUniformFloat(getVar(index, "quadraticAttenuation"), spotLight.getQuadraticAttenuation());

			index++;
		}

		shaderProgram.setUniformInt("nrOfSpotLights", size);

	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}

	private String getVar(int index, String name) {
		return String.format("%s[%d].%s", "spotLights", index, name);

	}
}
