package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.ShaderType;

import java.io.BufferedReader;
import java.io.FileReader;

public class Shaders {
	private final FearGl fearGl;

	public Shaders(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public int createProgram() {
		int shaderProgram = fearGl.glCreateProgram();
		if (shaderProgram == 0) {
			throw new RuntimeException("Failed to create shader program");
		}
		return shaderProgram;
	}

	public int loadAndCompile(String filename, ShaderType type) {
		int shader = fearGl.glCreateShader(type);

		if (shader == 0) {
			throw new RuntimeException("Failed to create " + type.name() + " shader");
		}

		String code = getFileContent(filename);
		fearGl.glShaderSource(shader, code);
		fearGl.glCompileShader(shader);

		printLogInfo(shader);

		return shader;

	}

	public void attachToProgram(int shaderProgram, int shader) {
		if (shader != 0) {
			fearGl.glAttachShader(shaderProgram, shader);
		}

		fearGl.glLinkProgram(shaderProgram);
		fearGl.glValidateProgram(shaderProgram);
		printLogInfo(shaderProgram);
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
