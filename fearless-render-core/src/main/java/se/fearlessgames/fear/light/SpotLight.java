package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public interface SpotLight {
	public Vector3 getLocation();

	public Vector3 getDirection();

	public ColorRGBA getLightColor();

	public float getAngle();

	public float getExponent();

	public float getConstantAttenuation();

	public float getLinearAttenuation();

	public float getQuadraticAttenuation();

}
