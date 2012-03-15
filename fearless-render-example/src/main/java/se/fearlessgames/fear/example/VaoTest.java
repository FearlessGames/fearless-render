package se.fearlessgames.fear.example;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.Transformation;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.*;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.vbo.InterleavedBuffer;
import se.fearlessgames.fear.vbo.VboBuilder;

public class VaoTest {
	private int vaoId;
	private final FearGl fearGl;
	private ShaderProgram shaderProgram;
	private MeshData meshData;
	private InterleavedBuffer interleavedBuffer;

	public VaoTest(FearGl fearGl) {
		this.fearGl = fearGl;
		meshData = new SphereFactory(100, 100, 1d, SphereFactory.TextureMode.PROJECTED).create();
		interleavedBuffer = VboBuilder.fromMeshData(fearGl, meshData).buildInterleavedBuffer();
	}

	private void setupShader() {
		shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.vert", ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.frag", ShaderType.FRAGMENT_SHADER);
		shaderProgram.attachToProgram(ShaderType.VERTEX_SHADER);
		shaderProgram.attachToProgram(ShaderType.FRAGMENT_SHADER);

	}


	private void setupVao() {

		//create the vao id
		vaoId = fearGl.glGenVertexArrays();
		//then bind it
		fearGl.glBindVertexArray(vaoId);


		//create the vbo binding
		int vboId = fearGl.glGenBuffers();
		//then bind it
		fearGl.glBindBuffer(BufferTarget.GL_ARRAY_BUFFER, vboId);

		//add the data to the vbo
		fearGl.glBufferData(BufferTarget.GL_ARRAY_BUFFER, interleavedBuffer.getBuffer(), BufferUsage.GL_STATIC_DRAW);

		//setup how to send the vertex data to the shader
		int stride = interleavedBuffer.getStride();
		int offset = 0;

		shaderProgram.setVertexAttribute("vertex", 3, stride, offset);

		offset = 3 * 4;

		if (interleavedBuffer.isNormals()) {
			shaderProgram.setVertexAttribute("normal", 3, stride, offset);
			offset += (3 * 4);
		}

		if (interleavedBuffer.isColors()) {
			shaderProgram.setVertexAttribute("color", 4, stride, offset);
			offset += (4 * 4);
		}

		if (interleavedBuffer.isTextureCords()) {
			shaderProgram.setVertexAttribute("textureCoord", 2, stride, offset);
		}


		//create the indices id
		int indicesId = fearGl.glGenBuffers();
		//then bind it
		fearGl.glBindBuffer(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, indicesId);
		//and copy data into it
		fearGl.glBufferData(BufferTarget.GL_ELEMENT_ARRAY_BUFFER, meshData.getIndices(), BufferUsage.GL_STATIC_DRAW);


		//remove the vao binding
		fearGl.glBindVertexArray(vaoId);
	}


	private void render(PerspectiveBuilder perspectiveBuilder, Matrix4 modelView) {
		//use the shader
		fearGl.glUseProgram(shaderProgram.getShaderProgram());

		shaderProgram.setUniformMatrix4("projectionMatrix", perspectiveBuilder.getMatrixAsBuffer());
		shaderProgram.setUniformMatrix4("modelViewMatrix", GlMatrixBuilder.convert(modelView));


		//bind the vao
		fearGl.glBindVertexArray(vaoId);

		//draw the bound vao
		fearGl.glDrawElements(VertexIndexMode.TRIANGLES, meshData.getIndices().limit(), IndexDataType.GL_UNSIGNED_INT, 0);
	}

	public static void main(String[] args) throws LWJGLException {
		int w = 800;
		int h = 600;


		DisplayUtil.create(w, h, "Shader Setup");
		FearGl fearGl = new FearLwjgl();

		fearGl.glViewport(0, 0, w, h);
		PerspectiveBuilder perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 200.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);

		Transformation transformation = new Transformation(new Vector3(0, 0, -10), Quaternion.IDENTITY, Vector3.ONE);
		Matrix4 modelView = transformation.asMatrix();

		VaoTest vaoTest = new VaoTest(fearGl);
		vaoTest.setupShader();
		vaoTest.setupVao();

		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			vaoTest.render(perspectiveBuilder, modelView);
			Display.update();
		}

	}


}
