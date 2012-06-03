package se.fearlessgames.fear.display.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import se.fearlessgames.fear.FearError;

public class LwjglDisplayProxy {

	public static void setDimensions(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	public static void setDisplayMode(DisplayMode displayMode) {
		try {
			Display.setDisplayMode(displayMode);
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	public static void setFullscreen(boolean fullscreen) {
		try {
			if (fullscreen) {
				if (!Display.getDisplayMode().isFullscreenCapable()) {
					throw new FearError("Attempting to set fullscreen on a display mode that is not fullscreen capable.");
				}
			}
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	public static void setVSyncEnabled(boolean enabled) {
		Display.setVSyncEnabled(enabled);
	}

	public static void setTitle(String title) {
		Display.setTitle(title);
	}

	public static boolean isFullscreen() {
		return Display.isFullscreen();
	}

	public static void create() {
		try {
			if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
				Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
			} else {
				Display.create();
			}
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}

	public static void update() {
		Display.update();
	}

	public static void destroy() {
		Display.destroy();
	}

	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public static DisplayMode[] getAvailableDisplayModes() {
		try {
			return Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			throw new FearError(e);
		}
	}
}
