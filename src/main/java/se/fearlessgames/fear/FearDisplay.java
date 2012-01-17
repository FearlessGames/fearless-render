package se.fearlessgames.fear;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class FearDisplay implements FearOutput {
	private static volatile boolean ACTIVE = false;
	private boolean destroyed;

	public FearDisplay(int width, int height, boolean fullScreen) {
		if (ACTIVE) {
			throw new FearError("Multiple displays may not be active at the same time");
		}
		try {
			Display.setFullscreen(fullScreen);
			if (!fullScreen) {
				Display.setDisplayMode(new DisplayMode(width, height));
			}
			Display.setTitle("Title");
			Display.setVSyncEnabled(true);
			Display.create();
			ACTIVE = true;
			destroyed = false;
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	public void finalize() {
		destroy();
	}

	private void destroy() {
		if (!destroyed) {
			destroyed = true;
			ACTIVE = false;
			Display.destroy();
		}
	}

	private void assertValid() {
		if (destroyed) {
			throw new FearError("Display is destroyed");
		}
	}

	public boolean isCloseRequested() {
		assertValid();
		return Display.isCloseRequested();
	}

	@Override
	public void flush() {
		assertValid();
		Display.update();
	}
}
