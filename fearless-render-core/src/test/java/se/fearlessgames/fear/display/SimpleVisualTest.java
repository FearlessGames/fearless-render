package se.fearlessgames.fear.display;

import org.junit.Test;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.FearOutput;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.display.lwjgl.LwjglDisplayBuilder;
import se.fearlessgames.fear.gl.FearLwjgl;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.mesh.ShaderPopulator;

import static se.mockachino.Mockachino.mock;

public class SimpleVisualTest {
	@Test
	public void testDummy() {
	}

	public void testSimple() throws Exception {
		DisplayBuilder.Builder displayBuilder = new LwjglDisplayBuilder().createBuilder();
		displayBuilder.setDimensions(100, 100).setFullscreen(false).setTitle("Simple visual test");
		Display display = displayBuilder.build();
		FearLwjgl fearGl = new FearLwjgl();
		Renderer renderer = new Renderer(new MeshRenderer(fearGl, new ShaderPopulator()));
		Scene scene = new Scene(new Node());
		while (true) {
			if (hasHitEscape() || display.isCloseRequested()) {
				break;
			}

			scene.render(renderer, mock(Camera.class));
			display.update();

			Thread.sleep(100);
		}
		display.destroy();
	}

	private boolean hasHitEscape() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				return true;
			}
		}
		return false;
	}
}
