package se.fearlessgames.fear.input;

public class MouseConfig {
	private int clickTimeMs;

	public MouseConfig(int clickTimeMs) {
		this.clickTimeMs = clickTimeMs;
	}

	public int clickTimeMs() {
		return clickTimeMs;
	}

	public void clickTimeMs(int clickTimeMs) {
		this.clickTimeMs = clickTimeMs;
	}
}
