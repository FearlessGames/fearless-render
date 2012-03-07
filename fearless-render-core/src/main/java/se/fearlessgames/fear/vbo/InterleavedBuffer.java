package se.fearlessgames.fear.vbo;

import java.nio.FloatBuffer;

public class InterleavedBuffer {
	private final FloatBuffer buffer; //V[xyz]N[xyz]C[rgba]T1[st]
	private final boolean normals;
	private final boolean colors;
	private final boolean textureCords;

	private final int stride;

	public InterleavedBuffer(FloatBuffer buffer, boolean normals, boolean colors, boolean textureCords) {
		this.buffer = buffer;
		this.normals = normals;
		this.colors = colors;
		this.textureCords = textureCords;

		int stride = 3; //xyz

		if (normals) {
			stride += 3; //xyz
		}

		if (colors) {
			stride += 4; //rgba
		}

		if (textureCords) {
			stride += 2; //st
		}

		stride *= 4; //4bytes for a float

		this.stride = stride;

	}

	public FloatBuffer getBuffer() {
		return buffer;
	}

	public boolean isNormals() {
		return normals;
	}

	public boolean isTextureCords() {
		return textureCords;
	}

	public boolean isColors() {
		return colors;
	}

	public int getStride() {
		return stride;
	}
}
