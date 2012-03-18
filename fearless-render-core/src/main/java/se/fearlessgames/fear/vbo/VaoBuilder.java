package se.fearlessgames.fear.vbo;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.IntBuffer;

public class VaoBuilder {
	private final FearGl fearGl;
	private final ShaderProgram shaderProgram;
	private InterleavedBuffer interleavedBuffer;
	private IntBuffer indices;
	private VertexIndexMode vertexIndexMode = VertexIndexMode.TRIANGLES;


	private VaoBuilder(FearGl fearGl, ShaderProgram shaderProgram) {
		this.fearGl = fearGl;
		this.shaderProgram = shaderProgram;
	}

	public static VaoBuilder fromMeshData(FearGl fearGl, ShaderProgram shaderProgram, MeshData meshData) {
		VaoBuilder vaoBuilder = new VaoBuilder(fearGl, shaderProgram);
		vaoBuilder.interleavedBuffer(meshData.createInterleavedBuffer());
		vaoBuilder.indices(meshData.getIndices());
		vaoBuilder.vertexIndexMode(meshData.getVertexIndexMode());
		return vaoBuilder;
	}

	private VaoBuilder vertexIndexMode(VertexIndexMode vertexIndexMode) {
		this.vertexIndexMode = vertexIndexMode;
		return this;
	}

	private VaoBuilder indices(IntBuffer indices) {
		this.indices = BufferUtils.duplicate(indices);
		return this;
	}

	private VaoBuilder interleavedBuffer(InterleavedBuffer interleavedBuffer) {
		this.interleavedBuffer = interleavedBuffer;
		return this;
	}

	public VertexArrayObject build() {
		return new VertexArrayObject(fearGl, interleavedBuffer, indices, vertexIndexMode, shaderProgram);
	}
}
