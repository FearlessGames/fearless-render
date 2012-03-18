package se.fearlessgames.fear.shape;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BoxFactory implements ShapeFactory {
	private static final double X = 1;
	private static final double Y = 1;
	private static final double Z = 1;

	public BoxFactory() {
	}

	@Override
	public MeshData create() {
		return new MeshData("Box",
				computeVertices(),
				computeNormals(),
				null,
				computeTexCords(),
				computeIndices(),
				VertexIndexMode.TRIANGLES);

	}

	private IntBuffer computeIndices() {
		int[] indices = {2, 1, 0, 3, 2, 0, 6, 5, 4, 7, 6,
				4, 10, 9, 8, 11, 10, 8, 14, 13, 12, 15, 14, 12,
				18, 17, 16, 19, 18, 16, 22, 21, 20, 23, 22, 20};
		return BufferUtils.createIntBuffer(indices);
	}

	private FloatBuffer computeTexCords() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(24 * 2);
		for (int i = 0; i < 6; i++) {
			buffer.put(1).put(0);
			buffer.put(0).put(0);
			buffer.put(0).put(1);
			buffer.put(1).put(1);
		}
		buffer.rewind();
		return buffer;
	}

	private FloatBuffer computeNormals() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(24 * 3);

		// back
		for (int i = 0; i < 4; i++) {
			buffer.put(0).put(0).put(-1);
		}

		// right
		for (int i = 0; i < 4; i++) {
			buffer.put(1).put(0).put(0);
		}

		// front
		for (int i = 0; i < 4; i++) {
			buffer.put(0).put(0).put(1);
		}

		// left
		for (int i = 0; i < 4; i++) {
			buffer.put(-1).put(0).put(0);
		}

		// top
		for (int i = 0; i < 4; i++) {
			buffer.put(0).put(1).put(0);
		}

		// bottom
		for (int i = 0; i < 4; i++) {
			buffer.put(0).put(-1).put(0);
		}
		buffer.rewind();
		return buffer;
	}

	private FloatBuffer computeVertices() {
		final Vector3 vertexes[] = new Vector3[8];
		vertexes[0] = new Vector3(-X, -Y, -Z);
		vertexes[1] = new Vector3(X, -Y, -Z);
		vertexes[2] = new Vector3(X, Y, -Z);
		vertexes[3] = new Vector3(-X, Y, -Z);
		vertexes[4] = new Vector3(X, -Y, Z);
		vertexes[5] = new Vector3(-X, -Y, Z);
		vertexes[6] = new Vector3(X, Y, Z);
		vertexes[7] = new Vector3(-X, Y, Z);


		FloatBuffer buffer = BufferUtils.createFloatBuffer(24 * 3);

		// Back
		addVector3(vertexes[0], buffer, 0);
		addVector3(vertexes[1], buffer, 1);
		addVector3(vertexes[2], buffer, 2);
		addVector3(vertexes[3], buffer, 3);

		// Right
		addVector3(vertexes[1], buffer, 4);
		addVector3(vertexes[4], buffer, 5);
		addVector3(vertexes[6], buffer, 6);
		addVector3(vertexes[2], buffer, 7);

		// Front
		addVector3(vertexes[4], buffer, 8);
		addVector3(vertexes[5], buffer, 9);
		addVector3(vertexes[7], buffer, 10);
		addVector3(vertexes[6], buffer, 11);

		// Left
		addVector3(vertexes[5], buffer, 12);
		addVector3(vertexes[0], buffer, 13);
		addVector3(vertexes[3], buffer, 14);
		addVector3(vertexes[7], buffer, 15);

		// Top
		addVector3(vertexes[2], buffer, 16);
		addVector3(vertexes[6], buffer, 17);
		addVector3(vertexes[7], buffer, 18);
		addVector3(vertexes[3], buffer, 19);

		// Bottom
		addVector3(vertexes[0], buffer, 20);
		addVector3(vertexes[5], buffer, 21);
		addVector3(vertexes[4], buffer, 22);
		addVector3(vertexes[1], buffer, 23);
		buffer.rewind();
		return buffer;
	}

	private void addVector3(Vector3 vector, FloatBuffer buffer, int index) {
		buffer.put(index * 3, (float) vector.getX());
		buffer.put((index * 3) + 1, (float) vector.getY());
		buffer.put((index * 3) + 2, (float) vector.getZ());
	}


}
