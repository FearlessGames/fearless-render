package se.fearlessgames.fear.display.lwjgl;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.fear.FearError;
import se.fearlessgames.fear.GlCommand;
import se.fearlessgames.fear.display.Display;
import se.fearlessgames.fear.display.DisplayConfig;

import java.util.List;

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
	public boolean isFullscreen() {
		return LwjglDisplayProxy.isFullscreen();
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		LwjglDisplayProxy.setFullscreen(fullscreen);
	}

	@Override
	public void setVSyncEnabled(boolean enabled) {
		LwjglDisplayProxy.setVSyncEnabled(enabled);
	}

	@Override
	public List<DisplayConfig> getAvailableFullscreenModes() {
		final List<DisplayConfig> displayConfigs = Lists.newArrayList();
		final DisplayMode[] availableDisplayModes = LwjglDisplayProxy.getAvailableDisplayModes();

		for (DisplayMode displayMode : availableDisplayModes) {
			if (displayMode.isFullscreenCapable()) {
				displayConfigs.add(new LwjglDisplayConfig(displayMode));
			}
		}

		return displayConfigs;
	}

	@Override
	public void setDisplayConfig(DisplayConfig displayConfig) {
		LwjglDisplayProxy.setDisplayMode(((LwjglDisplayConfig) displayConfig).displayMode());
	}

	@Override
	public void setDimensions(int width, int height) {
		LwjglDisplayProxy.setDimensions(width, height);
	}

	@Override
	public void destroy() {
		assertValid();
		LwjglDisplayProxy.destroy();
		ACTIVE = false;
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
