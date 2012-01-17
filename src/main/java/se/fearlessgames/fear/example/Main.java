package se.fearlessgames.fear.example;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import se.fearlessgames.fear.Box;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*
* @author Stephen Jones
*/
public class Main {

	private boolean done = false; //game runs until done is set to true

	public Main() {
		init();

		Box box = new Box();
		long t1 = System.currentTimeMillis();
		long t2;
		int c = 0;
		while (!done) {
			if (Display.isCloseRequested())
				done = true;
			box.update();
			render(box);
			Display.update();
			t2 = System.currentTimeMillis();
			if ((c++ & 127) == 0) {
				System.out.printf("FPS: %.3f\n", 1000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();
	}

	private void render(Box box) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
				GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		box.draw();
	}

	private void init() {
		int w = 1024;
		int h = 768;

		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setVSyncEnabled(true);
			Display.setTitle("Shader Setup");
			Display.create();
		} catch (Exception e) {
			System.out.println("Error setting up display");
			System.exit(0);
		}

		GL11.glViewport(0, 0, w, h);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float) w / (float) h), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,
				GL11.GL_NICEST);
	}

	public static void main(String[] args) {
		new Main();
	}
}