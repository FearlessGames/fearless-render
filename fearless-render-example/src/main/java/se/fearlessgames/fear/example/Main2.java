package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.*;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.shape.ShapeFactory;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.EnumSet;
import java.util.List;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class Main2 {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;
	private final Scene scene;
	private final Renderer renderer;
	private final List<Orb> orbs = Lists.newArrayList();
	private Transformation camera = new Transformation(Vector3.ZERO, Quaternion.IDENTITY, Vector3.ONE);
	private ShaderProgram shaderProgram;

	public Main2() {
		fearGl = new FearLwjgl();
		init();

		shaderProgram = createShaderProgram();
		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, -15, -80));

		renderer = new Renderer(new MeshRenderer(fearGl, perspectiveBuilder));
		long t1 = System.nanoTime();
		long t2;
		TimeProvider timeProvider = new SystemTimeProvider();
		int c = 0;
		int x = 0, y = 0, z = 0;
		while (!done) {
			if (Display.isCloseRequested()) {
				done = true;
			}
			// TODO: update the scene
			long now = timeProvider.now();
			for (Orb orb : orbs) {
				orb.update(now);
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				x++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				x--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				y--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				y++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				z--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
				z++;
			}
			camera = new Transformation(new Vector3(x, y, z), Quaternion.IDENTITY, Vector3.ONE);
			render();
			Display.update();
			t2 = System.nanoTime();
			if ((c++ & 127) == 0) {
				log.info("FPS: {}", 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();

	}

	private Scene createScene() {
		VertexBufferObject vbo = createVbo();
		Node root = new Node("root");


		Orb sun = new Orb("Sun", vbo, 2.5, 0, 0, new MeshType(shaderProgram, RenderBucket.OPAQUE));

		Orb planet = new Orb("Planet", vbo, 1, 1e-3, 1e-3, new MeshType(shaderProgram, RenderBucket.OPAQUE));
		planet.setRotationRadius(new Vector3(30, 0, 0));
		sun.addChild(planet);

		Orb moon = new Orb("Moon", vbo, 0.25, 1e-2, 1e-5, new MeshType(shaderProgram, RenderBucket.OPAQUE));
		moon.setRotationRadius(new Vector3(10, 0, 0));
		planet.addChild(moon);

		orbs.add(sun);
		orbs.add(planet);
		orbs.add(moon);

		root.addChild(sun.getRoot());

		Scene scene = new Scene(root);
		return scene;
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
		scene.render(renderer, camera);
	}

	private void init() {
		int w = 1024;
		int h = 768;

		try {
			DisplayUtil.create(w, h, "Shader Setup");
		} catch (Exception e) {
			log.error("Error setting up display", e);
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
		ShapeFactory factory = new SphereFactory(fearGl, 40, 40, 1, SphereFactory.TextureMode.PROJECTED);
		return factory.create();
	}

}