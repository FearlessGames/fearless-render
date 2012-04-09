package se.fearlessgames.fear.input.hw;

import com.google.common.collect.*;
import org.lwjgl.input.Mouse;
import se.fearlessgames.fear.input.MouseButton;
import se.fearlessgames.fear.input.MouseButtonState;
import se.fearlessgames.fear.input.MouseController;
import se.fearlessgames.fear.input.MouseState;

import java.util.EnumMap;
import java.util.EnumSet;

public class HardwareMouseController implements MouseController {
	private final static PeekingIterator<MouseState> mouseIterator = new MouseIterator();

	public HardwareMouseController() {
		if (!Mouse.isCreated()) {
			try {
				Mouse.create();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public PeekingIterator<MouseState> getEvents() {
		return mouseIterator;
	}

	private static class MouseIterator extends AbstractIterator<MouseState> implements PeekingIterator<MouseState> {
		private static long CLICK_TIME_MS = 500;
		private final EnumMap<MouseButton, Long> lastClickTime = Maps.newEnumMap(MouseButton.class);
		private final Multiset<MouseButton> clicks = EnumMultiset.create(MouseButton.class);
		private final EnumSet<MouseButton> clickArmed = EnumSet.noneOf(MouseButton.class);

		private boolean sendClickState = false;
		private MouseState nextState;

		@Override
		protected MouseState computeNext() {
			if (nextState != null) {
				MouseState nextState = this.nextState;
				this.nextState = null;
				return nextState;
			}

			if (!Mouse.next()) {
				return endOfData();
			}

			final EnumMap<MouseButton, MouseButtonState> buttons = Maps.newEnumMap(MouseButton.class);

			for (int buttonNr = 0; buttonNr < Mouse.getButtonCount(); buttonNr++) {
				MouseButton mouseButton = MouseButton.values()[buttonNr];
				boolean pressed = Mouse.isButtonDown(buttonNr);
				processButtonForClick(mouseButton, pressed);
				buttons.put(mouseButton, pressed ? MouseButtonState.DOWN : MouseButtonState.UP);
			}


			final MouseState nextState = new MouseState(
					Mouse.getEventX(),
					Mouse.getEventY(),
					Mouse.getEventDX(),
					Mouse.getEventDY(),
					Mouse.getEventDWheel(),
					buttons,
					null);

			if (nextState.getDx() != 0.0 || nextState.getDy() != 0.0) {
				clickArmed.clear();
				clicks.clear();
				sendClickState = false;
			}

			if (sendClickState) {
				this.nextState = nextState;
				sendClickState = false;
				return new MouseState(
						nextState.getX(),
						nextState.getY(),
						nextState.getDx(),
						nextState.getDy(),
						nextState.getDwheel(),
						buttons,
						EnumMultiset.create(clicks));
			} else {
				return nextState;
			}

		}

		private void processButtonForClick(MouseButton b, boolean down) {
			boolean expired = false;
			long currentTime = System.currentTimeMillis();
			if (lastClickTime.containsKey(b) && (currentTime - lastClickTime.get(b) > CLICK_TIME_MS)) {
				clicks.setCount(b, 0);
				expired = true;
			}
			if (down) {
				if (clickArmed.contains(b)) {
					clicks.setCount(b, 0);
				}
				clickArmed.add(b);
				lastClickTime.put(b, currentTime);
			} else {
				if (!expired && clickArmed.contains(b)) {
					clicks.add(b);
					sendClickState = true;
				} else {
					clicks.setCount(b, 0); // clear click count for button b.
				}
				clickArmed.remove(b);
			}
		}
	}
}
