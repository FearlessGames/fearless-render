package se.fearlessgames.fear.light;

import org.apache.commons.math.util.FastMath;
import se.fearlessgames.fear.ShaderProgram;

import java.util.List;

public class SpotlightShaderPopulator {
	private final ShaderProgram shader;
	private final List<SpotLight> spotLights;
	private final String spotLightsName;
	private final String nrOfSpotLightsName;

	public SpotlightShaderPopulator(ShaderProgram shaderProgram, List<SpotLight> spotLights, String spotLightsName, String nrOfSpotLightsName) {
		this.shader = shaderProgram;
		this.spotLights = spotLights;
		this.spotLightsName = spotLightsName;
		this.nrOfSpotLightsName = nrOfSpotLightsName;
	}

	public void populate() {
		if (spotLights.isEmpty()) {
			return;
		}

		int size = spotLights.size();
		int index = 0;
		for (SpotLight spotLight : spotLights) {
			shader.setUniformVector3(getVar(index, "location"), spotLight.getLocation());
			shader.setUniformVector3(getVar(index, "lightingColor"), spotLight.getLightColor().toVector3());
			shader.setUniformVector3(getVar(index, "direction"), spotLight.getDirection());

			shader.setUniformFloat(getVar(index, "exponent"), spotLight.getExponent());
			shader.setUniformFloat(getVar(index, "spotCosCutoff"), (float) FastMath.cos(spotLight.getAngle()));
			shader.setUniformFloat(getVar(index, "constantAttenuation"), spotLight.getConstantAttenuation());
			shader.setUniformFloat(getVar(index, "linearAttenuation"), spotLight.getLinearAttenuation());
			shader.setUniformFloat(getVar(index, "quadraticAttenuation"), spotLight.getQuadraticAttenuation());

			index++;
		}

		shader.setUniformInt(nrOfSpotLightsName, size);

	}

	private String getVar(int index, String name) {
		return String.format("%s[%d].%s", spotLightsName, index, name);

	}
}
