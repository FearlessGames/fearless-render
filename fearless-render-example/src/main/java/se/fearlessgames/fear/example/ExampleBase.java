package se.fearlessgames.fear.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.FearError;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.camera.RotationCamera;
import se.fearlessgames.fear.display.Display;
import se.fearlessgames.fear.display.DisplayBuilder;
import se.fearlessgames.fear.display.DisplayConfig;
import se.fearlessgames.fear.display.lwjgl.LwjglDisplayBuilder;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.input.hw.DisplayFocusController;
import se.fearlessgames.fear.input.hw.HardwareKeyboardController;
import se.fearlessgames.fear.input.hw.HardwareMouseController;
import se.fearlessgames.fear.input.hw.lwjgl.LwjglDisplayFocus;
import se.fearlessgames.fear.input.hw.lwjgl.LwjglHardwareKeyboard;
import se.fearlessgames.fear.input.hw.lwjgl.LwjglHardwareMouse;
import se.fearlessgames.fear.light.DirectionalLight;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.shader.ShaderProgram;
import se.fearlessgames.fear.texture.FearlessTextureLoader;
import se.fearlessgames.fear.texture.TextureLoader;

import java.util.EnumSet;
import java.util.List;

public abstract class ExampleBase {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final DisplayBuilder displayBuilder = new LwjglDisplayBuilder();
	protected final RotationCamera camera;
	protected final FearGl fearGl;
	protected final Scene scene;
	protected final ExampleRenderer renderer;
	protected final TextureLoader textureManager;
	protected final int height;
	protected final int width;
	protected final ShaderProgram shaderProgram;
	protected final InputHandler inputHandler;

	protected final MouseController mouseController;
	protected final KeyboardController keyboardController;

	private final String vertexShaderFile;
	private final String fragmentShaderFile;

	protected boolean done;
	protected Display display;


	public ExampleBase(int width, int height, String vertexShaderFile, String fragmentShaderFile) throws Exception {
		this.height = height;
		this.width = width;
		this.vertexShaderFile = vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;

		fearGl = LoggingFearGl.create(new FearLwjgl());

		createDisplay();

		shaderProgram = createShaderProgram();

		textureManager = new FearlessTextureLoader(fearGl);
		camera = new RotationCamera(new CameraPerspective(45.0f, ((float) width / (float) height), 0.1f, 10000.0f));
		renderer = new ExampleRenderer(new MeshRenderer(fearGl));
		scene = createScene();

		mouseController = new HardwareMouseController(new SystemTimeProvider(), new LwjglHardwareMouse(), new MouseConfig(500));
		keyboardController = new HardwareKeyboardController(new LwjglHardwareKeyboard());

		inputHandler = new InputHandler(new InputController(keyboardController, mouseController, new DisplayFocusController(new LwjglDisplayFocus())));
		inputHandler.addTrigger(new InputTrigger(new QuitAction(), KeyboardPredicates.singleKeyDown(Key.ESCAPE)));
		inputHandler.addTrigger(new InputTrigger(new FullscreenAction(), KeyboardPredicates.singleKeyUp(Key.F11)));

		setupCameraControl();
	}

	public void setupCameraControl() {
		FailFirstPersonController firstPersonController = new FailFirstPersonController(inputHandler, camera);
		firstPersonController.setupKeyboard();
		firstPersonController.setupMouseTriggers();
		mouseController.grabbed(true);
	}

	private void createDisplay() {
		try {
			display = displayBuilder.createBuilder()
					.setDimensions(width, height)
					.setTitle("Fearless-render example")
					.build();

		} catch (Exception e) {
			log.error("Error setting up display", e);
			throw new RuntimeException("Error setting up display", e);
		}

		log.info("OpenGL Vendor: " + fearGl.glGetString(StringName.VENDOR));
		log.info("OpenGL Renderer: " + fearGl.glGetString(StringName.RENDERER));
		log.info("OpenGL Version: " + fearGl.glGetString(StringName.VERSION));
		log.info("OpenGL GLSL Version: " + fearGl.glGetString(StringName.SHADING_LANGUAGE_VERSION));
		//log.info("OpenGL Extension: " + fearGl.glGetString(StringName.EXTENSIONS));

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

		done = false;
		while (!done) {
			if (display.isCloseRequested()) {
				done = true;
			}

			inputHandler.poll();

			beforeRender();
			render();
			afterRender();

			display.update();
			t2 = System.nanoTime();
			if ((c++ & 127) == 0) {
				log.info("FPS: {}", 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		display.destroy();
	}

	protected void render() {
		fearGl.glClear(EnumSet.of(ClearBit.GL_COLOR_BUFFER_BIT, ClearBit.GL_DEPTH_BUFFER_BIT));
		scene.render(renderer, camera);
	}

	private ShaderProgram createShaderProgram() {
		ShaderProgram shaderProgram = new ShaderProgram(fearGl);
		shaderProgram.load(vertexShaderFile, ShaderType.VERTEX_SHADER);
		shaderProgram.load(fragmentShaderFile, ShaderType.FRAGMENT_SHADER);
		shaderProgram.compile();
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

	private class QuitAction implements TriggerAction {
		@Override
		public void perform(InputState inputState) {
			done = true;
		}
	}

	private class FullscreenAction implements TriggerAction {
		@Override
		public void perform(InputState inputState) {
			final boolean isFullscreen = display.isFullscreen();

			if (!isFullscreen) {
				final List<DisplayConfig> availableFullscreenModes = display.getAvailableFullscreenModes();
				DisplayConfig largest = null;
				for (DisplayConfig availableFullscreenMode : availableFullscreenModes) {
					if (largest == null || largest.width() < availableFullscreenMode.width()) {
						largest = availableFullscreenMode;
					}
				}

				if (largest != null) {
					display.setDisplayConfig(largest);
					display.setFullscreen(true);

					fearGl.glViewport(0, 0, largest.width(), largest.height());
				} else {
					throw new FearError("No fullscreen mode found.");
				}
			} else {
				display.setDimensions(width, height);
				display.setFullscreen(false);
				fearGl.glViewport(0, 0, width, height);
			}

			display.setVSyncEnabled(!isFullscreen);
		}
	}
}
