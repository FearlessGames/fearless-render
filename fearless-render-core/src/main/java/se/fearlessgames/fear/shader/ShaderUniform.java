package se.fearlessgames.fear.shader;

public enum ShaderUniform {
	MODEL_VIEW_PROJECTION_MATRIX("modelViewProjectionMatrix"),
	PROJECTION_MATRIX("projectionMatrix"),
	MODEL_VIEW_MATRIX("modelViewMatrix"),
	MODEL_MATRIX("modelMatrix"),
	NORMAL_MATRIX("normalMatrix");

	private final String name;

	ShaderUniform(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
