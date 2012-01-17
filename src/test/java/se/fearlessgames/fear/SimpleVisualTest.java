package se.fearlessgames.fear;

import org.junit.Test;
import org.lwjgl.input.Keyboard;

public class SimpleVisualTest {
	@Test
	public void testSimple() throws Exception {
		FearDisplay output = new FearDisplay(100, 100, false);
		FearScene scene = new FearScene();
		boolean running = true;
		while (running) {
			while (Keyboard.next()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					running = false;
				}
			}
			if (output.isCloseRequested()) {
				running = false;
			}
			output.render(scene);
			Thread.sleep(100);
		}
	}
}
