package se.fearlessgames.fear.material;

import se.fearlessgames.fear.ColorRGBA;

public class Material {
	private final ColorRGBA ambient;
	private final ColorRGBA diffuse;
	private final ColorRGBA specular;
	private final ColorRGBA emissive;
	private final float shininess;

	public Material(ColorRGBA ambient, ColorRGBA diffuse, ColorRGBA specular, ColorRGBA emissive, float shininess) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.emissive = emissive;
		this.shininess = shininess;
	}

	public ColorRGBA getAmbient() {
		return ambient;
	}

	public ColorRGBA getDiffuse() {
		return diffuse;
	}

	public ColorRGBA getSpecular() {
		return specular;
	}

	public ColorRGBA getEmissive() {
		return emissive;
	}

	public float getShininess() {
		return shininess;
	}
}
