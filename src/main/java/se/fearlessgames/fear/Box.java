package se.fearlessgames.fear;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Box {

	private boolean useShader = true;

	private int shader = 0;
	private int vertShader = 0;
	private int fragShader = 0;
	private double angle;

	public Box() {

		shader = GL20.glCreateProgram();
		if (shader != 0) {
			vertShader = createVertShader("src/main/resources/shaders/screen.vert");
			fragShader = createFragShader("src/main/resources/shaders/screen.frag");
		} else useShader = false;


		if (vertShader != 0 && fragShader != 0) {
			GL20.glAttachShader(shader, vertShader);
			GL20.glAttachShader(shader, fragShader);
			GL20.glLinkProgram(shader);
			GL20.glValidateProgram(shader);
			useShader = printLogInfo(shader);
		} else useShader = false;
	}

	public void draw() {
		if (useShader) {
			GL20.glUseProgram(shader);
		}
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, 0.0f, -10.0f);
		GL11.glRotated(angle, 0.3, 1, 1);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);//white
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
		GL11.glVertex3f(1.0f, 1.0f, 0.0f);
		GL11.glVertex3f(1.0f, -1.0f, 0.0f);
		GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
		GL11.glEnd();

		//release the shader
		GL20.glUseProgram(0);
	}

	public void update() {
		angle += 0.1;
	}


	private int createVertShader(String filename) {
		//vertShader will be non zero if succefully created

		vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

		//if created, convert the vertex shader code to a String
		if (vertShader == 0) {
			return 0;
		}
		String vertexCode = "";
		String line;
		try {
			File foo = new File("foo");
			System.out.println(foo.getAbsolutePath());
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				vertexCode += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Fail reading vertex shading code");
			return 0;
		}
		/*
				  * associate the vertex code String with the created vertex shader
				  * and compile
				  */
		GL20.glShaderSource(vertShader, vertexCode);
		GL20.glCompileShader(vertShader);

		//if there was a problem compiling, reset vertShader to zero
		if (!printLogInfo(vertShader)) {
			vertShader = 0;
		}
		//if zero we won't be using the shader
		return vertShader;
	}

	private int createFragShader(String filename) {

		fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		if (fragShader == 0) {
			return 0;
		}
		String fragCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				fragCode += line + "\n";
			}
		} catch (Exception e) {
			System.out.println("Fail reading fragment shading code");
			return 0;
		}
		GL20.glShaderSource(fragShader, fragCode);
		GL20.glCompileShader(fragShader);

		if (!printLogInfo(fragShader)) {
			fragShader = 0;
		}

		return fragShader;
	}

	private static boolean printLogInfo(int obj) {
		String out = GL20.glGetShaderInfoLog(obj, 40);
		System.out.println("Info log:\n" + out);
		return true;
	}

}
