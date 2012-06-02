package se.fearlessgames.fear.input.hw;

import com.google.common.collect.Multiset;
import com.google.common.collect.PeekingIterator;
import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.input.MouseButton;
import se.fearlessgames.fear.input.MouseButtonState;
import se.fearlessgames.fear.input.MouseConfig;
import se.fearlessgames.fear.input.MouseState;
import se.mockachino.annotations.Mock;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.setupMocks;
import static se.mockachino.Mockachino.stubReturn;

public class HardwareMouseControllerTest {

	@Mock
	private HardwareMouse hardwareMouse;

	@Mock
	private TimeProvider timeProvider;


	private HardwareMouseController hardwareMouseController;

	@Before
	public void setUp() {
		setupMocks(this);
		hardwareMouseController = new HardwareMouseController(timeProvider, hardwareMouse, new MouseConfig(10));
	}

	@Test
	public void mouseMoveYieldsEvent() {
		stubReturn(false).on(hardwareMouse).next();
		final PeekingIterator<MouseState> events = hardwareMouseController.events();
		assertEquals(false, events.hasNext());

		final PeekingIterator<MouseState> events2 = hardwareMouseController.events();
		setUpHardwareMouseStubReturn(0, 1, 2, 3, 4);
		assertEquals(true, events2.hasNext());
		final MouseState mouseState = events2.next();
		assertEquals(0, mouseState.getX());
		assertEquals(1, mouseState.getY());
		assertEquals(2, mouseState.getDX());
		assertEquals(3, mouseState.getDY());
		assertEquals(4, mouseState.getDWheel());
	}

	@Test
	public void mouseDownStateEvent() {
		final PeekingIterator<MouseState> events = hardwareMouseController.events();
		setUpHardwareMouseStubReturn(0, 1, 2, 3, 4);
		stubReturn(1).on(hardwareMouse).buttonCount();
		stubReturn(true).on(hardwareMouse).isButtonDown(MouseButton.LEFT);

		assertEquals(true, events.hasNext());

		final MouseState mouseState = events.next();
		assertEquals(MouseButton.values().length, mouseState.getButtonStates().size());
		assertEquals(MouseButtonState.DOWN, mouseState.getButtonStates().get(MouseButton.LEFT));
		assertEquals(MouseButtonState.UP, mouseState.getButtonStates().get(MouseButton.RIGHT));
		final Multiset<MouseButton> clickCounts = mouseState.getClickCounts();
		assertEquals(0, clickCounts.size());
	}

	@Test
	public void clickEvent() {
		setUpHardwareMouseStubReturn(0, 0, 0, 0, 0);
		stubReturn(1).on(hardwareMouse).buttonCount();
		stubReturn(true).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		final PeekingIterator<MouseState> events = hardwareMouseController.events();
		events.next();

		stubReturn(false).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		final PeekingIterator<MouseState> events2 = hardwareMouseController.events();

		final MouseState mouseState = events2.next();
		assertEquals(MouseButton.values().length, mouseState.getButtonStates().size());
		assertEquals(MouseButtonState.UP, mouseState.getButtonStates().get(MouseButton.LEFT));
		final Multiset<MouseButton> clickCounts = mouseState.getClickCounts();
		assertEquals(1, clickCounts.size());
	}

	@Test
	public void noClickEventWithMovingMouse() {
		setUpHardwareMouseStubReturn(0, 0, 1, 0, 0);
		stubReturn(1).on(hardwareMouse).buttonCount();
		stubReturn(true).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		hardwareMouseController.events().next();

		stubReturn(false).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		final PeekingIterator<MouseState> events = hardwareMouseController.events();
		final Multiset<MouseButton> clickCounts = events.next().getClickCounts();
		assertEquals(0, clickCounts.size());
	}

	@Test
	public void noClickEventWhenDelayIsGreaterThanLimit() {
		setUpHardwareMouseStubReturn(0, 0, 0, 0, 0);
		stubReturn(1).on(hardwareMouse).buttonCount();
		stubReturn(true).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		hardwareMouseController.events().next();

		stubReturn(100l).on(timeProvider).now();

		stubReturn(false).on(hardwareMouse).isButtonDown(MouseButton.LEFT);
		final PeekingIterator<MouseState> events = hardwareMouseController.events();
		final Multiset<MouseButton> clickCounts = events.next().getClickCounts();
		assertEquals(0, clickCounts.size());
	}

	private void setUpHardwareMouseStubReturn(int x, int y, int dx, int dy, int dw) {
		stubReturn(true).on(hardwareMouse).next();
		stubReturn(x).on(hardwareMouse).eventX();
		stubReturn(y).on(hardwareMouse).eventY();
		stubReturn(dx).on(hardwareMouse).eventDX();
		stubReturn(dy).on(hardwareMouse).eventDY();
		stubReturn(dw).on(hardwareMouse).eventDWheel();
	}

}
