package se.fearlessgames.fear;

import java.nio.*;

public class BufferUtils {
	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}

	public static CharBuffer createCharBuffer(int size) {
		return createByteBuffer(size << 1).asCharBuffer();
	}

	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}

	public static LongBuffer createLongBuffer(int size) {
		return createByteBuffer(size << 3).asLongBuffer();
	}

	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}

	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << 3).asDoubleBuffer();
	}

	public static FloatBuffer createFloat3Buffer(int size) {
		return createFloatBuffer(size * 3);
	}

	public static FloatBuffer createFloat4Buffer(int size) {
		return createFloatBuffer(size * 4);
	}

	public static FloatBuffer createFloat2Buffer(int size) {
		return createFloatBuffer(size * 2);
	}


}
