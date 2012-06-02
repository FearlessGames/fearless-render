package se.fearlessgames.fear.input;

public class KeyEvent {
	private final Key key;
	private final KeyState keyState;
	private final char keyChar;

	public KeyEvent(Key key, KeyState keyState, char keyChar) {
		this.key = key;
		this.keyState = keyState;
		this.keyChar = keyChar;
	}

	public Key key() {
		return key;
	}

	public KeyState keyState() {
		return keyState;
	}

	public char keyChar() {
		return keyChar;
	}
}
