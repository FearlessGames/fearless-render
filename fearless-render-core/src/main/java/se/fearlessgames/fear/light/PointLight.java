package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public interface PointLight {
	public Vector3 getLocation();

	public ColorRGBA getLightColor();

	public ColorRGBA getAmbientColor();
}
