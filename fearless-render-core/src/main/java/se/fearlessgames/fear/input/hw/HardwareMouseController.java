package se.fearlessgames.fear.input.hw;

import com.google.common.collect.*;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.input.*;

import java.util.EnumMap;
import java.util.EnumSet;

public class HardwareMouseController implements MouseController {
	private final EnumMultiset<MouseButton> EMPTY_MOUSE_BUTTON_MULTISET = EnumMultiset.create(MouseButton.class);

	private final EnumMap<MouseButton, Long> lastClickTime = Maps.newEnumMap(MouseButton.class);
	private final Multiset<MouseButton> clicks = EnumMultiset.create(MouseButton.class);
	private final EnumSet<MouseButton> activeClickButtons = EnumSet.noneOf(MouseButton.class);
	private final TimeProvider timeProvider;

	private final HardwareMouse hardwareMouse;
	private final MouseConfig mouseConfig;
	private boolean sendClickState = false;
	private MouseState nextState;

	public HardwareMouseController(TimeProvider timeProvider, HardwareMouse hardwareMouse, MouseConfig mouseConfig) {
		this.timeProvider = timeProvider;
		this.hardwareMouse = hardwareMouse;
		this.mouseConfig = mouseConfig;
	}

	@Override
	public PeekingIterator<MouseState> events() {
		return new MouseIterator();
	}

	@Override
	public void grabbed(boolean grabbed) {
		hardwareMouse.grabbed(grabbed);
	}

	@Override
	public boolean grabbed() {
		return hardwareMouse.grabbed();
	}

	private class MouseIterator extends AbstractIterator<MouseState> implements PeekingIterator<MouseState> {

		@Override
		protected MouseState computeNext() {
			if (nextState != null) {
				MouseState mouseState = nextState;
				nextState = null;
				return mouseState;
			}

			if (!hardwareMouse.next()) {
				return endOfData();
			}

			final EnumMap<MouseButton, MouseButtonState> buttons = Maps.newEnumMap(MouseButton.class);
			final MouseButton[] mouseButtonValues = MouseButton.values();

			for (MouseButton mouseButton : mouseButtonValues) {
				final MouseButtonState mouseButtonState = hardwareMouse.isButtonDown(mouseButton) ? MouseButtonState.DOWN : MouseButtonState.UP;
				processButtonForClick(mouseButton, mouseButtonState);
				buttons.put(mouseButton, mouseButtonState);
			}

			final MouseState nextMouseState = new MouseState(
					hardwareMouse.eventX(),
					hardwareMouse.eventY(),
					hardwareMouse.eventDX(),
					hardwareMouse.eventDY(),
					hardwareMouse.eventDWheel(),
					buttons,
					EMPTY_MOUSE_BUTTON_MULTISET);

			if (nextMouseState.getDX() != 0.0 || nextMouseState.getDY() != 0.0) {
				activeClickButtons.clear();
				clicks.clear();
				sendClickState = false;
			}

			if (sendClickState) {
				nextState = nextMouseState;
				sendClickState = false;
				return new MouseState(
						nextMouseState.getX(),
						nextMouseState.getY(),
						nextMouseState.getDX(),
						nextMouseState.getDY(),
						nextMouseState.getDWheel(),
						buttons,
						EnumMultiset.create(clicks));
			} else {
				return nextMouseState;
			}

		}

		private void processButtonForClick(MouseButton mouseButton, MouseButtonState mouseButtonState) {
			final long currentTime = timeProvider.now();
			boolean expired = false;

			if (lastClickTime.containsKey(mouseButton) && (currentTime - lastClickTime.get(mouseButton) > mouseConfig.clickTimeMs())) {
				clicks.setCount(mouseButton, 0);
				expired = true;
			}

			if (mouseButtonState == MouseButtonState.DOWN) {
				if (activeClickButtons.contains(mouseButton)) {
					clicks.setCount(mouseButton, 0);
				}

				activeClickButtons.add(mouseButton);
				lastClickTime.put(mouseButton, currentTime);
			} else {
				if (!expired && activeClickButtons.contains(mouseButton)) {
					clicks.add(mouseButton);
					sendClickState = true;
				} else {
					clicks.setCount(mouseButton, 0); // clear click count for button mouseButton.
				}
				activeClickButtons.remove(mouseButton);
			}
		}
	}
}
