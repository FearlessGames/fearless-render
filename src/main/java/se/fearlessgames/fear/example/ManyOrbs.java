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
import se.fearlessgames.fear.shape.ShapeFactory;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class ManyOrbs {

	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;
	private final FearScene scene;
	private final Renderer renderer;
	private final List<Orb> orbs = Lists.newArrayList();
	private final VertexBufferObject vbo;

	public ManyOrbs() {
		fearGl = new FearLwjgl();
		init();

		int numOrbs = 100;

		ShapeFactory shapeFactory = new SphereFactory(fearGl, 200, 200, 1);
		vbo = shapeFactory.create();


		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, -15, -80));
		renderer = new Renderer(fearGl, createShaderProgram(), perspectiveBuilder);
		long t1 = System.nanoTime();
		long t2;
		TimeProvider timeProvider = new SystemTimeProvider();
		int c = 0;


		for (int i = 0; i < numOrbs; i++) {
			Orb orb = new Orb("orb" + i, vbo, 1 * Math.random(), 1e-4 * Math.random(), 1e-4 * Math.random());
			orb.setRotationRadius(new Vector3(30 * Math.random(), 0, 0));
			orbs.add(orb);
			scene.getRoot().addChild(orb.getRoot());
		}

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
				System.out.printf("Orbs: %10d, FPS: %.3f\n", numOrbs, 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();

	}

	private FearScene createScene() {
		FearNode root = new FearNode("root", Collections.<FearMesh>emptyList());
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
		int w = 640;
		int h = 480;

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
		perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 400.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
	}

	public static void main(String[] args) {
		new ManyOrbs();
	}


}
