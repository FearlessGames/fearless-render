package se.fearlessgames.fear.input.hw.lwjgl;

import org.lwjgl.opengl.Display;
import se.fearlessgames.fear.input.hw.DisplayFocus;

public class LwjglDisplayFocus implements DisplayFocus {
	@Override
	public boolean hasFocus() {
		return !(Display.isActive() && Display.isVisible());
	}
}
