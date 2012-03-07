package se.fearlessgames.fear.texture;

import java.io.IOException;
import java.io.InputStream;

public class TextureLoaderImpl implements TextureLoader {

	@Override
	public Texture loadTexture(TextureType textureType, InputStream inputStream) throws IOException {
		org.newdawn.slick.opengl.Texture texture = org.newdawn.slick.opengl.TextureLoader.getTexture(textureType.getType(), inputStream);
		return new SlickTexture(texture);
	}

	@Override
	public Texture loadTextureFlipped(TextureType textureType, InputStream inputStream) throws IOException {
		org.newdawn.slick.opengl.Texture texture = org.newdawn.slick.opengl.TextureLoader.getTexture(textureType.getType(), inputStream, true);
		return new SlickTexture(texture);
	}
}
