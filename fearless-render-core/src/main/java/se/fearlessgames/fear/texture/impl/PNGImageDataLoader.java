package se.fearlessgames.fear.texture.impl;

import java.io.IOException;
import java.io.InputStream;

public class PNGImageDataLoader implements ImageDataLoader {


	@Override
	public ImageData load(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
		PNGStreamDecoder pngStream = new PNGStreamDecoder(is);
		pngStream.decodeStream(flipped, forceAlpha, transparent);
		return new ImageDataImpl(pngStream.getWidth(), pngStream.getHeight(), pngStream.getTexWidth(), pngStream.getTexHeight(), pngStream.getBitDepth(), pngStream.getBuffer());
	}

}
