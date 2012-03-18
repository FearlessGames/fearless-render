package se.fearlessgames.fear.texture;

import se.fearlessgames.fear.gl.TextureType;

import java.io.IOException;
import java.io.InputStream;

public interface TextureLoader {
	Texture load(String resourceName, TextureFileType textureFileType, InputStream is) throws IOException;

	Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType) throws IOException;

	Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped) throws IOException;

	Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped, boolean forceAlpha) throws IOException;

	Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException;

}
