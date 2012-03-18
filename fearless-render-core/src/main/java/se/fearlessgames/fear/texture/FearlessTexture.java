package se.fearlessgames.fear.texture;

import se.fearlessgames.fear.gl.TextureType;

public class FearlessTexture implements Texture {
	private final TextureType textureType;
	private final int textureId;
	private final String resourceName;
	private final int width;
	private final int height;
	private final int texWidth;
	private final int texHeight;
	private final boolean hasAlpha;

	public FearlessTexture(TextureType textureType, int textureId, String resourceName, int width, int height, int texWidth, int texHeight, boolean hasAlpha) {
		this.textureType = textureType;
		this.textureId = textureId;
		this.resourceName = resourceName;
		this.width = width;
		this.height = height;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.hasAlpha = hasAlpha;
	}

	@Override
	public int getId() {
		return textureId;
	}

	public TextureType getTextureType() {
		return textureType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTexWidth() {
		return texWidth;
	}

	public int getTexHeight() {
		return texHeight;
	}

	public boolean hasAlpha() {
		return hasAlpha;
	}
}
