package se.fearlessgames.fear.display;

import org.junit.Test;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.camera.VectorCamera;
import se.fearlessgames.fear.gl.FearLwjgl;
import se.fearlessgames.fear.mesh.MeshRenderer;

import static se.mockachino.Mockachino.mock;

public class SimpleVisualTest {
	@Test
	public void testDummy() {
	}

	public void testSimple() throws Exception {
		final DisplayBuilder displayBuilder = new LwjglDisplayBuilder();
		final Display display = displayBuilder.createBuilder().setDimensions(100, 100).build();
		final FearLwjgl fearGl = new FearLwjgl();
		final Renderer renderer = new Renderer(new MeshRenderer(fearGl));

		final Scene scene = new Scene(new Node());
		while (true) {
			if (hasHitEscape() || display.isCloseRequested()) {
				break;
			}

			scene.render(renderer, mock(VectorCamera.class));
			display.update();

			Thread.sleep(100);
		}
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
