package se.fearlessgames.fear.input.hw;

import se.fearlessgames.fear.input.Key;

public interface HardwareKeyboard {
	boolean next();

	Key eventKey();

	boolean eventKeyState();

	char eventCharacter();
}
