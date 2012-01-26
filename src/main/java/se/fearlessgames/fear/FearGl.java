package se.fearlessgames.fear;

import java.nio.FloatBuffer;
import java.util.EnumSet;

public interface FearGl {
	void glUseProgram(int programId);
	int glGetUniformLocation(int programId, String uniformName);

	void glUniformMatrix4(int uniformLocation, boolean transpose, FloatBuffer matrix);


	void glClear(EnumSet<ClearBit> clearBits);
	void glLoadIdentity();
	void glClearColor(float r, float g, float b, float a);

	int glGenBuffers();
	void glBindBuffer(BufferTarget target, int bufferId);
	void glBufferData(BufferTarget target, FloatBuffer data, BufferUsage usage);
}
