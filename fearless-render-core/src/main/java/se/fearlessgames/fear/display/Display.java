package se.fearlessgames.fear.display;

import se.fearlessgames.fear.GlCommand;

import java.util.List;

public interface Display {
	void destroy();

	void update();

	void add(GlCommand command);

	boolean isCloseRequested();

	boolean isFullscreen();

	void setFullscreen(boolean fullscreen);

	void setVSyncEnabled(boolean enabled);

	List<DisplayConfig> getAvailableFullscreenModes();

	void setDisplayConfig(DisplayConfig displayConfig);

	void setDimensions(int width, int height);
}
