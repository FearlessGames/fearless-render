package se.fearlessgames.fear.example;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;

public class VBOTest {
	public static void main(String[] args) {
		//init display
		Display.setTitle("vbo test");
		try {
			DisplayMode mode = new DisplayMode(800, 600);
			Display.setDisplayMode(mode);
			Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		//init OpenGL
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);//_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-1, 1, -1, 1, 1, 1000);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(0, 10, 0, 0, 0, 0, 0, 0, 1);

		if (!GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
			throw new RuntimeException("VBOs unsupported");

		//Create two VBO ids, one for vertice data and one for index data
		int dataVboId = ARBVertexBufferObject.glGenBuffersARB();
		int indexVboId = ARBVertexBufferObject.glGenBuffersARB();

		//The vertex buffer will contain interleaved data :
		//	-3d vertex (in doubles)
		//	-3d normal (in doubles)
		//	-RGBA color (in floats)
		//Get the size of primitive types in bytes
		int intSize = Integer.SIZE / 8;
		int floatSize = Float.SIZE / 8;
		int doubleSize = Double.SIZE / 8;
		//Get the size of a 3d vector (in doubles) and RGBA color (in floats)
		int verticeSize = 3 * doubleSize;
		int normalSize = 3 * doubleSize;
		int colorSize = 4 * floatSize;

		//Get the size of an element
		int elementSize = verticeSize + normalSize + colorSize;

		//Allocate the vertex buffer for three elements (to test displaying a triangle)
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dataVboId);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,
				elementSize * 3, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

		//Map the vertex buffer to a ByteBuffer
		ByteBuffer dataBuffer = ARBVertexBufferObject.glMapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,
				ARBVertexBufferObject.GL_WRITE_ONLY_ARB, elementSize * 3, null);

		//write vertex {5,0,5}, normal {0,1,0}, color {0,0,1,1}
		dataBuffer.putDouble(5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(1);
		dataBuffer.putDouble(0);
		dataBuffer.putFloat(0).putFloat(0).putFloat(1).putFloat(1);

		//write vertex {-5,0,5}, normal {0,1,0}, color {0,0,1,1}
		dataBuffer.putDouble(-5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(1);
		dataBuffer.putDouble(0);
		dataBuffer.putFloat(0).putFloat(0).putFloat(1).putFloat(1);

		//write vertex {-5,0,-5}, normal {0,1,0}, color {0,0,1,1}
		dataBuffer.putDouble(-5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(-5);
		dataBuffer.putDouble(0);
		dataBuffer.putDouble(1);
		dataBuffer.putDouble(0);
		dataBuffer.putFloat(0).putFloat(0).putFloat(1).putFloat(1);

		//flip buffer for reading
		dataBuffer.flip();

		//unmap and unbind
		ARBVertexBufferObject.glUnmapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);

		//Allocate the index buffer for three elements (they will point to the three elements above)
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, indexVboId);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
				intSize * 3, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

		//Map the index buffer to a ByteBuffer
		ByteBuffer indexBuffer = ARBVertexBufferObject.glMapBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
				ARBVertexBufferObject.GL_WRITE_ONLY_ARB, intSize * 3, null);

		//Write the indexes and flip
		indexBuffer.putInt(0).putInt(1).putInt(2);
		indexBuffer.flip();

		//unmap and unbind
		ARBVertexBufferObject.glUnmapBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);


		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			//enable the arrays
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

			//bind the vertex buffer and the index buffer
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dataVboId);
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, indexVboId);

			//the vertex pointer has an offset of 0 and a stride of elementSize
			GL11.glVertexPointer(3, GL11.GL_DOUBLE, elementSize, 0);
			//the normal pointer has an offset of verticeSize and a stride of elementSize
			GL11.glNormalPointer(GL11.GL_DOUBLE, elementSize, verticeSize);
			//the color pointer has an offset of (verticeSize+normalSize) and a stride of elementSize
			GL11.glColorPointer(4, GL11.GL_FLOAT, elementSize, verticeSize + normalSize);

			//Draw the triangle with three indexes (with 0 offset in the index buffer)
			GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_INT, 0);

			//unbind the buffers
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

			//disable the arrays
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

			Display.update();
		}
	}
}