package se.fearlessgames.fear.input;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputHandler {
	private final InputController inputController;
	private List<InputTrigger> inputTriggers = new CopyOnWriteArrayList<InputTrigger>();


	public InputHandler(InputController inputController) {
		this.inputController = inputController;

	}

	public void poll() {
		InputState inputState = inputController.readInputStates();
		for (InputTrigger inputTrigger : inputTriggers) {
			inputTrigger.performIfValid(inputState);
		}
	}

	public void addTrigger(InputTrigger inputTrigger) {
		inputTriggers.add(inputTrigger);
	}

	public void removeTrigger(InputTrigger inputTrigger) {
		inputTriggers.remove(inputTrigger);
	}
}
