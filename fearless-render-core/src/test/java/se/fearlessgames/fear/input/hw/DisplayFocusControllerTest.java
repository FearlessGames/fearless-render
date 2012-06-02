package se.fearlessgames.fear.input.hw;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.input.FocusController;
import se.mockachino.annotations.Mock;

import static se.mockachino.Mockachino.*;

public class DisplayFocusControllerTest {

	@Mock
	private DisplayFocus displayFocus;

	private DisplayFocusController displayFocusController;

	@Before
	public void setUp() throws Exception {
		setupMocks(this);

		displayFocusController = new DisplayFocusController(displayFocus);
	}

	@Test
	public void listenerFiresOnStateChange() {
		final FocusController.FocusListener mockListener = mock(FocusController.FocusListener.class);
		displayFocusController.addListener(mockListener);

		stubReturn(false).on(displayFocus).hasFocus();
		displayFocusController.poll();
		stubReturn(true).on(displayFocus).hasFocus();
		displayFocusController.poll();

		verifyOnce().on(mockListener).focusChanged(true);
	}

	@Test
	public void listenerDoesNotFireWhenNoStateChange() {
		final FocusController.FocusListener mockListener = mock(FocusController.FocusListener.class);
		displayFocusController.addListener(mockListener);

		stubReturn(false).on(displayFocus).hasFocus();
		displayFocusController.poll();
		stubReturn(false).on(displayFocus).hasFocus();
		displayFocusController.poll();

		verifyNever().on(mockListener).focusChanged(true);
	}
}
