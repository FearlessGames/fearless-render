package se.fearlessgames.fear.input;

import com.google.common.collect.PeekingIterator;

import java.util.EnumSet;

public class InputController {
	private final KeyboardController keyboardController;
	private final MouseController mouseController;

	private final EnumSet<Key> currentKeysDown = EnumSet.noneOf(Key.class);
	private MouseState mouseState = MouseState.NOTHING;

	public InputController(KeyboardController keyboardController, MouseController mouseController) {
		this.keyboardController = keyboardController;
		this.mouseController = mouseController;
	}

	public InputState readInputStates() {
		KeyboardState keyboardState = readKeyboardState();
		MouseState mouseState = readMouseState();
		return new InputState(keyboardState, mouseState);
	}


	private KeyboardState readKeyboardState() {

		PeekingIterator<KeyEvent> events = keyboardController.getEvents();

		EnumSet<Key> keysUp = EnumSet.noneOf(Key.class);

		while (events.hasNext()) {
			KeyEvent keyEvent = events.next();
			Key key = keyEvent.getKey();
			if (keyEvent.getKeyState() == KeyState.DOWN) {
				currentKeysDown.add(key);
			} else {
				keysUp.add(key);
				currentKeysDown.remove(key);
			}
		}

		return new KeyboardState(EnumSet.copyOf(currentKeysDown), keysUp);
	}

	private MouseState readMouseState() {
		PeekingIterator<MouseState> events = mouseController.getEvents();
		if (events.hasNext()) {
			mouseState = events.next();
		}

		return mouseState;
	}
}
