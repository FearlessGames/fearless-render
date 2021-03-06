package se.fearlessgames.fear.math;

public class MathUtils {
	public static final double EPSILON = 2.220446049250313E-16d;
	public static final double PI = Math.PI;
	public static final double HALF_PI = 0.5 * PI;
	public static final double ZERO_TOLERANCE = 0.0001;
	public static final double TWO_PI = 2.0 * PI;
	public static final double INV_PI = 1.0 / PI;
	public static final double DEG_TO_RAD = PI / 180.0;
	public static final double PI_OVER_360 = (PI / 360.0d);

	public static double inverseSqrt(double a) {
		return 1 / Math.sqrt(a);
	}

	public static double cos(double a) {
		return Math.cos(a);
	}

	public static double sin(double a) {
		return Math.sin(a);
	}

	public static double sqrt(double a) {
		return Math.sqrt(a);
	}

	public static double acos(double a) {
		return Math.acos(a);
	}

	public static double tan(double v) {
		return Math.tan(v);
	}
}
