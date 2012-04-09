package se.fearlessgames.fear.input;

public class InputState {
	private final KeyboardState keyboardState;
	private final MouseState mouseState;

	public InputState(KeyboardState keyboardState, MouseState mouseState) {
		this.keyboardState = keyboardState;
		this.mouseState = mouseState;
	}

	public KeyboardState getKeyboardState() {
		return keyboardState;
	}

	public MouseState getMouseState() {
		return mouseState;
	}
}
