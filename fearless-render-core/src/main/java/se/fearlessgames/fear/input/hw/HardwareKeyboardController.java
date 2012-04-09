package se.fearlessgames.fear.input.hw;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.PeekingIterator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.input.Key;
import se.fearlessgames.fear.input.KeyEvent;
import se.fearlessgames.fear.input.KeyState;
import se.fearlessgames.fear.input.KeyboardController;

public class HardwareKeyboardController implements KeyboardController {
	public HardwareKeyboardController() {
		if (!Keyboard.isCreated()) {
			try {
				Keyboard.create();
			} catch (final LWJGLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public PeekingIterator<KeyEvent> getEvents() {
		return new KeyEventIterator();
	}

	private static class KeyEventIterator extends AbstractIterator<KeyEvent> implements PeekingIterator<KeyEvent> {

		@Override
		protected KeyEvent computeNext() {

			if (!Keyboard.next()) {
				return endOfData();
			}
			final int keyCode = Keyboard.getEventKey();
			final boolean pressed = Keyboard.getEventKeyState();
			final char keyChar = Keyboard.getEventCharacter();

			Key key = Key.findByKeyEventCode(keyCode);

			return new KeyEvent(key, pressed ? KeyState.DOWN : KeyState.UP, keyChar);
		}
	}

}
