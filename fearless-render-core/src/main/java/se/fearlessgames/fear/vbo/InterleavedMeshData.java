package se.fearlessgames.fear.vbo;

import com.google.common.collect.Lists;

import java.nio.FloatBuffer;
import java.util.List;

public class InterleavedMeshData {
	private final FloatBuffer buffer; // for example V[xyz]N[xyz]C[rgba]T1[st]
	private List<MeshBuffer> buffers = Lists.newArrayList();
	private final int stride;

	public InterleavedMeshData(FloatBuffer buffer, boolean normals, boolean colors, boolean textureCords) {
		this.buffer = buffer;
		int offset = 0;
		boolean vertices = true;

		if (vertices) {
			int count = 3;	// XYZ
			buffers.add(new MeshBuffer("vertex", count, 4, offset));
			offset += count * 4;
		}

		if (normals) {
			int count = 3;   // XYZ
			buffers.add(new MeshBuffer("normal", count, 4, offset));
			offset += count * 4;
		}

		if (colors) {
			int count = 4;	// RGBA
			buffers.add(new MeshBuffer("color", count, 4, offset));
			offset += count * 4;
		}

		if (textureCords) {
			int count = 2;	// UV
			buffers.add(new MeshBuffer("textureCoord", count, 4, offset));
			offset += count * 4;
		}

		stride = offset;
	}

	public List<MeshBuffer> getBuffers() {
		return buffers;
	}

	public FloatBuffer getFloatBuffer() {
		return buffer;
	}

	public int getStride() {
		return stride;
	}
}