package se.fearlessgames.fear.input;

import com.google.common.collect.PeekingIterator;

public interface MouseController {
	PeekingIterator<MouseState> events();

	void grabbed(boolean grabbed);

	boolean grabbed();
}
