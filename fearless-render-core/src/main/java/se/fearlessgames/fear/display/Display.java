package se.fearlessgames.fear.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.fear.FearError;
import se.fearlessgames.fear.FearOutput;
import se.fearlessgames.fear.GlCommand;

public class Display implements FearOutput {
	private static volatile boolean ACTIVE = false;
	private boolean destroyed;

	Display(int width, int height, boolean fullScreen) {
		if (ACTIVE) {
			throw new FearError("Multiple displays may not be active at the same time");
		}
		try {
			org.lwjgl.opengl.Display.setFullscreen(fullScreen);
			if (!fullScreen) {
				org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(width, height));
			}
			org.lwjgl.opengl.Display.setTitle("Title");
			org.lwjgl.opengl.Display.setVSyncEnabled(true);
			org.lwjgl.opengl.Display.create();
			ACTIVE = true;
			destroyed = false;
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	@Override
	public void finalize() {
		destroy();
	}

	private void destroy() {
		if (!destroyed) {
			destroyed = true;
			ACTIVE = false;
			org.lwjgl.opengl.Display.destroy();
		}
	}

	private void assertValid() {
		if (destroyed) {
			throw new FearError("Display is destroyed");
		}
	}

	public boolean isCloseRequested() {
		assertValid();
		return org.lwjgl.opengl.Display.isCloseRequested();
	}

	@Override
	public void flush() {
		assertValid();
		org.lwjgl.opengl.Display.update();
	}

	@Override
	public void add(GlCommand command) {
	}
}
