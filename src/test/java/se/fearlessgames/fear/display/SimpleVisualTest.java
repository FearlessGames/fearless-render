package se.fearlessgames.fear.display;

import org.junit.Test;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.FearOutput;
import se.fearlessgames.fear.FearScene;
import se.fearlessgames.fear.display.FearDisplay;

public class SimpleVisualTest {
	@Test
	public void testSimple() throws Exception {
		DisplaySupplier supplier = new DisplaySupplier();
		supplier.setDimensions(100, 100);
		FearDisplay output = supplier.showDisplay();
		FearScene scene = new FearScene();
		while (true) {
			if (hasHitEscape() || output.isCloseRequested()) {
				break;
			}

			scene.render(output);
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
