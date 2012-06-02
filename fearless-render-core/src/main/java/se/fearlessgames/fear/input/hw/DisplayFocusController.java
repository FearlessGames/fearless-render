package se.fearlessgames.fear.input.hw;

import se.fearlessgames.fear.input.FocusController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayFocusController implements FocusController {
	private final DisplayFocus displayFocus;
	private List<FocusListener> listeners = new CopyOnWriteArrayList<FocusListener>();
	private boolean hasFocus;

	public DisplayFocusController(DisplayFocus displayFocus) {
		this.displayFocus = displayFocus;

		hasFocus = checkFocus();
	}

	private boolean checkFocus() {
		return displayFocus.hasFocus();
	}

	@Override
	public void poll() {
		boolean currentFocus = checkFocus();
		if (currentFocus != hasFocus) {
			hasFocus = currentFocus;
			trigger();
		}
	}

	private void trigger() {
		for (FocusListener listener : listeners) {
			listener.focusChanged(hasFocus);
		}
	}

	@Override
	public void addListener(FocusListener focusListener) {
		listeners.add(focusListener);
	}
}
