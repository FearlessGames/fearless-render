package se.fearlessgames.fear.input;

import com.google.common.collect.PeekingIterator;

import java.util.EnumSet;

public class InputController implements FocusController.FocusListener {
	private final KeyboardController keyboardController;
	private final MouseController mouseController;
	private final FocusController focusController;

	private final EnumSet<Key> currentKeysDown = EnumSet.noneOf(Key.class);
	private MouseState mouseState = MouseState.NOTHING;

	public InputController(KeyboardController keyboardController, MouseController mouseController, FocusController focusController) {
		this.keyboardController = keyboardController;
		this.mouseController = mouseController;
		this.focusController = focusController;
		this.focusController.addListener(this);
	}

	public InputState readInputStates() {
		focusController.poll();
		KeyboardState keyboardState = readKeyboardState();
		MouseState mouseState = readMouseState();
		return new InputState(keyboardState, mouseState);
	}

	@Override
	public void focusChanged(boolean hasFocus) {
		currentKeysDown.clear();
		mouseState = MouseState.NOTHING;
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
		} else {
			mouseState = new MouseState(mouseState.getX(), mouseState.getY(), 0, 0, 0, mouseState.getButtonStates(), mouseState.getClickCounts());
		}

		return mouseState;
	}


}
