package se.fearlessgames.fear.display;

public interface DisplayBuilder {
	Builder createBuilder();

	public interface Builder {
		Builder setDimensions(int width, int height);

		Builder setFullscreen(boolean fullScreen);

		Builder setVsyncEnabled(boolean enabled);

		Builder setTitle(String title);

		Display build();
	}
}
