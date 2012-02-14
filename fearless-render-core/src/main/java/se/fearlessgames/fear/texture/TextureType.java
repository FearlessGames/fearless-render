package se.fearlessgames.fear.texture;

public enum TextureType {
	PNG("PNG");

	private final String type;

	TextureType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
