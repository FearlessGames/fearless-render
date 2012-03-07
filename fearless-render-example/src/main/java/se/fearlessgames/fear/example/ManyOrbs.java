package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.*;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.shape.BoxFactory;
import se.fearlessgames.fear.shape.ShapeFactory;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.texture.*;
import se.fearlessgames.fear.texture.TextureType;
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
	private final Logger log = LoggerFactory.getLogger(getClass());
	public static final Transformation DEFAULT_CAMERA = new Transformation(Vector3.ZERO, Quaternion.IDENTITY, Vector3.ONE);
	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;
	private final Scene scene;
	private final ExampleRenderer renderer;

	public ManyOrbs() {
		fearGl = new FearLwjgl();
		init();

		int numOrbs = 20;
		int numTransparent = 10;

		ShapeFactory shapeFactory = new SphereFactory(fearGl, 100, 100, 2, SphereFactory.TextureMode.PROJECTED);
		VertexBufferObject vbo = shapeFactory.create();


		scene = createScene();
		scene.getRoot().setPosition(new Vector3(0, -15, -80));
		ShaderProgram shaderProgram = createShaderProgram();
		renderer = new ExampleRenderer(new MeshRenderer(fearGl, perspectiveBuilder));
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

		MeshType orbMeshType = new MeshType(shaderProgram, renderer.opaqueBucket, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture));
		MeshType orbMeshType2 = new MeshType(shaderProgram, renderer.translucentBucket, DirectionalLightRenderState.DEFAULT, new TransparentTextureRenderState(texture));

		List<Orb> orbs = Lists.newArrayList();
		for (int i = 0; i < numOrbs; i++) {
			MeshType type = (i < numOrbs - numTransparent) ? orbMeshType : orbMeshType2;
			Orb orb = new Orb("orb" + i, vbo, 0.5 + 1 * rand.nextDouble(), 1e-3 * rand.nextDouble(), 1e-3 * (rand.nextDouble() - 0.5), type);
			orb.setRotationRadius(new Vector3(30 * rand.nextDouble(), 20 * rand.nextDouble(), 0));
			orbs.add(orb);
			scene.getRoot().addChild(orb.getRoot());
		}

		scene.getRoot().addChild(createBoxNode(shaderProgram, textureManager));
		log.info("Scene contains {} vertices", scene.getRoot().getVertexCount());

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
				log.info("Orbs: {}, FPS: {}", numOrbs, 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();

	}

	private Node createBoxNode(ShaderProgram shaderProgram, TextureLoader textureManager) {
		Texture texture = null;
		try {
			texture = textureManager.loadTexture(TextureType.PNG, new FileInputStream("src/main/resources/texture/crate.png"));
		} catch (IOException ignored) {
		}
		MeshType meshType = new MeshType(shaderProgram, RenderBucket.OPAQUE, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture));
		Mesh boxMesh = new Mesh(new BoxFactory(fearGl).create(), meshType);

		Node node = new Node("Center Box", boxMesh);
		node.setScale(new Vector3(4, 4, 4));
		node.setRotation(Quaternion.fromEulerAngles(-30, -30, 30));
		return node;
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
		scene.render(renderer, DEFAULT_CAMERA);
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
		perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 400.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
		fearGl.glEnable(Capability.GL_BLEND);
	}

	public static void main(String[] args) {
		new ManyOrbs();
	}


}
