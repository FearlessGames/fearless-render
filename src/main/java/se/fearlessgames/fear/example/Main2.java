package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.fear.*;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.EnumSet;
import java.util.List;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class Main2 {

	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;
	private final FearScene scene;
	private final Renderer renderer;
	private FearNode sun;
	private FearNode planet;
	private FearNode moon;

	public Main2() {
		fearGl = new FearLwjgl();
		init();


		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, 0, -80));
		renderer = new Renderer(fearGl, createShaderProgram(), perspectiveBuilder);
		long t1 = System.nanoTime();
		long t2;
		int c = 0;
		double angle = 0;
		while (!done) {
			if (Display.isCloseRequested()) {
				done = true;
			}
			// TODO: update the scene
			angle += 0.001;
			planet.setRotation(Quaternion.fromEulerAngles(angle, 0, 0));

			render();
			Display.update();
			t2 = System.nanoTime();
			if ((c++ & 127) == 0) {
				System.out.printf("FPS: %.3f\n", 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();

	}

	private FearScene createScene() {
		VertexBufferObject vbo = createVbo();
		FearNode root = new FearNode();

		FearMesh sunMesh = new FearMesh(vbo);
		sunMesh.setScale(new Vector3(5, 5, 5));
		sun = new FearNode(Lists.newArrayList(sunMesh));

		FearMesh planetMesh = new FearMesh(vbo);
		planet = new FearNode(Lists.newArrayList(planetMesh));
		planet.setPosition(new Vector3(30, 0, 0));
		planetMesh.setScale(new Vector3(2, 2, 2));

		moon = new FearNode(Lists.newArrayList(new FearMesh(vbo)));
		moon.setScale(new Vector3(0.5, 0.5, 0.5));
		moon.setPosition(new Vector3(5, 0, 0));

		planet.addChild(moon);
		
		sun.addChild(planet);

		root.addChild(sun);

		FearScene fearScene = new FearScene(root);
		return fearScene;
	}

	private List<FearMesh> createBottomLeftMeshes(VertexBufferObject vbo) {
		FearMesh[] meshes = new FearMesh[4];

		meshes[0] = new FearMesh(vbo);
		meshes[1] = new FearMesh(vbo);
		meshes[2] = new FearMesh(vbo);
		meshes[3] = new FearMesh(vbo);

		meshes[0].setPosition(new Vector3(5, 5, 0));
		meshes[1].setPosition(new Vector3(5, -5, 0));
		meshes[2].setPosition(new Vector3(-5, 5, 0));
		meshes[3].setPosition(new Vector3(-5, -5, 0));

		return Lists.newArrayList(meshes);
	}

	private List<FearMesh> createMeshes(VertexBufferObject vbo) {


		FearMesh[] meshes = new FearMesh[3];

		meshes[0] = new FearMesh(vbo);
		meshes[1] = new FearMesh(vbo);
		meshes[2] = new FearMesh(vbo);


		meshes[0].setPosition(new Vector3(20, 20, 0));
		meshes[1].setPosition(new Vector3(20, -20, 0));
		meshes[2].setPosition(new Vector3(-20, 20, 0));

		return Lists.newArrayList(meshes);
	}

	private ShaderProgram createShaderProgram() {
		ShaderProgram shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.vert", ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile("src/main/resources/shaders/screen.frag", ShaderType.FRAGMENT_SHADER);
		shaderProgram.attachToProgram(ShaderType.VERTEX_SHADER);
		shaderProgram.attachToProgram(ShaderType.FRAGMENT_SHADER);
		return shaderProgram;
	}


	private void render() {
		fearGl.glClear(EnumSet.of(ClearBit.GL_COLOR_BUFFER_BIT, ClearBit.GL_DEPTH_BUFFER_BIT));
		scene.render(renderer);
	}

	private void init() {
		int w = 1280;
		int h = 1024;

		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setVSyncEnabled(true);
			Display.setTitle("Shader Setup");
			Display.create();
		} catch (Exception e) {
			System.out.println("Error setting up display");
			System.exit(0);
		}

		fearGl.glViewport(0, 0, w, h);
		perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 100.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
	}

	public static void main(String[] args) {
		new Main2();
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

		float[] colors = {
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
		};

		int[] indices = {
				0, 1, 2, 3,
				7, 6, 5, 4,
				1, 5, 6, 2,
				0, 3, 7, 4
		};

		return VboBuilder.fromArray(fearGl, data).indices(indices).colors(colors).quads().build();
	}

}