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
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.texture.TextureLoader;
import se.fearlessgames.fear.texture.TextureLoaderImpl;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class ManyOrbs {

	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;
	private final Scene scene;
	private final Renderer renderer;
	private final List<Orb> orbs = Lists.newArrayList();
	private final VertexBufferObject vbo;

	public ManyOrbs() {
		fearGl = new FearLwjgl();
		init();

		int numOrbs = 100;

		ShapeFactory shapeFactory = new SphereFactory(fearGl, 100, 100, 2, SphereFactory.TextureMode.PROJECTED);
		vbo = shapeFactory.create();


		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, -15, -80));
		renderer = new Renderer(new MeshRenderer(fearGl, createShaderProgram(), perspectiveBuilder));
		long t1 = System.nanoTime();
		long t2;
		TimeProvider timeProvider = new SystemTimeProvider();
		int c = 0;

		Random rand = new Random();
		TextureLoader textureManager = new TextureLoaderImpl();
		Texture texture = null;
		try {
			texture = textureManager.loadTexture(se.fearlessgames.fear.texture.TextureType.PNG, new FileInputStream("src/main/resources/texture/earth.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < numOrbs; i++) {
			Orb orb = new Orb("orb" + i, vbo, 1 * rand.nextDouble(), 1e-4 * rand.nextDouble(), 1e-3 * (rand.nextDouble() - 0.5));
			orb.setRotationRadius(new Vector3(30 * rand.nextDouble(), 20 * rand.nextDouble(), 0));
			orbs.add(orb);
			orb.getMesh().setTexture(texture);
			scene.getRoot().addChild(orb.getRoot());
		}
		System.out.printf("Scene contains %d vertices\n", scene.getRoot().getVertexCount());

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

	private Scene createScene() {
		Node root = new Node("root");
		Scene scene = new Scene(root);
		return scene;
	}


	private ShaderProgram createShaderProgram() {
		ShaderProgram shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile("src/main/resources/shaders/textured.vert", ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile("src/main/resources/shaders/textured.frag", ShaderType.FRAGMENT_SHADER);
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
