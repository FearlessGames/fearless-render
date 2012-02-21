package se.fearlessgames.fear.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.EnumSet;

public interface FearGl {
	void glUseProgram(int programId);

	int glGetUniformLocation(int programId, String uniformName);

	void glUniformMatrix4(int uniformLocation, boolean transpose, FloatBuffer matrix);

	void glUniformMatrix3(int uniformLocation, boolean transpose, FloatBuffer matrix);

	void glUniform3f(int pointer, float v0, float v1, float v2);

	void glUniform1f(int pointer, float value);

	void glUniform1i(int pointer, int value);

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

	void glDisable(Capability capability);

	void glDepthFunc(DepthFunction function);

	int glCreateShader(ShaderType type);

	void glShaderSource(int shader, String code);

	void glCompileShader(int shader);

	void glAttachShader(int shaderProgram, int shader);

	void glLinkProgram(int shaderProgram);

	void glValidateProgram(int shaderProgram);

	String glGetShaderInfoLog(int shader, int maxLength);

	int glCreateProgram();

	void glBindTexture(TextureType type, int textureId);

	int glGetAttribLocation(int shaderProgram, String name);

	void glVertexAttribPointer(int index, int size, DataType type, boolean normalized, int stride, int buffer_buffer_offset);

	void glEnableVertexAttribArray(int index);

	void glBindFragDataLocation(int shaderProgram, int colorNumber, String name);

	void glBlendFunc(BlendFunction sFactor, BlendFunction dFactor);
}
