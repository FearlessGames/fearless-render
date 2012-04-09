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
	private static long CLICK_TIME_MS = 500;
	private final EnumMap<MouseButton, Long> lastClickTime = Maps.newEnumMap(MouseButton.class);
	private final Multiset<MouseButton> clicks = EnumMultiset.create(MouseButton.class);
	private final EnumSet<MouseButton> clickArmed = EnumSet.noneOf(MouseButton.class);

	private boolean sendClickState = false;
	private MouseState nextState;

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
		return new MouseIterator();
	}

	private class MouseIterator extends AbstractIterator<MouseState> implements PeekingIterator<MouseState> {


		@Override
		protected MouseState computeNext() {
			if (nextState != null) {
				MouseState ns = nextState;
				nextState = null;
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


			final MouseState ns = new MouseState(
					Mouse.getEventX(),
					Mouse.getEventY(),
					Mouse.getEventDX(),
					Mouse.getEventDY(),
					Mouse.getEventDWheel(),
					buttons,
					null);

			if (ns.getDx() != 0.0 || ns.getDy() != 0.0) {
				clickArmed.clear();
				clicks.clear();
				sendClickState = false;
			}

			if (sendClickState) {
				nextState = ns;
				sendClickState = false;
				return new MouseState(
						ns.getX(),
						ns.getY(),
						ns.getDx(),
						ns.getDy(),
						ns.getDwheel(),
						buttons,
						EnumMultiset.create(clicks));
			} else {
				return ns;
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
