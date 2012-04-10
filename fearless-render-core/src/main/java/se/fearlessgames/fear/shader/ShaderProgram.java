package se.fearlessgames.fear.shader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.ShaderType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final FearGl fearGl;
	private final int shaderProgram;
	private final EnumMap<ShaderType, String> shaderMap;

	private final Map<String, Uniform> uniformPointerCache;
	private final Map<String, Attribute> attributePointerCache;

	public ShaderProgram(FearGl fearGl) {
		this.fearGl = fearGl;
		shaderProgram = fearGl.glCreateProgram();
		if (shaderProgram == 0) {
			throw new RuntimeException("Failed to create shader program");
		}
		shaderMap = new EnumMap<ShaderType, String>(ShaderType.class);
		uniformPointerCache = new HashMap<String, Uniform>();
		attributePointerCache = new HashMap<String, Attribute>();
	}

	public int getShaderProgram() {
		return shaderProgram;
	}

	public void load(String filename, ShaderType type) {
		int shader = fearGl.glCreateShader(type);
		if (shader == 0) {
			throw new RuntimeException("Failed to create " + type.name() + " shader");
		}

		String code = getFileContent(filename);
		shaderMap.put(type, code);

	}

	public void compile() {
		for (Map.Entry<ShaderType, String> entry : shaderMap.entrySet()) {
			ShaderType type = entry.getKey();
			String code = entry.getValue();

			int shader = fearGl.glCreateShader(type);
			if (shader == 0) {
				throw new RuntimeException("Failed to create " + type.name() + " shader");
			}

			fearGl.glShaderSource(shader, code);
			fearGl.glCompileShader(shader);

			printLogInfo(shader);

			fearGl.glAttachShader(shaderProgram, shader);
		}

		fearGl.glLinkProgram(shaderProgram);
		fearGl.glValidateProgram(shaderProgram);

		indexDefaultUniforms();
		indexDefaultAttributes();

	}

	private void indexDefaultUniforms() {
		for (ShaderUniform shaderUniform : ShaderUniform.values()) {
			uniform(shaderUniform);
		}
	}

	private void indexDefaultAttributes() {
		for (ShaderAttribute shaderAttribute : ShaderAttribute.values()) {
			attribute(shaderAttribute);
		}
	}

	public Uniform uniform(ShaderUniform uniform) {
		return uniform(uniform.getName());
	}

	public Uniform uniform(String name) {
		if (!uniformPointerCache.containsKey(name)) {
			int pointer = fearGl.glGetUniformLocation(shaderProgram, name);
			if (pointer == -1) {
				uniformPointerCache.put(name, new NOPUniformPointer());
			} else {
				uniformPointerCache.put(name, new UniformPointer(fearGl, pointer));
			}
		}

		return uniformPointerCache.get(name);
	}

	public Attribute attribute(ShaderAttribute attribute) {
		String name = attribute.getName();
		if (!attributePointerCache.containsKey(name)) {
			int pointer = fearGl.glGetAttribLocation(shaderProgram, name);
			if (pointer == -1) {
				attributePointerCache.put(name, new NOPAttributePointer());
			} else {
				attributePointerCache.put(name, new AttributePointer(fearGl, pointer));
			}
		}
		return attributePointerCache.get(name);
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
		log.info("Shader compile result: " + stripLineBreak(out));
	}

	private String stripLineBreak(String out) {
		if (out == null) {
			return null;
		}
		out = out.trim();
		if (out.endsWith("\n")) {
			return out.substring(0, out.indexOf('\n'));
		}
		return out;
	}


}

