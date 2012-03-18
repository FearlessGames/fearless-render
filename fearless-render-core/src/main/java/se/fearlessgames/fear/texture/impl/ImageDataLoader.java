package se.fearlessgames.fear.texture.impl;

import java.io.IOException;
import java.io.InputStream;

public interface ImageDataLoader {
	public ImageData load(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException;
}
