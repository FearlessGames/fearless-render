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

	public Key getKey() {
		return key;
	}

	public KeyState getKeyState() {
		return keyState;
	}

	public char getKeyChar() {
		return keyChar;
	}
}
