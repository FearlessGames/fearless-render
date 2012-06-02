package se.fearlessgames.fear.input.hw.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import se.fearlessgames.fear.input.Key;
import se.fearlessgames.fear.input.LwjglKey;
import se.fearlessgames.fear.input.hw.HardwareKeyboard;

public class LwjglHardwareKeyboard implements HardwareKeyboard {

	public LwjglHardwareKeyboard() {
		if (!Keyboard.isCreated()) {
			try {
				Keyboard.create();
			} catch (final LWJGLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public boolean next() {
		return Keyboard.next();
	}

	@Override
	public Key eventKey() {
		return LwjglKey.findByKeyEventCode(Keyboard.getEventKey());
	}

	@Override
	public boolean eventKeyState() {
		return Keyboard.getEventKeyState();
	}

	@Override
	public char eventCharacter() {
		return Keyboard.getEventCharacter();
	}
}
