package se.fearlessgames.fear.input;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import java.util.EnumMap;
import java.util.Map;

public class MousePredicates {
	public static final Predicate<InputState> MOUSE_MOVED_CONDITION = new MouseMovedCondition();
	public static final MouseButtonCondition LEFT_DOWN_CONDITION = new MouseButtonCondition(MouseButton.LEFT, MouseButtonState.DOWN);
	public static final MouseButtonCondition RIGHT_DOWN_CONDITION = new MouseButtonCondition(MouseButton.RIGHT, MouseButtonState.DOWN);
	public static final MouseButtonCondition MIDDLE_DOWN_CONDITION = new MouseButtonCondition(MouseButton.MIDDLE, MouseButtonState.DOWN);

	private static class MouseMovedCondition implements Predicate<InputState> {
		public boolean apply(final InputState state) {
			return state.getMouseState().getDX() != 0 || state.getMouseState().getDY() != 0;
		}
	}

	public static class MouseButtonCondition implements Predicate<InputState> {
		private final EnumMap<MouseButton, MouseButtonState> states = Maps.newEnumMap(MouseButton.class);

		public MouseButtonCondition(final EnumMap<MouseButton, MouseButtonState> states) {
			this.states.putAll(states);
		}

		public MouseButtonCondition(final MouseButtonState left, final MouseButtonState right, final MouseButtonState middle) {
			if (left != MouseButtonState.UNDEFINED) {
				states.put(MouseButton.LEFT, left);
			}
			if (left != MouseButtonState.UNDEFINED) {
				states.put(MouseButton.RIGHT, right);
			}
			if (left != MouseButtonState.UNDEFINED) {
				states.put(MouseButton.MIDDLE, middle);
			}
		}

		public MouseButtonCondition(MouseButton button, MouseButtonState state) {
			for (final MouseButton b : MouseButton.values()) {
				states.put(b, button != b ? MouseButtonState.UNDEFINED : state);
			}
		}

		@Override
		public boolean apply(final InputState inputState) {
			Map<MouseButton, MouseButtonState> buttonStates = inputState.getMouseState().getButtonStates();
			for (final MouseButton button : buttonStates.keySet()) {
				final MouseButtonState required = states.get(button);
				if (required != MouseButtonState.UNDEFINED) {
					if (buttonStates.get(button) != required) {
						return false;
					}
				}
			}
			return true;
		}

	}
}
