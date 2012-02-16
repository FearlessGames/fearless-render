package se.fearlessgames.fear.light;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;

public class MutableSpotLight implements SpotLight {
	private Vector3 location = Vector3.ZERO;
	private Vector3 direction = Vector3.ZERO;
	private float angle;
	private float exponent;
	private ColorRGBA lightColor = new ColorRGBA(0, 0, 0, 0);

	// see http://www.ogre3d.org/tikiwiki/-Point+Light+Attenuation for values below, could be calculated per vertex bases as well
	private float constantAttenuation = 1.0f;
	private float linearAttenuation = 0.22f;
	private float quadraticAttenuation = 0.20f;

	@Override
	public Vector3 getLocation() {
		return location;
	}

	public void setLocation(Vector3 location) {
		this.location = location;
	}

	@Override
	public Vector3 getDirection() {
		return direction;
	}

	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}

	@Override
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		if (angle < 0 || (angle > 90 && angle != 180)) {
			throw new IllegalArgumentException("invalid angle.  Angle must be between 0 and 90, or 180");
		}
		this.angle = angle;
	}

	@Override
	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}

	@Override
	public ColorRGBA getLightColor() {
		return lightColor;
	}

	public void setLightColor(ColorRGBA lightColor) {
		this.lightColor = lightColor;
	}

	public float getConstantAttenuation() {
		return constantAttenuation;
	}

	public void setConstantAttenuation(float constantAttenuation) {
		this.constantAttenuation = constantAttenuation;
	}

	public float getLinearAttenuation() {
		return linearAttenuation;
	}

	public void setLinearAttenuation(float linearAttenuation) {
		this.linearAttenuation = linearAttenuation;
	}

	public float getQuadraticAttenuation() {
		return quadraticAttenuation;
	}

	public void setQuadraticAttenuation(float quadraticAttenuation) {
		this.quadraticAttenuation = quadraticAttenuation;
	}
}
