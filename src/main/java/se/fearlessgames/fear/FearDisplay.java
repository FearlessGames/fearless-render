package se.fearlessgames.fear;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class FearDisplay implements FearOutput {
	private static final FearDisplay DISPLAY = new FearDisplay();

	private boolean destroyed;

	public static FearDisplay getDISPLAY() {
		return DISPLAY;
	}

	private FearDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(100, 100));
			Display.setVSyncEnabled(true);
			Display.setTitle("Title");
			Display.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			throw new Error(e);
		}

	}

	public void finalize() {
		if (!destroyed) {
			destroyed = true;
			Display.destroy();
		}
	}

	public void setDimensions(int width, int height) throws LWJGLException {
		assertValid();
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setFullscreen(false);
	}

	private void assertValid() throws LWJGLException {
		if (destroyed) {
			throw new LWJGLException("Display is destroyed");
		}
	}

	public void fullScreen() throws LWJGLException {
		assertValid();
		Display.setFullscreen(true);
	}

	@Override
	public void render(FearScene scene) throws LWJGLException {
		assertValid();
	}
}
