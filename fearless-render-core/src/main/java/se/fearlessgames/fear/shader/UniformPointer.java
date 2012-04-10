package se.fearlessgames.fear.shader;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

import java.nio.FloatBuffer;

class UniformPointer implements Uniform {
	private final FearGl fearGl;
	private final int pointer;

	UniformPointer(FearGl fearGl, int pointer) {
		this.fearGl = fearGl;
		this.pointer = pointer;
	}

	@Override
	public void setMatrix4(FloatBuffer matrix) {
		fearGl.glUniformMatrix4(pointer, false, matrix);
	}

	@Override
	public void setMatrix3(FloatBuffer matrix) {
		fearGl.glUniformMatrix3(pointer, false, matrix);
	}

	@Override
	public void setVector3(Vector3 vector3) {
		fearGl.glUniform3f(pointer, (float) vector3.getX(), (float) vector3.getY(), (float) vector3.getZ());
	}

	@Override
	public void setVector4(Vector4 vector4) {
		fearGl.glUniform4f(pointer, (float) vector4.getX(), (float) vector4.getY(), (float) vector4.getZ(), (float) vector4.getW());
	}

	@Override
	public void setVector4(ColorRGBA color) {
		fearGl.glUniform4f(pointer, (float) color.getR(), (float) color.getG(), (float) color.getB(), (float) color.getA());
	}

	@Override
	public void setFloat(float value) {
		fearGl.glUniform1f(pointer, value);
	}

	@Override
	public void setInt(int value) {
		fearGl.glUniform1i(pointer, value);
	}
}
