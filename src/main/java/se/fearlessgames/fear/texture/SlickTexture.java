package se.fearlessgames.fear.texture;

public class SlickTexture implements Texture {
	private final org.newdawn.slick.opengl.Texture internalTexture;

	public SlickTexture(org.newdawn.slick.opengl.Texture internalTexture) {
		this.internalTexture = internalTexture;
	}

	@Override
	public int getId() {
		return internalTexture.getTextureID();
	}
}
