package se.fearlessgames.fear.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.EnumSet;

public interface FearGl {
	void glUseProgram(int programId);

	int glGetUniformLocation(int programId, String uniformName);

	void glUniformMatrix4(int uniformLocation, boolean transpose, FloatBuffer matrix);

	void glClear(EnumSet<ClearBit> clearBits);

	void glClearColor(float r, float g, float b, float a);

	void glClearDepth(float depth);

	int glGenBuffers();

	void glBindBuffer(BufferTarget target, int bufferId);

	void glReleaseBuffer(BufferTarget target);

	void glBufferData(BufferTarget target, FloatBuffer data, BufferUsage usage);

	void glBufferData(BufferTarget target, IntBuffer data, BufferUsage usage);

	void glVertexPointer(int size, DataType type, int stride, int offset);

	void glNormalPointer(DataType type, int stride, int offset);

	void glColorPointer(int size, DataType type, int stride, int offset);

	void glTexCoordPointer(int size, DataType type, int stride, int offset);

	void glDrawElements(VertexDrawMode drawMode, int count, IndexDataType type, int offset);

	void glEnableClientState(ClientState state);

	void glDisableClientState(ClientState state);


	void glViewport(int x, int y, int width, int height);

	void glEnable(Capability capability);

	void glDepthFunc(DepthFunction function);
}
