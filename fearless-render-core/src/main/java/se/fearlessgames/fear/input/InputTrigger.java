package se.fearlessgames.fear.input;

import com.google.common.base.Predicate;

public class InputTrigger {
	private final Predicate<InputState> condition;
	private final TriggerAction action;

	public InputTrigger(TriggerAction action, Predicate<InputState> condition) {
		this.action = action;
		this.condition = condition;
	}

	public void performIfValid(InputState inputState) {
		if (condition.apply(inputState)) {
			action.perform(inputState);
		}
	}
}
