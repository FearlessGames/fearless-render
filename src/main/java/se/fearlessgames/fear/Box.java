package se.fearlessgames.fear;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;

public class Box {


    private int shader = 0;
    private int vertShader = 0;
    private int fragShader = 0;
    private double angle;

    public Box() {

        shader = GL20.glCreateProgram();
        if (shader != 0) {
            vertShader = createVertShader("src/main/resources/shaders/screen.vert");
            fragShader = createFragShader("src/main/resources/shaders/screen.frag");
        } else {
            throw new RuntimeException("Failed to load shaders");
        }


        if (vertShader != 0 && fragShader != 0) {
            GL20.glAttachShader(shader, vertShader);
            GL20.glAttachShader(shader, fragShader);
            GL20.glLinkProgram(shader);
            GL20.glValidateProgram(shader);
            printLogInfo(shader);
        } else {
            throw new RuntimeException("Failed to compile shaders");
        }
    }

    public void draw() {

        GL20.glUseProgram(shader);

        int pos = GL20.glGetUniformLocation(shader, "pos");
        if (pos != -1) {
            GL20.glUniform3f(pos, 0f, 0f, -10f);
        } else {
            throw new RuntimeException("Failed to get pos location");
        }

        int rot = GL20.glGetUniformLocation(shader, "rot");
        if (rot != -1) {
            GL20.glUniform3f(rot, (float) angle, (float) angle * 0.2f, (float) angle * 0.5f);
        } else {
            throw new RuntimeException("Failed to get rot location");
        }


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
        angle += 0.001;
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
        printLogInfo(vertShader);

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

        printLogInfo(fragShader);

        return fragShader;
    }

    private static void printLogInfo(int obj) {
        String out = GL20.glGetShaderInfoLog(obj, 40);
        System.out.println("Info log:\n" + out);
    }

}
