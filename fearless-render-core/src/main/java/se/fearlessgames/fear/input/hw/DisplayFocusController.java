package se.fearlessgames.fear.input.hw;

import org.lwjgl.opengl.Display;
import se.fearlessgames.fear.input.FocusController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayFocusController implements FocusController {
	private List<FocusListener> listeners = new CopyOnWriteArrayList<FocusListener>();
	private boolean hasFocus;

	public DisplayFocusController() {
		hasFocus = checkFocus();
	}

	private boolean checkFocus() {
		return !(Display.isActive() && Display.isVisible());
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
