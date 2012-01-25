package se.fearlessgames.fear.vbo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VertexBufferObject {

    private final int vertexBufferId;
    private final int indexBufferId;
    private final InterleavedBuffer interleavedBuffer;

    private final IntBuffer indices;
    private final VertexDrawMode drawMode;

    public VertexBufferObject(InterleavedBuffer interleavedBuffer, IntBuffer indices, VertexDrawMode drawMode) {
        this.interleavedBuffer = interleavedBuffer;
        this.indices = indices;
        this.drawMode = drawMode;

        vertexBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, interleavedBuffer.getBuffer(), GL15.GL_STATIC_DRAW);

        indexBufferId = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GL11.GL_NONE);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL11.GL_NONE);
    }


    public void draw() {
        enableStates();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);

        int stride = interleavedBuffer.getStride();
        int offset = 0;

        GL11.glVertexPointer(3, GL11.GL_FLOAT, stride, offset);
        offset = 3 * 4;

        if (interleavedBuffer.isNormals()) {
            GL11.glNormalPointer(GL11.GL_FLOAT, stride, offset);
            offset += (3 * 4);
        }

        if (interleavedBuffer.isColors()) {
            GL11.glColorPointer(4, GL11.GL_FLOAT, stride, offset);
            offset += (4 * 4);
        }

        if (interleavedBuffer.isTextureCords()) {
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, offset);
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);

        GL11.glDrawElements(drawMode.getGlMode(), indices.limit(), GL11.GL_UNSIGNED_INT, 0);


        disableStates();
    }


    private void enableStates() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        if (interleavedBuffer.isNormals()) {
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        }

        if (interleavedBuffer.isColors()) {
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        }

        if (interleavedBuffer.isTextureCords()) {
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }
    }

    private void disableStates() {
        if (interleavedBuffer.isNormals()) {
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        }

        if (interleavedBuffer.isColors()) {
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        }

        if (interleavedBuffer.isTextureCords()) {
            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    protected static class InterleavedBuffer {
        private final FloatBuffer buffer; //V[xyz]N[xyz]C[rgba]T1[st]
        private final boolean normals;
        private final boolean colors;
        private final boolean textureCords;

        private final int stride;

        public InterleavedBuffer(FloatBuffer buffer, boolean normals, boolean colors, boolean textureCords) {
            this.buffer = buffer;
            this.normals = normals;
            this.colors = colors;
            this.textureCords = textureCords;

            int stride = 3; //xyz

            if (normals) {
                stride += 3; //xyz
            }

            if (colors) {
                stride += 4; //rgba
            }

            if (textureCords) {
                stride += 2; //st
            }

            stride *= 4; //4bytes for a float

            this.stride = stride;

        }

        public FloatBuffer getBuffer() {
            return buffer;
        }

        public boolean isNormals() {
            return normals;
        }

        public boolean isTextureCords() {
            return textureCords;
        }

        public boolean isColors() {
            return colors;
        }

        public int getStride() {
            return stride;
        }
    }
}
