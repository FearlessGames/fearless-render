package se.fearlessgames.fear.input;

import com.google.common.collect.PeekingIterator;

public interface KeyboardController {
	PeekingIterator<KeyEvent> getEvents();
}
