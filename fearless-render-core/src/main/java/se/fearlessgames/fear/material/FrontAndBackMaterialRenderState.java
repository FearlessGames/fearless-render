package se.fearlessgames.fear.material;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.RenderState;

public class FrontAndBackMaterialRenderState implements RenderState {

	private final Material frontMaterial;
	private final Material backMaterial;

	public FrontAndBackMaterialRenderState(Material frontMaterial, Material backMaterial) {
		this.frontMaterial = frontMaterial;
		this.backMaterial = backMaterial;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		setShaderData(shaderProgram, "frontMaterial", frontMaterial);
		setShaderData(shaderProgram, "backMaterial", backMaterial);
	}

	private void setShaderData(ShaderProgram shaderProgram, String name, Material material) {
		setColor(shaderProgram, name + ".ambient", material.getAmbient());
		setColor(shaderProgram, name + ".diffuse", material.getDiffuse());
		setColor(shaderProgram, name + ".emissive", material.getEmissive());
		setColor(shaderProgram, name + ".specular", material.getSpecular());
		shaderProgram.setUniformFloat(name + ".shininess", material.getShininess());
	}

	private void setColor(ShaderProgram shaderProgram, String name, ColorRGBA color) {
		shaderProgram.setUniformVector3(name, color.toVector3());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {

	}
}
