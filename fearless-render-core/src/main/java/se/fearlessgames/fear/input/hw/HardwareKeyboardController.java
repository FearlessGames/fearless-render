package se.fearlessgames.fear.input.hw;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.PeekingIterator;
import se.fearlessgames.fear.input.Key;
import se.fearlessgames.fear.input.KeyEvent;
import se.fearlessgames.fear.input.KeyState;
import se.fearlessgames.fear.input.KeyboardController;

public class HardwareKeyboardController implements KeyboardController {
	private final HardwareKeyboard hardwareKeyboard;

	public HardwareKeyboardController(HardwareKeyboard hardwareKeyboard) {
		this.hardwareKeyboard = hardwareKeyboard;
	}

	@Override
	public PeekingIterator<KeyEvent> getEvents() {
		return new KeyEventIterator(hardwareKeyboard);
	}

	private static class KeyEventIterator extends AbstractIterator<KeyEvent> implements PeekingIterator<KeyEvent> {

		private final HardwareKeyboard hardwareKeyboard;

		public KeyEventIterator(HardwareKeyboard hardwareKeyboard) {
			this.hardwareKeyboard = hardwareKeyboard;
		}

		@Override
		protected KeyEvent computeNext() {
			if (!hardwareKeyboard.next()) {
				return endOfData();
			}

			final Key key = hardwareKeyboard.eventKey();
			final boolean pressed = hardwareKeyboard.eventKeyState();
			final char keyChar = hardwareKeyboard.eventCharacter();

			return new KeyEvent(key, pressed ? KeyState.DOWN : KeyState.UP, keyChar);
		}
	}

}
