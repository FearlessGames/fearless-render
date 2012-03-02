package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public class DefaultAmbientDirectionalLight implements DirectionalLight {
	private final Vector3 location = new Vector3(0f, 0f, 0f);
	private final ColorRGBA lightColor = new ColorRGBA(0f, 0f, 0f, 0f);
	private final ColorRGBA ambientColor = new ColorRGBA(1f, 1f, 1f, 0f);
	private final ColorRGBA specularColor = new ColorRGBA(1f, 1f, 1f, 1f);

	@Override
	public Vector3 getLocation() {
		return location;
	}

	@Override
	public ColorRGBA getDiffuse() {
		return lightColor;
	}

	@Override
	public ColorRGBA getAmbient() {
		return ambientColor;
	}

	@Override
	public ColorRGBA getSpecular() {
		return specularColor;
	}
}
