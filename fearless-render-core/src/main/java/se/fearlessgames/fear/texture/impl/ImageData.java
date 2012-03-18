package se.fearlessgames.fear.texture.impl;

import java.nio.ByteBuffer;

public interface ImageData {
	public int getDepth();

	public int getWidth();

	public int getHeight();

	public int getTexWidth();

	public int getTexHeight();

	public ByteBuffer getImageBufferData();
}
