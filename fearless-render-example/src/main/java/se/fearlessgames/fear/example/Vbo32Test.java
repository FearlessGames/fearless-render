package se.fearlessgames.fear.example;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.Transformation;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.*;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.shape.SphereFactory;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Vbo32Test {
	private int vboId;
	private final FloatBuffer vertexBuffer;
	private final IntBuffer indices;
	private final int shaderId;
	private int indicesId;

	public Vbo32Test(FloatBuffer vertexBuffer, IntBuffer indices, int shaderId) {
		this.vertexBuffer = vertexBuffer;
		this.indices = indices;
		this.shaderId = shaderId;
	}

	private void setupVbo() {

		//create the vbo binding
		vboId = GL15.glGenBuffers();
		//then bind it
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

		//add the data to the vbo
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);


		//create the indices id
		indicesId = GL15.glGenBuffers();
		//then bind it
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
		//and copy data into it
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);


		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}


	private void render(FloatBuffer projection, FloatBuffer modelView) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL20.glUseProgram(shaderId);


		setUniformMatrix4("projectionMatrix", projection);
		setUniformMatrix4("modelViewMatrix", modelView);

		int pointer = GL20.glGetAttribLocation(shaderId, "vertex");
		GL20.glEnableVertexAttribArray(pointer);
		GL20.glVertexAttribPointer(pointer, 3, GL11.GL_FLOAT, true, 3 * 4, 0);


		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.limit(), GL11.GL_UNSIGNED_INT, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glUseProgram(0);
	}

	private void setUniformMatrix4(String name, FloatBuffer buffer) {
		int pointer = GL20.glGetUniformLocation(shaderId, name);
		GL20.glUniformMatrix4(pointer, false, buffer);
	}

	public static void main(String[] args) throws LWJGLException {
		int w = 800;
		int h = 600;


		Display.setDisplayMode(new DisplayMode(w, h));
		Display.setVSyncEnabled(true);
		Display.setTitle("Vbo Test");

		ContextAttribs attribs = new ContextAttribs(3, 2).withProfileCore(true);

		Display.create(new PixelFormat(), attribs);

		FearGl fearGl = new FearLwjgl();

		fearGl.glViewport(0, 0, w, h);
		PerspectiveBuilder perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 200.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);

		Transformation transformation = new Transformation(new Vector3(0, 0, -10), Quaternion.IDENTITY, Vector3.ONE);
		Matrix4 modelView = transformation.asMatrix();

		MeshData meshData = new SphereFactory(100, 100, 1d, SphereFactory.TextureMode.PROJECTED).create();

		ShaderProgram shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile("src/main/resources/shaders/vbo32.vert", ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile("src/main/resources/shaders/vbo32.frag", ShaderType.FRAGMENT_SHADER);
		shaderProgram.attachToProgram(ShaderType.VERTEX_SHADER);
		shaderProgram.attachToProgram(ShaderType.FRAGMENT_SHADER);

		int error = GL11.glGetError();
		if (error != 0) {
			System.out.println("error during context setup: " + error);
		}

		Vbo32Test vaoTest = new Vbo32Test(meshData.getVertexBuffer(), meshData.getIndices(), shaderProgram.getShaderProgram());
		vaoTest.setupVbo();

		error = GL11.glGetError();
		if (error != 0) {
			System.out.println("error during vbo setup: " + error);
		}

		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			vaoTest.render(perspectiveBuilder.getMatrixAsBuffer(), GlMatrixBuilder.convert(modelView));
			Display.update();
			error = GL11.glGetError();
			if (error != 0) {
				System.out.println("error during rendering: " + error);
			}
		}

	}


}
