package se.fearlessgames.fear.texture.impl;

import se.fearlessgames.fear.BufferUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGStreamDecoder {
	private static final byte[] SIGNATURE = {(byte) 137, 80, 78, 71, 13, 10, 26, 10};
	private static final int IHDR = 0x49484452;
	private static final int PLTE = 0x504C5445;
	private static final int tRNS = 0x74524E53;
	private static final int IDAT = 0x49444154;
	private static final int IEND = 0x49454E44;
	private static final byte COLOR_GREYSCALE = 0;
	private static final byte COLOR_TRUECOLOR = 2;
	private static final byte COLOR_INDEXED = 3;
	private static final byte COLOR_GREYALPHA = 4;
	private static final byte COLOR_TRUEALPHA = 6;

	private final CRC32 crc = new CRC32();
	private final byte[] tempBuffer = new byte[4096];
	private final InputStream input;

	private int chunkLength;
	private int chunkType;
	private int chunkRemaining;
	private int width;
	private int height;
	private int colorType;
	private int bytesPerPixel;
	private byte[] palette;
	private byte[] paletteA;
	private byte[] transPixel;
	private int bitDepth;
	private int texWidth;
	private int texHeight;

	private ByteBuffer buffer;

	public PNGStreamDecoder(InputStream input) {
		this.input = input;
	}

	public int getBitDepth() {
		return bitDepth;
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

	public ByteBuffer getBuffer() {
		return buffer;
	}

	private void init() throws IOException {
		int read = input.read(tempBuffer, 0, SIGNATURE.length);
		if (read != SIGNATURE.length || !checkSignatur(tempBuffer)) {
			throw new IOException("Not a valid PNG file");
		}

		openChunk(IHDR);
		readIHDR();
		closeChunk();

		searchIDAT:
		for (; ; ) {
			openChunk();
			switch (chunkType) {
				case IDAT:
					break searchIDAT;
				case PLTE:
					readPLTE();
					break;
				case tRNS:
					readtRNS();
					break;
			}
			closeChunk();
		}
	}

	private boolean hasAlpha() {
		return colorType == COLOR_TRUEALPHA ||
				paletteA != null || transPixel != null;
	}


	private boolean isRGB() {
		return colorType == COLOR_TRUEALPHA ||
				colorType == COLOR_TRUECOLOR ||
				colorType == COLOR_INDEXED;
	}

	private void decode(ByteBuffer buffer, int stride, boolean flip) throws IOException {
		final int offset = buffer.position();
		byte[] curLine = new byte[width * bytesPerPixel + 1];
		byte[] prevLine = new byte[width * bytesPerPixel + 1];

		final Inflater inflater = new Inflater();
		try {
			for (int yIndex = 0; yIndex < height; yIndex++) {
				int y = yIndex;
				if (flip) {
					y = height - 1 - yIndex;
				}

				readChunkUnzip(inflater, curLine, 0, curLine.length);
				unfilter(curLine, prevLine);

				buffer.position(offset + y * stride);

				switch (colorType) {
					case COLOR_TRUECOLOR:
					case COLOR_TRUEALPHA:
						copy(buffer, curLine);
						break;
					case COLOR_INDEXED:
						copyExpand(buffer, curLine);
						break;
					default:
						throw new UnsupportedOperationException("Not yet implemented");
				}

				byte[] tmp = curLine;
				curLine = prevLine;
				prevLine = tmp;
			}
		} finally {
			inflater.end();
		}

		bitDepth = hasAlpha() ? 32 : 24;
	}


	private void copyExpand(ByteBuffer buffer, byte[] curLine) {
		for (int i = 1; i < curLine.length; i++) {
			int v = curLine[i] & 255;

			int index = v * 3;
			for (int j = 0; j < 3; j++) {
				buffer.put(palette[index + j]);
			}

			if (hasAlpha()) {
				if (paletteA != null) {
					buffer.put(paletteA[v]);
				} else {
					buffer.put((byte) 255);
				}
			}
		}
	}


	private void copy(ByteBuffer buffer, byte[] curLine) {
		buffer.put(curLine, 1, curLine.length - 1);
	}


	private void unfilter(byte[] curLine, byte[] prevLine) throws IOException {
		switch (curLine[0]) {
			case 0: // none
				break;
			case 1:
				unfilterSub(curLine);
				break;
			case 2:
				unfilterUp(curLine, prevLine);
				break;
			case 3:
				unfilterAverage(curLine, prevLine);
				break;
			case 4:
				unfilterPaeth(curLine, prevLine);
				break;
			default:
				throw new IOException("invalide filter type in scanline: " + curLine[0]);
		}
	}

	private void unfilterSub(byte[] curLine) {
		final int bpp = this.bytesPerPixel;
		final int lineSize = width * bpp;

		for (int i = bpp + 1; i <= lineSize; ++i) {
			curLine[i] += curLine[i - bpp];
		}
	}

	private void unfilterUp(byte[] curLine, byte[] prevLine) {
		final int bpp = this.bytesPerPixel;
		final int lineSize = width * bpp;

		for (int i = 1; i <= lineSize; ++i) {
			curLine[i] += prevLine[i];
		}
	}

	private void unfilterAverage(byte[] curLine, byte[] prevLine) {
		final int bpp = this.bytesPerPixel;
		final int lineSize = width * bpp;

		int i;
		for (i = 1; i <= bpp; ++i) {
			curLine[i] += (byte) ((prevLine[i] & 0xFF) >>> 1);
		}
		for (; i <= lineSize; ++i) {
			curLine[i] += (byte) (((prevLine[i] & 0xFF) + (curLine[i - bpp] & 0xFF)) >>> 1);
		}
	}

	private void unfilterPaeth(byte[] curLine, byte[] prevLine) {
		final int bpp = this.bytesPerPixel;
		final int lineSize = width * bpp;

		int i;
		for (i = 1; i <= bpp; ++i) {
			curLine[i] += prevLine[i];
		}
		for (; i <= lineSize; ++i) {
			int a = curLine[i - bpp] & 255;
			int b = prevLine[i] & 255;
			int c = prevLine[i - bpp] & 255;
			int p = a + b - c;
			int pa = p - a;
			if (pa < 0) {
				pa = -pa;
			}
			int pb = p - b;
			if (pb < 0) {
				pb = -pb;
			}
			int pc = p - c;
			if (pc < 0) {
				pc = -pc;
			}
			if (pa <= pb && pa <= pc) {
				c = a;
			} else if (pb <= pc) {
				c = b;
			}
			curLine[i] += (byte) c;
		}
	}

	private void readIHDR() throws IOException {
		checkChunkLength(13);
		readChunk(tempBuffer, 0, 13);
		width = readInt(tempBuffer, 0);
		height = readInt(tempBuffer, 4);

		if (tempBuffer[8] != 8) {
			throw new IOException("Unsupported bit depth");
		}

		colorType = tempBuffer[9] & 255;
		switch (colorType) {
			case COLOR_GREYSCALE:
				bytesPerPixel = 1;
				break;
			case COLOR_TRUECOLOR:
				bytesPerPixel = 3;
				break;
			case COLOR_TRUEALPHA:
				bytesPerPixel = 4;
				break;
			case COLOR_INDEXED:
				bytesPerPixel = 1;
				break;
			default:
				throw new IOException("unsupported color format");
		}

		if (tempBuffer[10] != 0) {
			throw new IOException("unsupported compression method");
		}
		if (tempBuffer[11] != 0) {
			throw new IOException("unsupported filtering method");
		}
		if (tempBuffer[12] != 0) {
			throw new IOException("unsupported interlace method");
		}
	}

	private void readPLTE() throws IOException {
		int paletteEntries = chunkLength / 3;
		if (paletteEntries < 1 || paletteEntries > 256 || (chunkLength % 3) != 0) {
			throw new IOException("PLTE chunk has wrong length");
		}

		palette = new byte[paletteEntries * 3];
		readChunk(palette, 0, palette.length);
	}

	private void readtRNS() throws IOException {
		switch (colorType) {
			case COLOR_GREYSCALE:
				checkChunkLength(2);
				transPixel = new byte[2];
				readChunk(transPixel, 0, 2);
				break;
			case COLOR_TRUECOLOR:
				checkChunkLength(6);
				transPixel = new byte[6];
				readChunk(transPixel, 0, 6);
				break;
			case COLOR_INDEXED:
				if (palette == null) {
					throw new IOException("tRNS chunk without PLTE chunk");
				}
				paletteA = new byte[palette.length / 3];
				// initialise default palette values
				for (int i = 0; i < paletteA.length; i++) {
					paletteA[i] = (byte) 255;
				}
				readChunk(paletteA, 0, paletteA.length);
				break;
			default:
				// just ignore it
		}
	}

	private void closeChunk() throws IOException {
		if (chunkRemaining > 0) {
			// just skip the rest and the CRC
			input.skip(chunkRemaining + 4);
		} else {
			readFully(tempBuffer, 0, 4);
			int expectedCrc = readInt(tempBuffer, 0);
			int computedCrc = (int) crc.getValue();
			if (computedCrc != expectedCrc) {
				throw new IOException("Invalid CRC");
			}
		}
		chunkRemaining = 0;
		chunkLength = 0;
		chunkType = 0;
	}

	private void openChunk() throws IOException {
		readFully(tempBuffer, 0, 8);
		chunkLength = readInt(tempBuffer, 0);
		chunkType = readInt(tempBuffer, 4);
		chunkRemaining = chunkLength;
		crc.reset();
		crc.update(tempBuffer, 4, 4);   // only chunkType
	}

	private void openChunk(int expected) throws IOException {
		openChunk();
		if (chunkType != expected) {
			throw new IOException("Expected chunk: " + Integer.toHexString(expected));
		}
	}

	private void checkChunkLength(int expected) throws IOException {
		if (chunkLength != expected) {
			throw new IOException("Chunk has wrong size");
		}
	}

	private int readChunk(byte[] buffer, int offset, int length) throws IOException {
		if (length > chunkRemaining) {
			length = chunkRemaining;
		}
		readFully(buffer, offset, length);
		crc.update(buffer, offset, length);
		chunkRemaining -= length;
		return length;
	}

	private void refillInflater(Inflater inflater) throws IOException {
		while (chunkRemaining == 0) {
			closeChunk();
			openChunk(IDAT);
		}
		int read = readChunk(tempBuffer, 0, tempBuffer.length);
		inflater.setInput(tempBuffer, 0, read);
	}

	private void readChunkUnzip(Inflater inflater, byte[] buffer, int offset, int length) throws IOException {
		try {
			do {
				int read = inflater.inflate(buffer, offset, length);
				if (read <= 0) {
					if (inflater.finished()) {
						throw new EOFException();
					}
					if (inflater.needsInput()) {
						refillInflater(inflater);
					} else {
						throw new IOException("Can't inflate " + length + " bytes");
					}
				} else {
					offset += read;
					length -= read;
				}
			} while (length > 0);
		} catch (DataFormatException ex) {
			IOException io = new IOException("inflate error");
			io.initCause(ex);

			throw io;
		}
	}

	private void readFully(byte[] buffer, int offset, int length) throws IOException {
		do {
			int read = input.read(buffer, offset, length);
			if (read < 0) {
				throw new EOFException();
			}
			offset += read;
			length -= read;
		} while (length > 0);
	}


	private int readInt(byte[] buffer, int offset) {
		return
				((buffer[offset]) << 24) |
						((buffer[offset + 1] & 255) << 16) |
						((buffer[offset + 2] & 255) << 8) |
						((buffer[offset + 3] & 255));
	}


	private boolean checkSignatur(byte[] buffer) {
		for (int i = 0; i < SIGNATURE.length; i++) {
			if (buffer[i] != SIGNATURE[i]) {
				return false;
			}
		}
		return true;
	}


	public void decodeStream(boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
		if (transparent != null) {
			forceAlpha = true;
		}

		init();

		if (!isRGB()) {
			throw new IOException("Only RGB formatted images are supported by the PNGLoader");
		}

		texWidth = get2Fold(width);
		texHeight = get2Fold(height);

		int perPixel = hasAlpha() ? 4 : 3;

		// Get a pointer to the image memory
		buffer = BufferUtils.createByteBuffer(texWidth * texHeight * perPixel);
		decode(buffer, texWidth * perPixel, flipped);

		if (height < texHeight - 1) {
			int topOffset = (texHeight - 1) * (texWidth * perPixel);
			int bottomOffset = (height - 1) * (texWidth * perPixel);
			for (int x = 0; x < texWidth; x++) {
				for (int i = 0; i < perPixel; i++) {
					buffer.put(topOffset + x + i, buffer.get(x + i));
					buffer.put(bottomOffset + (texWidth * perPixel) + x + i, buffer.get(bottomOffset + x + i));
				}
			}
		}
		if (width < texWidth - 1) {
			for (int y = 0; y < texHeight; y++) {
				for (int i = 0; i < perPixel; i++) {
					buffer.put(((y + 1) * (texWidth * perPixel)) - perPixel + i, buffer.get(y * (texWidth * perPixel) + i));
					buffer.put((y * (texWidth * perPixel)) + (width * perPixel) + i, buffer.get((y * (texWidth * perPixel)) + ((width - 1) * perPixel) + i));
				}
			}
		}

		if (!hasAlpha() && forceAlpha) {
			ByteBuffer temp = BufferUtils.createByteBuffer(texWidth * texHeight * 4);
			for (int x = 0; x < texWidth; x++) {
				for (int y = 0; y < texHeight; y++) {
					int srcOffset = (y * 3) + (x * texHeight * 3);
					int dstOffset = (y * 4) + (x * texHeight * 4);

					temp.put(dstOffset, buffer.get(srcOffset));
					temp.put(dstOffset + 1, buffer.get(srcOffset + 1));
					temp.put(dstOffset + 2, buffer.get(srcOffset + 2));
					temp.put(dstOffset + 3, (byte) 255);
				}
			}

			colorType = COLOR_TRUEALPHA;
			bitDepth = 32;
			buffer = temp;
		}

		if (transparent != null) {
			for (int i = 0; i < texWidth * texHeight * 4; i += 4) {
				boolean match = true;
				for (int c = 0; c < 3; c++) {
					if (toInt(buffer.get(i + c)) != transparent[c]) {
						match = false;
					}
				}

				if (match) {
					buffer.put(i + 3, (byte) 0);
				}
			}
		}

		buffer.rewind();


	}

	private int toInt(byte b) {
		if (b < 0) {
			return 256 + b;
		}

		return b;
	}

	private int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

}
