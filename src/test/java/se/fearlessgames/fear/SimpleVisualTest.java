package se.fearlessgames.fear;

import org.junit.Test;
import org.lwjgl.input.Keyboard;

public class SimpleVisualTest {
	@Test
	public void testSimple() throws Exception {
		FearDisplay output = new FearDisplay(100, 100, false);
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
