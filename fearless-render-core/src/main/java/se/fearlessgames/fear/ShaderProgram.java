package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.DataType;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.ShaderType;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {
	private final FearGl fearGl;
	private final int shaderProgram;
	private final EnumMap<ShaderType, Integer> shaderMap;

	private final Map<String, Integer> uniformPointerCache;
	private final Map<String, Integer> attribPointerCache;

	public ShaderProgram(FearGl fearGl) {
		this.fearGl = fearGl;
		shaderProgram = fearGl.glCreateProgram();
		if (shaderProgram == 0) {
			throw new RuntimeException("Failed to create shader program");
		}
		shaderMap = new EnumMap<ShaderType, Integer>(ShaderType.class);
		uniformPointerCache = new HashMap<String, Integer>();
		attribPointerCache = new HashMap<String, Integer>();
	}

	public int getShaderProgram() {
		return shaderProgram;
	}

	public void loadAndCompile(String filename, ShaderType type) {
		int shader = fearGl.glCreateShader(type);

		if (shader == 0) {
			throw new RuntimeException("Failed to create " + type.name() + " shader");
		}

		String code = getFileContent(filename);
		fearGl.glShaderSource(shader, code);
		fearGl.glCompileShader(shader);

		printLogInfo(shader);

		shaderMap.put(type, shader);
	}

	public void attachToProgram(ShaderType type) {
		Integer shader = shaderMap.get(type);
		if (shader != null && shader != 0) {
			fearGl.glAttachShader(shaderProgram, shader);
			fearGl.glLinkProgram(shaderProgram);
			fearGl.glValidateProgram(shaderProgram);
			printLogInfo(shaderProgram);
		}
	}

	public void setUniformMatrix4(String name, FloatBuffer matrix) {
		int pointer = getUniformPointer(name);
		fearGl.glUniformMatrix4(pointer, false, matrix);
	}

	public void setUniformMatrix3(String name, FloatBuffer matrix) {
		int pointer = getUniformPointer(name);
		fearGl.glUniformMatrix3(pointer, false, matrix);
	}

	public void setUniformVector3(String name, Vector3 vector3) {
		int pointer = getUniformPointer(name);
		fearGl.glUniform3f(pointer, (float) vector3.getX(), (float) vector3.getY(), (float) vector3.getZ());
	}

	public void setUniformVector4(String name, Vector4 vector4) {
		int pointer = getUniformPointer(name);
		fearGl.glUniform4f(pointer, (float) vector4.getX(), (float) vector4.getY(), (float) vector4.getZ(), (float) vector4.getW());
	}

	public void setUniformVector4(String name, ColorRGBA color) {
		int pointer = getUniformPointer(name);
		fearGl.glUniform4f(pointer, (float) color.getR(), (float) color.getG(), (float) color.getB(), (float) color.getA());
	}


	public void setUniformFloat(String name, float value) {
		int pointer = getUniformPointer(name);
		fearGl.glUniform1f(pointer, value);
	}

	public void setUniformInt(String name, int value) {
		int pointer = getUniformPointer(name);
		fearGl.glUniform1i(pointer, value);
	}

	private int getUniformPointer(String name) {
		int pointer;
		if (!uniformPointerCache.containsKey(name)) {
			uniformPointerCache.put(name, fearGl.glGetUniformLocation(shaderProgram, name));
		}
		pointer = uniformPointerCache.get(name);
		return pointer;
	}

	public void setVertexAttribute(String name, int size, int stride, int offset) {

		int pointer;
		if (!attribPointerCache.containsKey(name)) {
			attribPointerCache.put(name, fearGl.glGetAttribLocation(shaderProgram, name));
		}
		pointer = attribPointerCache.get(name);

		fearGl.glEnableVertexAttribArray(pointer);
		fearGl.glVertexAttribPointer(pointer, size, DataType.GL_FLOAT, true, stride, offset);

	}


	private String getFileContent(String filename) {
		String line;
		StringBuilder code = new StringBuilder();
		try {

			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				code.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException("Fail reading shading code from file");
		}

		return code.toString();
	}

	private void printLogInfo(int obj) {
		String out = fearGl.glGetShaderInfoLog(obj, 1024);
		System.out.println("Info log: " + out);
	}


}

