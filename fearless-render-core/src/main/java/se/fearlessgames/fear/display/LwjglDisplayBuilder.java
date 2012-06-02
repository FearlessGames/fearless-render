package se.fearlessgames.fear.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import se.fearlessgames.fear.FearError;

public class LwjglDisplayBuilder implements DisplayBuilder {
	@Override
	public Builder createBuilder() {
		return new JwglBuilder();
	}

	private static class JwglBuilder implements Builder {
		@Override
		public Builder setDimensions(int width, int height) {
			try {
				org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(width, height));
			} catch (LWJGLException e) {
				throw new FearError(e);
			}

			return this;
		}

		@Override
		public Builder setFullscreen(boolean fullscreen) {
			try {
				org.lwjgl.opengl.Display.setFullscreen(fullscreen);
			} catch (LWJGLException e) {
				throw new FearError(e);
			}

			return this;
		}

		@Override
		public Builder setVsyncEnabled(boolean enabled) {
			org.lwjgl.opengl.Display.setVSyncEnabled(enabled);

			return this;
		}

		@Override
		public Builder setTitle(String title) {
			org.lwjgl.opengl.Display.setTitle(title);

			return this;
		}

		@Override
		public Display build() {
			try {
				if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
					org.lwjgl.opengl.Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
				} else {
					org.lwjgl.opengl.Display.create();
				}
			} catch (LWJGLException e) {
				throw new FearError(e);
			}

			return new LwjglDisplay();
		}
	}
}
