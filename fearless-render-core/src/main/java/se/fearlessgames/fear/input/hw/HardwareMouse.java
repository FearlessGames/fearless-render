package se.fearlessgames.fear.input.hw;

import se.fearlessgames.fear.input.MouseButton;

public interface HardwareMouse {
	void grabbed(boolean grabbed);

	boolean grabbed();

	int buttonCount();

	boolean isButtonDown(MouseButton mouseButton);

	int eventX();

	int eventY();

	int eventDX();

	int eventDY();

	int eventDWheel();

	boolean next();
}
