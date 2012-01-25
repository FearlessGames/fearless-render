package se.fearlessgames.fear;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * Source: http://en.wikipedia.org/wiki/Vertex_Buffer_Object
 */

public class VboBox {


    private int vertexBufferId;
    private int indexBufferId;

    private int shaderProgram;
    private double angle;

    public VboBox() {
        createShaders();
        createVbo();
    }

    private void createShaders() {
        Shaders shaders = new Shaders();

        shaderProgram = shaders.createProgram();
        int vertexShader = shaders.loadAndCompileVertexShader("src/main/resources/shaders/screen.vert");
        int fragmentShader = shaders.loadAndCompileFragmentShader("src/main/resources/shaders/screen.frag");
        shaders.attachToProgram(shaderProgram, vertexShader, fragmentShader);

    }

    private void createVbo() {

        vertexBufferId = GL15.glGenBuffers();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(4 * 3);
        vertexBuffer.put(new float[]{
                // Front face (facing viewer), correct winding order.
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        });
        vertexBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);


        indexBufferId = GL15.glGenBuffers();
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(4);
        indexBuffer.put(new int[]{
                0, 1, 2, 3
        });
        indexBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GL11.GL_NONE);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL11.GL_NONE);


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


        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);

        GL11.glDrawElements(GL11.GL_QUADS, 12, GL11.GL_UNSIGNED_INT, 0);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        GL20.glUseProgram(0);
    }

    public void update() {
        angle += 0.001;
    }


}
