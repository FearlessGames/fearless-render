package se.fearlessgames.fear.input;

public interface FocusController {
	void poll();

	void addListener(FocusListener focusListener);

	public interface FocusListener {
		void focusChanged(boolean hasFocus);
	}
}
