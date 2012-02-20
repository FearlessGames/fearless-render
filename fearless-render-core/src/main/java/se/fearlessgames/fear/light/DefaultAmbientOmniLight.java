package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public class DefaultAmbientOmniLight implements OmniLight {
	private final Vector3 location = new Vector3(0f, 0f, 0f);
	private final ColorRGBA lightColor = new ColorRGBA(0f, 0f, 0f, 0f);
	private final ColorRGBA ambientColor = new ColorRGBA(1f, 1f, 1f, 0f);

	@Override
	public Vector3 getLocation() {
		return location;
	}

	@Override
	public ColorRGBA getLightColor() {
		return lightColor;
	}

	@Override
	public ColorRGBA getAmbientColor() {
		return ambientColor;
	}
}
