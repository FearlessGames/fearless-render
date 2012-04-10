package se.fearlessgames.fear.shader;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

import java.nio.FloatBuffer;

public interface Uniform {
	void setMatrix4(FloatBuffer matrix);

	void setMatrix3(FloatBuffer matrix);

	void setVector3(Vector3 vector3);

	void setVector4(Vector4 vector4);

	void setVector4(ColorRGBA color);

	void setFloat(float value);

	void setInt(int value);
}
