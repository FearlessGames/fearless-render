package se.fearlessgames.fear;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * Source: http://en.wikipedia.org/wiki/Vertex_Buffer_Object
 */

public class VboBox {

	private int shaderProgram;
	private double angle;
	private VertexBufferObject vbo;

	public VboBox() {
		createShaders();
		vbo = createVbo();
	}

	private void createShaders() {
		Shaders shaders = new Shaders();

		shaderProgram = shaders.createProgram();
		int vertexShader = shaders.loadAndCompileVertexShader("src/main/resources/shaders/screen.vert");
		int fragmentShader = shaders.loadAndCompileFragmentShader("src/main/resources/shaders/screen.frag");
		shaders.attachToProgram(shaderProgram, vertexShader, fragmentShader);

	}

	private VertexBufferObject createVbo() {
		float[] data = {
				// Front face (facing viewer), correct winding order.
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,

				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f
		};

		int[] indices = {
				0, 1, 2, 3,
				7, 6, 5, 4,
				1, 5, 6, 2,
				0, 3, 7, 4
		};
		return new VertexBufferObject(data, indices);
	}


	public void draw() {

		GL20.glUseProgram(shaderProgram);

		int pos = GL20.glGetUniformLocation(shaderProgram, "pos");
		if (pos != -1) {
			GL20.glUniform3f(pos, 0f, 0f, -10f);
		} else {
			throw new RuntimeException("Failed to get pos location");
		}

		int rot = GL20.glGetUniformLocation(shaderProgram, "rot");
		if (rot != -1) {
			GL20.glUniform3f(rot, (float) angle, (float) angle * 0.2f, (float) angle * 0.5f);
		} else {
			throw new RuntimeException("Failed to get rot location");
		}


		vbo.draw();

		GL20.glUseProgram(0);
	}

	public void update() {
		angle += 0.001;
	}


}
