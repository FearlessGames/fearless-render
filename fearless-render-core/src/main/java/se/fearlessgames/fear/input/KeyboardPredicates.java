package se.fearlessgames.fear.input;

import com.google.common.base.Predicate;

public class KeyboardPredicates {
	public static final Predicate<InputState> ANY_KEY_DOWN = new AnyKeyDown();

	public static Predicate<InputState> singleKey(final Key key) {
		return new Predicate<InputState>() {
			@Override
			public boolean apply(InputState input) {
				KeyboardState keyboardState = input.getKeyboardState();
				return (keyboardState.isDown(key));
			}
		};
	}

	private static class AnyKeyDown implements Predicate<InputState> {
		@Override
		public boolean apply(InputState input) {
			KeyboardState keyboardState = input.getKeyboardState();
			return !(keyboardState.getKeysDown().isEmpty());
		}
	}
}
