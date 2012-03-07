package se.fearlessgames.fear.texture;

import java.io.IOException;
import java.io.InputStream;

public interface TextureLoader {
	Texture loadTexture(TextureType textureType, InputStream inputStream) throws IOException;


	Texture loadTextureFlipped(TextureType textureType, InputStream inputStream) throws IOException;
}
