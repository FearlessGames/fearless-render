package se.fearlessgames.fear.input;

import com.google.common.collect.Multiset;

import java.util.EnumMap;

public class MouseState {
	public static final MouseState NOTHING = new MouseState(0, 0, 0, 0, 0, null, null);

	private final int x;
	private final int y;
	private final int dx;
	private final int dy;
	private final int dwheel;
	private final EnumMap<MouseButton, MouseButtonState> buttonStates;
	private final Multiset<MouseButton> clickCounts;

	public MouseState(final int x, final int y, final int dx, final int dy, final int dwheel,
					  final EnumMap<MouseButton, MouseButtonState> buttonStates, final Multiset<MouseButton> clickCounts) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.dwheel = dwheel;

		this.buttonStates = buttonStates;
		this.clickCounts = clickCounts;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getDwheel() {
		return dwheel;
	}

	public EnumMap<MouseButton, MouseButtonState> getButtonStates() {
		return buttonStates;
	}

	public Multiset<MouseButton> getClickCounts() {
		return clickCounts;
	}
}
