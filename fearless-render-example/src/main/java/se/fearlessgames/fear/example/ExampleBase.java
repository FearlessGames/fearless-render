package se.fearlessgames.fear.example;

import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.input.hw.HardwareKeyboardController;
import se.fearlessgames.fear.input.hw.HardwareMouseController;
import se.fearlessgames.fear.light.DirectionalLight;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.texture.FearlessTextureLoader;
import se.fearlessgames.fear.texture.TextureLoader;

import java.util.EnumSet;

public abstract class ExampleBase {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final Camera camera;
	protected final FearGl fearGl;
	protected final Scene scene;
	protected final ExampleRenderer renderer;
	protected final TextureLoader textureManager;
	protected final int height;
	protected final int width;
	protected final ShaderProgram shaderProgram;
	protected final InputHandler inputHandler;

	private final String vertexShaderFile;
	private final String fragmentShaderFile;


	public ExampleBase(int width, int height, String vertexShaderFile, String fragmentShaderFile) throws Exception {
		this.height = height;
		this.width = width;
		this.vertexShaderFile = vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;

		fearGl = DebuggingFearLwjgl.create();

		createDisplay();

		shaderProgram = createShaderProgram();


		textureManager = new FearlessTextureLoader(fearGl);
		camera = new Camera(new CameraPerspective(45.0f, ((float) width / (float) height), 0.1f, 10000.0f));
		renderer = new ExampleRenderer(new MeshRenderer(fearGl));
		scene = createScene();

		inputHandler = createInputHandler();
	}

	private InputHandler createInputHandler() {
		InputHandler inputHandler = new InputHandler(new InputController(new HardwareKeyboardController(), new HardwareMouseController()));

		inputHandler.addTrigger(new InputTrigger(new KeyboardAction(camera), Predicates.ANY_KEY_DOWN));

		return inputHandler;
	}

	private void createDisplay() {
		try {
			DisplayUtil.create(width, height, "Fearless-render example");
		} catch (Exception e) {
			log.error("Error setting up display", e);
			throw new RuntimeException("Error setting up display", e);
		}

		log.info("OpenGL Vendor: " + fearGl.glGetString(StringName.VENDOR));
		log.info("OpenGL Renderer: " + fearGl.glGetString(StringName.RENDERER));
		log.info("OpenGL Version: " + fearGl.glGetString(StringName.VERSION));
		log.info("OpenGL GLSL Version: " + fearGl.glGetString(StringName.SHADING_LANGUAGE_VERSION));
		log.info("OpenGL Extension: " + fearGl.glGetString(StringName.EXTENSIONS));

		fearGl.glViewport(0, 0, width, height);
		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
	}

	public void startRenderLoop() {
		long t1 = System.nanoTime();
		long t2;
		int c = 0;

		boolean done = false;
		while (!done) {
			if (Display.isCloseRequested()) {
				done = true;
			}

			inputHandler.poll();

			beforeRender();
			render();
			afterRender();

			Display.update();
			t2 = System.nanoTime();
			if ((c++ & 127) == 0) {
				log.info("FPS: {}", 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();
	}

	protected void render() {
		fearGl.glClear(EnumSet.of(ClearBit.GL_COLOR_BUFFER_BIT, ClearBit.GL_DEPTH_BUFFER_BIT));
		scene.render(renderer, camera);
	}

	private ShaderProgram createShaderProgram() {
		ShaderProgram shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.loadAndCompile(vertexShaderFile, ShaderType.VERTEX_SHADER);
		shaderProgram.loadAndCompile(fragmentShaderFile, ShaderType.FRAGMENT_SHADER);
		shaderProgram.attachToProgram(ShaderType.VERTEX_SHADER);
		shaderProgram.attachToProgram(ShaderType.FRAGMENT_SHADER);
		return shaderProgram;
	}

	public abstract void beforeRender();

	public abstract void afterRender();

	public abstract Scene createScene() throws Exception;

	protected static class SunLight implements DirectionalLight {
		private final Vector3 location = new Vector3(20f, 20f, 0f);
		private final ColorRGBA diffuse = new ColorRGBA(0.8f, 0.8f, 0.8f, 0f);
		private final ColorRGBA ambient = new ColorRGBA(0.1f, 0.1f, 0.1f, 0f);
		private final ColorRGBA specular = new ColorRGBA(0.1f, 0.1f, 0.1f, 0f);

		@Override
		public Vector3 getLocation() {
			return location;
		}

		@Override
		public ColorRGBA getDiffuse() {
			return diffuse;
		}

		@Override
		public ColorRGBA getAmbient() {
			return ambient;
		}

		@Override
		public ColorRGBA getSpecular() {
			return specular;
		}
	}

	private static class KeyboardAction implements TriggerAction {
		int x = 0;
		int y = 0;
		int z = 0;
		private Camera camera;

		private KeyboardAction(Camera camera) {
			this.camera = camera;
		}

		@Override
		public void perform(InputState inputState) {
			KeyboardState keyboardState = inputState.getKeyboardState();
			if (keyboardState.isDown(Key.RIGHT)) {
				x++;
			}

			if (keyboardState.isDown(Key.LEFT)) {
				x--;
			}

			if (keyboardState.isDown(Key.DOWN)) {
				y--;
			}

			if (keyboardState.isDown(Key.UP)) {
				y++;
			}

			if (keyboardState.isDown(Key.A)) {
				z--;
			}

			if (keyboardState.isDown(Key.Z)) {
				z++;
			}

			camera.setPosition(new Vector3(x, y, z));
		}
	}


}
