package se.fearlessgames.fear.texture.impl;

import java.nio.ByteBuffer;

public class ImageDataImpl implements ImageData {
	private final int width;
	private final int height;
	private final int texWidth;
	private final int texHeight;
	private final int depth;
	private final ByteBuffer buffer;

	public ImageDataImpl(int width, int height, int texWidth, int texHeight, int depth, ByteBuffer buffer) {
		this.width = width;
		this.height = height;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.depth = depth;
		this.buffer = buffer;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getTexWidth() {
		return texWidth;
	}

	@Override
	public int getTexHeight() {
		return texHeight;
	}

	@Override
	public ByteBuffer getImageBufferData() {
		return buffer;
	}
}
