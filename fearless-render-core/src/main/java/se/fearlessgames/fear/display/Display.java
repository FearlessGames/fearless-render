package se.fearlessgames.fear.display;

import se.fearlessgames.fear.GlCommand;

public interface Display {
	void destroy();

	void update();

	void add(GlCommand command);

	boolean isCloseRequested();

	boolean getFullscreen();

	void setFullscreen(boolean fullscreen);
}
