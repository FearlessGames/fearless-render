package se.fearlessgames.fear.input;

import java.util.EnumSet;

public class KeyboardState {
	public static final KeyboardState NOTHING = new KeyboardState(EnumSet.noneOf(Key.class), EnumSet.noneOf(Key.class));
	private final EnumSet<Key> keysDown;
	private final EnumSet<Key> keysUp;

	public KeyboardState(EnumSet<Key> keysDown, EnumSet<Key> keysUp) {
		this.keysDown = keysDown;
		this.keysUp = keysUp;
	}

	public EnumSet<Key> getKeysDown() {
		return keysDown;
	}

	public EnumSet<Key> getKeysUp() {
		return keysUp;
	}

	public boolean isDown(Key key) {
		return keysDown.contains(key);
	}
}
