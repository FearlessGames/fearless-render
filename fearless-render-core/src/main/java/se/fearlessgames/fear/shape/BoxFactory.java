package se.fearlessgames.fear.shape;

import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class BoxFactory implements ShapeFactory {
	private final FearGl fearGl;

	public BoxFactory(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	@Override
	public VertexBufferObject create() {
		float[] data = {
				// Front face (facing viewer), correct winding order.
				// Front face
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f,

				// Back face
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,

				// Top face
				-1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, -1.0f,

				// Bottom face
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,

				// Right face
				1.0f, -1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,

				// Left face
				-1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f
		};

		float[] textureCords = {
				// Front face
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f,

				// Back face
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,

				// Top face
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,

				// Bottom face
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,

				// Right face
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,

				// Left face
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f
		};

		int[] indices = {
				0, 1, 2, 0, 2, 3,	// Front face
				4, 5, 6, 4, 6, 7,	// Back face
				8, 9, 10, 8, 10, 11,  // Top face
				12, 13, 14, 12, 14, 15, // Bottom face
				16, 17, 18, 16, 18, 19, // Right face
				20, 21, 22, 20, 22, 23  // Left face
		};


		return VboBuilder.fromArray(fearGl, data).indices(indices).textureCoords(textureCords).triangles().build();
	}
}
