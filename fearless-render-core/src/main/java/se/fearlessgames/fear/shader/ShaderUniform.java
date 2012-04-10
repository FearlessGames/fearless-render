package se.fearlessgames.fear.shader;

public enum ShaderUniform {
	MODEL_VIEW_MATRIX("modelViewMatrix"),
	PROJECTION_MATRIX("projectionMatrix"),
	NORMAL_MATRIX("normalMatrix");

	private final String name;

	ShaderUniform(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
