package se.fearlessgames.fear.display;

public class DisplaySupplier {
	private Display display;
	private int width;
	private int height;
	private boolean fullscreen;

	public Display showDisplay() {
		if (display == null) {
			display = createDisplay();
		}
		return display;
	}

	private Display createDisplay() {
		return new Display(width, height, fullscreen);
	}

	public void setDimensions(int w, int h) {
		if (display != null) {
			throw new IllegalStateException("Display is already created, can't change dimensions now");
		}
		width = w;
		height = h;
	}

	public void setFullscreen() {
		if (display != null) {
			throw new IllegalStateException("Display is already created, can't change fullscreen now");
		}
		this.fullscreen = true;
	}
}
