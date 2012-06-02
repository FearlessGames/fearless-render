package se.fearlessgames.fear.display.lwjgl;

import se.fearlessgames.fear.FearError;
import se.fearlessgames.fear.GlCommand;
import se.fearlessgames.fear.display.Display;

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
		return LwjglDisplayProxy.isCloseRequested();
	}

	@Override
	public boolean getFullscreen() {
		return LwjglDisplayProxy.isFullscreen();
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		LwjglDisplayProxy.setFullscreen(fullscreen);
	}

	@Override
	public void destroy() {
		if (ACTIVE) {
			LwjglDisplayProxy.destroy();
		}
	}

	@Override
	public void update() {
		assertValid();
		LwjglDisplayProxy.update();
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
