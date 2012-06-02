package se.fearlessgames.fear.input.hw;

import com.google.common.collect.PeekingIterator;
import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.input.Key;
import se.fearlessgames.fear.input.KeyEvent;
import se.fearlessgames.fear.input.KeyState;
import se.mockachino.annotations.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.setupMocks;
import static se.mockachino.Mockachino.stubReturn;

public class HardwareKeyboardControllerTest {

	@Mock
	private HardwareKeyboard hardwareKeyboard;

	private HardwareKeyboardController hardwareKeyboardController;

	@Before
	public void setUp() throws Exception {
		setupMocks(this);

		hardwareKeyboardController = new HardwareKeyboardController(hardwareKeyboard);
	}

	@Test
	public void keyDownEvent() throws Exception {
		stubReturn(true).on(hardwareKeyboard).next();
		stubReturn('A').on(hardwareKeyboard).eventCharacter();
		stubReturn(30).on(hardwareKeyboard).eventKey();
		stubReturn(true).on(hardwareKeyboard).eventKeyState();

		final PeekingIterator<KeyEvent> events = hardwareKeyboardController.getEvents();
		assertTrue(events.hasNext());
		final KeyEvent keyEvent = events.next();
		assertEquals(Key.A, keyEvent.key());
		assertEquals('A', keyEvent.keyChar());
		assertEquals(KeyState.DOWN, keyEvent.keyState());
	}
}
