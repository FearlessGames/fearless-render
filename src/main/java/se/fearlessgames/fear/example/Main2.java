package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.*;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.Collections;
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
	private final List<Orb> orbs = Lists.newArrayList();

	public Main2() {
		fearGl = new FearLwjgl();
		init();


		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, -15, -80));
		renderer = new Renderer(fearGl, createShaderProgram(), perspectiveBuilder);
		long t1 = System.nanoTime();
		long t2;
		TimeProvider timeProvider = new SystemTimeProvider();
		int c = 0;
		while (!done) {
			if (Display.isCloseRequested()) {
				done = true;
			}
			// TODO: update the scene
			long now = timeProvider.now();
			for (Orb orb : orbs) {
				orb.update(now);
			}

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
		FearNode root = new FearNode("root", Collections.<FearMesh>emptyList());

		Orb sun = new Orb("Sun", vbo, 2.5, 0, 0);
		orbs.add(sun);
		FearNode sunRoot = sun.getRoot();

		Orb planet = new Orb("Planet", vbo, 1, 1e-3, 1e-3);
		orbs.add(planet);
		FearNode planetRoot = planet.getRoot();
		planet.getOrb().setPosition(new Vector3(30, 0, 0));

		Orb moon = new Orb("Moon", vbo, 0.25, 1e-2, 1e-5);
		orbs.add(moon);
		moon.getOrb().setPosition(new Vector3(10, 0, 0));


		planetRoot.addChild(moon.getRoot());
		sunRoot.addChild(planetRoot);

		root.addChild(sunRoot);

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
		int w = 1024;
		int h = 768;

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
		perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 200.0f);

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