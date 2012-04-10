package se.fearlessgames.fear.shader;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

import java.nio.FloatBuffer;

class NOPUniformPointer implements Uniform {

	@Override
	public void setMatrix4(FloatBuffer matrix) {
	}

	@Override
	public void setMatrix3(FloatBuffer matrix) {
	}

	@Override
	public void setVector3(Vector3 vector3) {
	}

	@Override
	public void setVector4(Vector4 vector4) {
	}

	@Override
	public void setVector4(ColorRGBA color) {
	}

	@Override
	public void setFloat(float value) {
	}

	@Override
	public void setInt(int value) {
	}
}
