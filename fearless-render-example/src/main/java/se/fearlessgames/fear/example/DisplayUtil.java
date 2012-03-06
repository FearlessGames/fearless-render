package se.fearlessgames.fear.example;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayUtil {
	public static void create(int w, int h, String title) throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(w, h));
		Display.setVSyncEnabled(true);
		Display.setTitle(title);
		if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
			Display.create(new PixelFormat(8, 24, 0), new ContextAttribs(3, 2).withProfileCore(true));
		} else {
			Display.create();
		}
	}
}
