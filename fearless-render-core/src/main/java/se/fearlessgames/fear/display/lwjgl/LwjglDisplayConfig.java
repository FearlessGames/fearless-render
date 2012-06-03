package se.fearlessgames.fear.display.lwjgl;

import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.fear.display.DisplayConfig;

public class LwjglDisplayConfig implements DisplayConfig {
	private final DisplayMode displayMode;

	public LwjglDisplayConfig(DisplayMode displayMode) {
		this.displayMode = displayMode;
	}

	public LwjglDisplayConfig(int width, int height) {
		displayMode = new DisplayMode(width, height);
	}

	@Override
	public int width() {
		return displayMode.getWidth();
	}

	@Override
	public int height() {
		return displayMode.getHeight();
	}

	@Override
	public int frequency() {
		return displayMode.getFrequency();
	}

	@Override
	public int bitsPerPixel() {
		return displayMode.getBitsPerPixel();
	}

	public DisplayMode displayMode() {
		return displayMode;
	}
}
