package se.fearlessgames.fear.display.lwjgl;

import se.fearlessgames.fear.display.Display;
import se.fearlessgames.fear.display.DisplayBuilder;

public class LwjglDisplayBuilder implements DisplayBuilder {
	@Override
	public Builder createBuilder() {
		return new JwglBuilder();
	}

	public static class JwglBuilder implements Builder {
		@Override
		public Builder setDimensions(int width, int height) {
			LwjglDisplayProxy.setDimensions(width, height);

			return this;
		}

		@Override
		public Builder setFullscreen(boolean fullscreen) {
			LwjglDisplayProxy.setFullscreen(fullscreen);

			return this;
		}

		@Override
		public Builder setVsyncEnabled(boolean enabled) {
			LwjglDisplayProxy.setVSyncEnabled(enabled);

			return this;
		}

		@Override
		public Builder setTitle(String title) {
			LwjglDisplayProxy.setTitle(title);

			return this;
		}

		@Override
		public Display build() {
			LwjglDisplayProxy.create();

			return new LwjglDisplay();
		}
	}
}
