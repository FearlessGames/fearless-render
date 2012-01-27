package se.fearlessgames.fear.example;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import se.fearlessgames.fear.VboBox;
import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.math.PerspectiveBuilder;

import java.util.EnumSet;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*
* @author Stephen Jones
*/
public class Main {

	private boolean done = false; //game runs until done is set to true
	private PerspectiveBuilder perspectiveBuilder;
	private final FearGl fearGl;

	public Main() {
		fearGl = new FearLwjgl();
		init();

		VboBox obj = new VboBox(fearGl);

		long t1 = System.nanoTime();
		long t2;
		int c = 0;
		while (!done) {
			if (Display.isCloseRequested()) {
				done = true;
			}
			obj.update();
			render(obj);
			Display.update();
			t2 = System.nanoTime();
			if ((c++ & 127) == 0) {
				System.out.printf("FPS: %.3f\n", 1000000000.0d / (t2 - t1));
			}
			t1 = t2;
		}

		Display.destroy();

	}


	private void render(VboBox vboBox) {
		fearGl.glClear(EnumSet.of(ClearBit.GL_COLOR_BUFFER_BIT, ClearBit.GL_DEPTH_BUFFER_BIT));
		vboBox.draw(perspectiveBuilder);
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

		fearGl.glViewport(0, 0, w, h);
		perspectiveBuilder = new PerspectiveBuilder(45.0f, ((float) w / (float) h), 0.1f, 100.0f);

		fearGl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		fearGl.glClearDepth(1.0f);
		fearGl.glEnable(Capability.GL_DEPTH_TEST);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
	}

	public static void main(String[] args) {
		new Main();
	}
}