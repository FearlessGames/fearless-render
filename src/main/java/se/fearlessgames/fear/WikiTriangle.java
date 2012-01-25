package se.fearlessgames.fear;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * Source: http://en.wikipedia.org/wiki/Vertex_Buffer_Object
 */

public class WikiTriangle {


    private int vertexBufferId;
    private int colorBufferId;
    private int indexBufferId;

    private final int shaderAtribute = 0;
    private int elementSize;
    private int verticeSize;
    private int normalSize;


    public WikiTriangle() {

        int floatSize = Float.SIZE / 8;
        int doubleSize = Double.SIZE / 8;
        //Get the size of a 3d vector (in doubles) and RGBA color (in floats)
        verticeSize = 3 * doubleSize;
        normalSize = 3 * doubleSize;
        int colorSize = 4 * floatSize;

        //Get the size of an element
        elementSize = verticeSize + normalSize + colorSize;

        createVbo();


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


        colorBufferId = GL15.glGenBuffers();
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4 * 4);
        colorBuffer.put(new float[]{
                // Until things are figured out, everything is red.
                1.0f, 0, 0, 1.0f,
                1.0f, 0, 0, 1.0f,
                1.0f, 0, 0, 1.0f,
                1.0f, 0, 0, 1.0f
        });
        colorBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);

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
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferId);
        GL11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);

        GL11.glDrawElements(GL11.GL_QUADS, 12, GL11.GL_UNSIGNED_INT, 0);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

    public void update() {

    }


}
