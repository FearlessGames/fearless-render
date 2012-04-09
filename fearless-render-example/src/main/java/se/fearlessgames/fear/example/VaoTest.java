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
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

public class VaoTest {
	private final FearGl fearGl;
	private ShaderProgram shaderProgram;
	private final VertexArrayObject vertexArrayObject;

	public VaoTest(FearGl fearGl) {
		this.fearGl = fearGl;
		setupShader();

		MeshData meshData = new SphereFactory(100, 100, 1d, SphereFactory.TextureMode.PROJECTED).create();
		VaoBuilder vaoBuilder = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData);
		vertexArrayObject = vaoBuilder.build();
	}

	private void setupShader() {
		shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.vert", ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.frag", ShaderType.FRAGMENT_SHADER);
		shaderProgram.attachToProgram(ShaderType.VERTEX_SHADER);
		shaderProgram.attachToProgram(ShaderType.FRAGMENT_SHADER);

	}


	private void render(CameraPerspective cameraPerspective, Matrix4 modelView) {
		//use the shader
		fearGl.glUseProgram(shaderProgram.getShaderProgram());

		shaderProgram.setUniformMatrix4("projectionMatrix", cameraPerspective.getMatrixAsBuffer());
		shaderProgram.setUniformMatrix4("modelViewMatrix", GlMatrixBuilder.convert(modelView));


		//bind the vao
		fearGl.glBindVertexArray(vertexArrayObject.getVaoId());

		//draw the bound vao
		fearGl.glDrawElements(VertexIndexMode.TRIANGLES, vertexArrayObject.getIndicesCount(), IndexDataType.GL_UNSIGNED_INT, 0);
	}

	public static void main(String[] args) throws LWJGLException {
		int w = 800;
		int h = 600;


		DisplayUtil.create(w, h, "Shader Setup");
		FearGl fearGl = DebuggingFearLwjgl.create();

		fearGl.glViewport(0, 0, w, h);
		CameraPerspective cameraPerspective = new CameraPerspective(45.0f, ((float) w / (float) h), 0.1f, 200.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);

		Transformation transformation = new Transformation(new Vector3(0, 0, -10), Quaternion.IDENTITY, Vector3.ONE);
		Matrix4 modelView = transformation.asMatrix();

		VaoTest vaoTest = new VaoTest(fearGl);
		vaoTest.setupShader();


		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			vaoTest.render(cameraPerspective, modelView);
			Display.update();
		}

	}


}
