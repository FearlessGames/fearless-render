package se.fearlessgames.fear;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Box {


    private int shaderProgram = 0;
    private double angle;

    public Box() {
        Shaders shaders = new Shaders();

        shaderProgram = shaders.createProgram();
        int vertexShader = shaders.loadAndCompileVertexShader("src/main/resources/shaders/screen.vert");
        int fragmentShader = shaders.loadAndCompileFragmentShader("src/main/resources/shaders/screen.frag");
        shaders.attachToProgram(shaderProgram, vertexShader, fragmentShader);

        if (vertexShader != 0 && fragmentShader != 0) {

        } else {
            throw new RuntimeException("Failed to compile shaders");
        }
    }

    public void draw() {

        GL20.glUseProgram(shaderProgram);

        int pos = GL20.glGetUniformLocation(shaderProgram, "pos");
        if (pos != -1) {
            GL20.glUniform3f(pos, 0f, 0f, -10f);
        } else {
            throw new RuntimeException("Failed to get pos location");
        }

        int rot = GL20.glGetUniformLocation(shaderProgram, "rot");
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

        //release the shaderProgram
        GL20.glUseProgram(0);
    }

    public void update() {
        angle += 0.001;
    }


}
