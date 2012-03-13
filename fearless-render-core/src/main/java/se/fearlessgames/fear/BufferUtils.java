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


	public static IntBuffer duplicate(IntBuffer buffer) {
		if (buffer == null) {
			return null;
		}
		IntBuffer duplicate = buffer.duplicate();
		duplicate.rewind();
		return duplicate;
	}

	public static FloatBuffer duplicate(FloatBuffer buffer) {
		if (buffer == null) {
			return null;
		}
		FloatBuffer duplicate = buffer.duplicate();
		duplicate.rewind();
		return duplicate;
	}

	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer floatBuffer = createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
		return floatBuffer;
	}

	public static IntBuffer createIntBuffer(int[] data) {
		IntBuffer intBuffer = createIntBuffer(data.length);
		intBuffer.put(data);
		intBuffer.flip();
		return intBuffer;
	}
}
