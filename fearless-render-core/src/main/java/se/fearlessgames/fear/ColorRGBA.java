package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

public class ColorRGBA {
	private final double r;
	private final double g;
	private final double b;
	private final double a;

	public ColorRGBA(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public double getR() {
		return r;
	}

	public double getG() {
		return g;
	}

	public double getB() {
		return b;
	}

	public double getA() {
		return a;
	}

	public Vector3 toVector3() {
		return new Vector3(r, g, b);
	}

	public Vector4 toVector4() {
		return new Vector4(r, g, b, a);
	}
}
