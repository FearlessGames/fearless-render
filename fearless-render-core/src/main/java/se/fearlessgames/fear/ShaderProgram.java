package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.DataType;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.ShaderType;

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
		int pointer;
		if (!uniformPointerCache.containsKey(name)) {
			uniformPointerCache.put(name, fearGl.glGetUniformLocation(shaderProgram, name));
		}
		pointer = uniformPointerCache.get(name);

		fearGl.glUniformMatrix4(pointer, false, matrix);
	}

	public void setVertexAttribute(String name, int size, int stride, int offset) {

		int pointer;
		if (!attribPointerCache.containsKey(name)) {
			uniformPointerCache.put(name, fearGl.glGetAttribLocation(shaderProgram, name));
		}
		pointer = uniformPointerCache.get(name);

		fearGl.glVertexAttribPointer(pointer, size, DataType.GL_FLOAT, true, stride, offset);
		fearGl.glEnableVertexAttribArray(pointer);
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
