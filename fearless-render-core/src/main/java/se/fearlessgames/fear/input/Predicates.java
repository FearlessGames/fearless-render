package se.fearlessgames.fear.input;

import com.google.common.base.Predicate;

public class Predicates {
	public static final Predicate<InputState> ANY_KEY_DOWN = new AnyKeyDown();


	private static class AnyKeyDown implements Predicate<InputState> {
		@Override
		public boolean apply(InputState input) {
			KeyboardState keyboardState = input.getKeyboardState();
			return !(keyboardState.getKeysDown().isEmpty());
		}
	}
}
