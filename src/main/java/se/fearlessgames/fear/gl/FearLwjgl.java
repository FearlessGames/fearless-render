package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.EnumSet;

public class FearLwjgl implements FearGl {
	@Override
	public void glUseProgram(int programId) {
		GL20.glUseProgram(programId);
	}

	@Override
	public int glGetUniformLocation(int programId, String uniformName) {
		return GL20.glGetUniformLocation(programId, uniformName);
	}

	@Override
	public void glUniformMatrix4(int uniformLocation, boolean transpose, FloatBuffer matrix) {
		GL20.glUniformMatrix4(uniformLocation, transpose, matrix);
	}

	@Override
	public void glClear(EnumSet<ClearBit> clearBits) {
		int mask = 0;
		for (ClearBit bit : clearBits) {
			mask |= bit.getGlBit();
		}
		GL11.glClear(mask);
	}

	@Override
	public void glLoadIdentity() {
		GL11.glLoadIdentity();
	}

	@Override
	public void glClearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	@Override
	public int glGenBuffers() {
		return GL15.glGenBuffers();
	}

	@Override
	public void glBindBuffer(BufferTarget target, int bufferId) {
		GL15.glBindBuffer(target.getGlBufferTarget(), bufferId);
	}

	@Override
	public void glReleaseBuffer(BufferTarget target) {
		GL15.glBindBuffer(target.getGlBufferTarget(), GL11.GL_NONE);
	}

	@Override
	public void glBufferData(BufferTarget target, FloatBuffer data, BufferUsage usage) {
		GL15.glBufferData(target.getGlBufferTarget(), data, usage.getGlUsage());
	}

	@Override
	public void glBufferData(BufferTarget target, IntBuffer data, BufferUsage usage) {
		GL15.glBufferData(target.getGlBufferTarget(), data, usage.getGlUsage());
	}


	@Override
	public void glVertexPointer(int size, DataType type, int stride, int offset) {
		GL11.glVertexPointer(size, type.getGlType(), stride, offset);
	}

	@Override
	public void glNormalPointer(DataType type, int stride, int offset) {
		GL11.glNormalPointer(type.getGlType(), stride, offset);
	}

	@Override
	public void glColorPointer(int size, DataType type, int stride, int offset) {
		GL11.glColorPointer(size, type.getGlType(), stride, offset);
	}

	@Override
	public void glTexCoordPointer(int size, DataType type, int stride, int offset) {
		GL11.glTexCoordPointer(size, type.getGlType(), stride, offset);
	}

	@Override
	public void glDrawElements(VertexDrawMode drawMode, int count, IndexDataType type, int offset) {
		GL11.glDrawElements(drawMode.getGlMode(), count, type.getGlType(), offset);
	}

	@Override
	public void glEnableClientState(ClientState state) {
		GL11.glEnableClientState(state.getGlState());
	}

	@Override
	public void glDisableClientState(ClientState state) {
		GL11.glDisableClientState(state.getGlState());
	}
}