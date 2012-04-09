package se.fearlessgames.fear.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
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
	public void glUniformMatrix3(int uniformLocation, boolean transpose, FloatBuffer matrix) {
		GL20.glUniformMatrix3(uniformLocation, transpose, matrix);
	}

	@Override
	public void glUniform3f(int pointer, float v0, float v1, float v2) {
		GL20.glUniform3f(pointer, v0, v1, v2);
	}

	@Override
	public void glUniform4f(int pointer, float v0, float v1, float v2, float v3) {
		GL20.glUniform4f(pointer, v0, v1, v2, v3);
	}

	@Override
	public void glUniform1f(int pointer, float value) {
		GL20.glUniform1f(pointer, value);
	}

	@Override
	public void glUniform1i(int pointer, int value) {
		GL20.glUniform1i(pointer, value);
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
	public void glClearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}

	@Override
	public void glClearDepth(float depth) {
		GL11.glClearDepth(depth);
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
	public void glDrawElements(VertexIndexMode indexMode, int count, IndexDataType type, int offset) {
		GL11.glDrawElements(indexMode.getGlMode(), count, type.getGlType(), offset);
	}

	@Override
	public void glViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
	}

	@Override
	public void glEnable(Capability capability) {
		GL11.glEnable(capability.getGlCap());
	}

	@Override
	public void glDisable(Capability capability) {
		GL11.glDisable(capability.getGlCap());
	}

	@Override
	public void glDepthFunc(DepthFunction function) {
		GL11.glDepthFunc(function.getGlFunction());
	}

	@Override
	public void glDepthMask(boolean on) {
		GL11.glDepthMask(on);
	}

	@Override
	public int glCreateShader(ShaderType type) {
		return GL20.glCreateShader(type.getType());
	}

	@Override
	public void glShaderSource(int shader, String code) {
		GL20.glShaderSource(shader, code);
	}

	@Override
	public void glCompileShader(int shader) {
		GL20.glCompileShader(shader);
	}

	@Override
	public void glAttachShader(int shaderProgram, int shader) {
		GL20.glAttachShader(shaderProgram, shader);
	}

	@Override
	public void glLinkProgram(int shaderProgram) {
		GL20.glLinkProgram(shaderProgram);
	}

	@Override
	public void glValidateProgram(int shaderProgram) {
		GL20.glValidateProgram(shaderProgram);
	}

	@Override
	public String glGetShaderInfoLog(int shader, int maxLength) {
		return GL20.glGetShaderInfoLog(shader, maxLength);
	}

	@Override
	public int glCreateProgram() {
		return GL20.glCreateProgram();
	}

	@Override
	public void glBindTexture(TextureType type, int textureId) {
		GL11.glBindTexture(type.getGlType(), textureId);
	}

	@Override
	public int glGetAttribLocation(int shaderProgram, String name) {
		return GL20.glGetAttribLocation(shaderProgram, name);
	}

	@Override
	public void glVertexAttribPointer(int index, int size, DataType type, boolean normalized, int stride, int buffer_buffer_offset) {
		GL20.glVertexAttribPointer(index, size, type.getGlType(), normalized, stride, buffer_buffer_offset);
	}

	@Override
	public void glEnableVertexAttribArray(int index) {
		GL20.glEnableVertexAttribArray(index);
	}

	@Override
	public void glBindFragDataLocation(int shaderProgram, int colorNumber, String name) {
		GL30.glBindFragDataLocation(shaderProgram, colorNumber, name);
	}

	@Override
	public void glBlendFunc(BlendFunction sFactor, BlendFunction dFactor) {
		GL11.glBlendFunc(sFactor.getGlValue(), dFactor.getGlValue());
	}

	@Override
	public void glCullFace(Culling culling) {
		GL11.glCullFace(culling.getGlValue());
	}

	@Override
	public int glGenTextures() {
		return GL11.glGenTextures();
	}

	@Override
	public int glGenVertexArrays() {
		return GL30.glGenVertexArrays();
	}

	@Override
	public void glBindVertexArray(int vaoId) {
		GL30.glBindVertexArray(vaoId);
	}

	@Override
	public void glTexParameteri(TextureType textureType, TextureMagnificationFilter textureMagnificationFilter) {
		GL11.glTexParameteri(textureType.getGlType(), GL11.GL_TEXTURE_MAG_FILTER, textureMagnificationFilter.getGlType());
	}

	@Override
	public void glTexParameteri(TextureType textureType, TextureMinificationFilter textureMinificationFilter) {
		GL11.glTexParameteri(textureType.getGlType(), GL11.GL_TEXTURE_MIN_FILTER, textureMinificationFilter.getGlType());
	}

	@Override
	public void glTexImage2D(TextureType textureType, int level, TexturePixelFormat internalFormat, int width, int height, int border, TexturePixelFormat format, DataType dataType, ByteBuffer buffer) {
		GL11.glTexImage2D(textureType.getGlType(), level, internalFormat.getGlType(), width, height, border, format.getGlType(), dataType.getGlType(), buffer);
	}

	@Override
	public int glGetError() {
		return GL11.glGetError();
	}

	@Override
	public String glGetString(StringName name) {
		return GL11.glGetString(name.getName());
	}
}

