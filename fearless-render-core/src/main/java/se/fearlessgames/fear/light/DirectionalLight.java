package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public interface DirectionalLight {
	public Vector3 getLocation();

	public ColorRGBA getDiffuse();

	public ColorRGBA getAmbient();

	public ColorRGBA getSpecular();
}
