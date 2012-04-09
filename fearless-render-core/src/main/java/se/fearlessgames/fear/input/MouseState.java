package se.fearlessgames.fear.input;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

import java.util.EnumMap;

public class MouseState {
	public static final MouseState NOTHING = new MouseState(0, 0, 0, 0, 0, null, null);

	private final int x;
	private final int y;
	private final int dx;
	private final int dy;
	private final int dwheel;
	private final ImmutableMap<MouseButton, MouseButtonState> buttonStates;
	private final ImmutableMultiset<MouseButton> clickCounts;

	public MouseState(final int x, final int y, final int dx, final int dy, final int dwheel,
					  final EnumMap<MouseButton, MouseButtonState> buttonStates, final Multiset<MouseButton> clicks) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.dwheel = dwheel;
		if (buttonStates != null) {
			final com.google.common.collect.ImmutableMap.Builder<MouseButton, MouseButtonState> builder = ImmutableMap
					.builder();
			this.buttonStates = builder.putAll(buttonStates).build();
		} else {
			this.buttonStates = ImmutableMap.of();
		}
		if (clicks != null) {
			final ImmutableMultiset.Builder<MouseButton> builder = ImmutableMultiset.builder();
			clickCounts = builder.addAll(clicks).build();
		} else {
			clickCounts = ImmutableMultiset.of();
		}
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

	public ImmutableMap<MouseButton, MouseButtonState> getButtonStates() {
		return buttonStates;
	}

	public ImmutableMultiset<MouseButton> getClickCounts() {
		return clickCounts;
	}
}
