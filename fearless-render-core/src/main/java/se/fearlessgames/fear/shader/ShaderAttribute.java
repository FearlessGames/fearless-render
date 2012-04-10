package se.fearlessgames.fear.shader;

public enum ShaderAttribute {
	VERTEX("vertex"),
	NORMAL("normal"),
	COLOR("color"),
	TEXTURE_COORD("textureCoord");

	private final String name;

	ShaderAttribute(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
