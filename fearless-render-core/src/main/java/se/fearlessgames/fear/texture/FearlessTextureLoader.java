package se.fearlessgames.fear.texture;

import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.texture.impl.ImageData;
import se.fearlessgames.fear.texture.impl.ImageDataLoader;
import se.fearlessgames.fear.texture.impl.ImageIODataLoader;
import se.fearlessgames.fear.texture.impl.PNGImageDataLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;

public class FearlessTextureLoader implements TextureLoader {
	private static final TextureMagnificationFilter TEXTURE_MAGNIFICATION_FILTER = TextureMagnificationFilter.LINEAR;
	private static final TextureMinificationFilter TEXTURE_MINIFICATION_FILTER = TextureMinificationFilter.LINEAR;

	private final TexturePixelFormat dstPixelFormat = TexturePixelFormat.GL_RGBA;

	private final FearGl fearGl;
	private final EnumMap<TextureFileType, ImageDataLoader> imageDataLoaderMap;

	public FearlessTextureLoader(FearGl fearGl) {
		this.fearGl = fearGl;
		imageDataLoaderMap = new EnumMap<TextureFileType, ImageDataLoader>(TextureFileType.class);

		imageDataLoaderMap.put(TextureFileType.PNG, new PNGImageDataLoader());
		imageDataLoaderMap.put(TextureFileType.JPEG, new ImageIODataLoader(false));
		imageDataLoaderMap.put(TextureFileType.GIF, new ImageIODataLoader(false));
		imageDataLoaderMap.put(TextureFileType.TIFF, new ImageIODataLoader(false));
		imageDataLoaderMap.put(TextureFileType.BMP, new ImageIODataLoader(false));

	}

	@Override
	public Texture load(String resourceName, TextureFileType textureFileType, InputStream is) throws IOException {
		return load(resourceName, textureFileType, is, TextureType.TEXTURE_2D);
	}

	@Override
	public Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType) throws IOException {
		return load(resourceName, textureFileType, is, textureType, false);
	}

	@Override
	public Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped) throws IOException {
		return load(resourceName, textureFileType, is, textureType, flipped, false);
	}

	@Override
	public Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped, boolean forceAlpha) throws IOException {
		return load(resourceName, textureFileType, is, textureType, flipped, forceAlpha, null);
	}

	public Texture load(String resourceName, TextureFileType textureFileType, InputStream is, TextureType textureType, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {

		if (textureFileType == TextureFileType.GUESS) {
			textureFileType = guessFromResourceName(resourceName);
		}

		if (textureFileType == null) {
			throw new FearlessTextureLoaderException("TextureFileType is null or not able to guess what type it should be for resource: " + resourceName);
		}

		ImageDataLoader imageDataLoader = imageDataLoaderMap.get(textureFileType);
		ImageData imageData = imageDataLoader.load(is, flipped, forceAlpha, transparent);

		return createFearGLTexture(resourceName, textureType, imageData);

	}


	private FearlessTexture createFearGLTexture(String resourceName, TextureType textureType, ImageData imageData) {
		fearGl.glEnable(Capability.GL_TEXTURE_2D);

		int textureId = createTextureId();

		ByteBuffer textureBuffer = imageData.getImageBufferData();
		int width = imageData.getWidth();
		int height = imageData.getHeight();
		int texWidth = imageData.getTexWidth();
		int texHeight = imageData.getTexHeight();
		boolean hasAlpha = imageData.getDepth() == 32;

		TexturePixelFormat srcPixelFormat = hasAlpha ? TexturePixelFormat.GL_RGBA : TexturePixelFormat.GL_RGB;

		int componentCount = hasAlpha ? 4 : 3;

		FearlessTexture fearlessTexture = new FearlessTexture(textureType, textureId, resourceName, width, height, texWidth, texHeight, hasAlpha);

		fearGl.glBindTexture(textureType, fearlessTexture.getId());
		fearGl.glTexParameteri(textureType, TEXTURE_MAGNIFICATION_FILTER);
		fearGl.glTexParameteri(textureType, TEXTURE_MINIFICATION_FILTER);

		fearGl.glTexImage2D(textureType,
				0,
				dstPixelFormat,
				get2Fold(width),
				get2Fold(height),
				0,
				srcPixelFormat,
				DataType.GL_UNSIGNED_BYTE,
				textureBuffer);

		return fearlessTexture;
	}

	private int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

	private TextureFileType guessFromResourceName(String resourceName) {
		String type = resourceName.toLowerCase();

		if (type.endsWith(".jpg") || type.endsWith(".jpeg")) {
			return TextureFileType.JPEG;
		}

		if (type.endsWith(".png")) {
			return TextureFileType.PNG;
		}

		if (type.endsWith(".gif")) {
			return TextureFileType.GIF;
		}

		if (type.endsWith(".tif") || type.endsWith(".tiff")) {
			return TextureFileType.TIFF;
		}

		if (type.endsWith(".bmp")) {
			return TextureFileType.BMP;
		}

		return null;

	}


	private int createTextureId() {
		return fearGl.glGenTextures();
	}
}
