package se.fearlessgames.fear.light;

import org.apache.commons.math.util.FastMath;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;
import se.fearlessgames.fear.shader.ShaderProgram;

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
			shaderProgram.uniform(getVar(index, "location")).setVector3(spotLight.getLocation());
			shaderProgram.uniform(getVar(index, "lightingColor")).setVector3(spotLight.getLightColor().toVector3());
			shaderProgram.uniform(getVar(index, "direction")).setVector3(spotLight.getDirection());

			shaderProgram.uniform(getVar(index, "exponent")).setFloat(spotLight.getExponent());
			shaderProgram.uniform(getVar(index, "spotCosCutoff")).setFloat((float) FastMath.cos(spotLight.getAngle()));
			shaderProgram.uniform(getVar(index, "constantAttenuation")).setFloat(spotLight.getConstantAttenuation());
			shaderProgram.uniform(getVar(index, "linearAttenuation")).setFloat(spotLight.getLinearAttenuation());
			shaderProgram.uniform(getVar(index, "quadraticAttenuation")).setFloat(spotLight.getQuadraticAttenuation());

			index++;
		}

		shaderProgram.uniform("nrOfSpotLights").setInt(size);
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}

	private String getVar(int index, String name) {
		return String.format("%s[%d].%s", "spotLights", index, name);

	}
}
