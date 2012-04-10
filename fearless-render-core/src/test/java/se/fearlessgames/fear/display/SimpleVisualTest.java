package se.fearlessgames.fear.display;

import org.junit.Test;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.FearOutput;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.gl.FearLwjgl;
import se.fearlessgames.fear.mesh.MeshRenderer;

import static se.mockachino.Mockachino.mock;

public class SimpleVisualTest {
	@Test
	public void testDummy() {
	}

	public void testSimple() throws Exception {
		DisplaySupplier supplier = new DisplaySupplier();
		supplier.setDimensions(100, 100);
		Display output = supplier.showDisplay();
		FearLwjgl fearGl = new FearLwjgl();
		Renderer renderer = new Renderer(new MeshRenderer(fearGl));
		Scene scene = new Scene(new Node());
		while (true) {
			if (hasHitEscape() || output.isCloseRequested()) {
				break;
			}

			scene.render(renderer, mock(Camera.class));
			renderUI(output);
			output.flush();

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

	private void renderUI(FearOutput output) {
	}
}
