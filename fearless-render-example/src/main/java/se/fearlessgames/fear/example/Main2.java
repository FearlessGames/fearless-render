package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.Skybox;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.renderbucket.RenderBucket;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.texture.*;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class Main2 {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private boolean done = false; //game runs until done is set to true
	private final FearGl fearGl;
	private final Scene scene;
	private final ExampleRenderer renderer;
	private final List<Orb> orbs = Lists.newArrayList();
	private Camera camera;
	private ShaderProgram shaderProgram;

	public Main2() {
		fearGl = DebuggingFearLwjgl.create();
		init();

		shaderProgram = createShaderProgram();
		renderer = new ExampleRenderer(new MeshRenderer(fearGl));
		scene = createScene();
		Skybox skybox = new Skybox();
		skybox.getRoot().addChild(new Node("skybox-sphere", createSkyboxSphere(fearGl, shaderProgram, renderer.skyboxBucket)));
		scene.getRoot().addChild(skybox.getRoot());

		long t1 = System.nanoTime();
		long t2;
		TimeProvider timeProvider = new SystemTimeProvider();
		int c = 0;
		int x = 0, y = 0, z = 0;
		int angle = 0;
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
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				angle--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				angle++;
			}

			camera.setPosition(new Vector3(x, y, z));

			skybox.moveToCamera(camera);


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

	private Mesh createSkyboxSphere(FearGl fearGl, ShaderProgram shaderProgram, RenderBucket skyboxBucket) {
		TextureLoader textureManager = new FearlessTextureLoader(fearGl);
		Texture texture = null;
		try {
			String resourceName = "src/main/resources/texture/earth.png";
			texture = textureManager.load(resourceName, TextureFileType.PNG, new FileInputStream(resourceName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		MeshData meshData = new SphereFactory(100, 100, 100, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		return new Mesh(vertexArrayObject, new MeshType(shaderProgram, skyboxBucket, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture)));
	}

	private Scene createScene() {
		MeshData meshData = new SphereFactory(40, 40, 1, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vao = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();
		Node root = new Node("root");


		Orb sun = new Orb("Sun", vao, 2.5, 0, 0, new MeshType(shaderProgram, renderer.opaqueBucket));

		Orb planet = new Orb("Planet", vao, 1, 1e-3, 1e-3, new MeshType(shaderProgram, renderer.opaqueBucket));
		planet.setRotationRadius(new Vector3(30, 0, 0));
		sun.addChild(planet);

		Orb moon = new Orb("Moon", vao, 0.25, 1e-2, 1e-5, new MeshType(shaderProgram, renderer.opaqueBucket));
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
		camera = new Camera(new CameraPerspective(45.0f, ((float) w / (float) h), 0.1f, 200.0f));

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
	}

	public static void main(String[] args) {
		new Main2();
	}


}