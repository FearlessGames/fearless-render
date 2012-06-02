package se.fearlessgames.fear.input.hw;

public interface HardwareKeyboard {
	boolean next();

	int eventKey();

	boolean eventKeyState();

	char eventCharacter();
}
