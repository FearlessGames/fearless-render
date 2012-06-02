package se.fearlessgames.fear.display;

import se.fearlessgames.fear.FearError;
import se.fearlessgames.fear.GlCommand;

public class LwjglDisplay implements Display {
	private static volatile boolean ACTIVE = false;

	public LwjglDisplay() {
		if (ACTIVE) {
			throw new FearError("Multiple displays may not be active at the same time.");
		} else {
			ACTIVE = true;
		}
	}

	@Override
	public boolean isCloseRequested() {
		assertValid();
		return org.lwjgl.opengl.Display.isCloseRequested();
	}

	@Override
	public void destroy() {
		if (ACTIVE) {
			org.lwjgl.opengl.Display.destroy();
		}
	}

	@Override
	public void update() {
		assertValid();
		org.lwjgl.opengl.Display.update();
	}

	@Override
	public void add(GlCommand command) {
	}

	protected void assertValid() {
		if (!ACTIVE) {
			throw new FearError("Display is destroyed");
		}
	}
}
