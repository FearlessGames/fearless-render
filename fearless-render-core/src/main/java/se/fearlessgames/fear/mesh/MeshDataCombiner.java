package se.fearlessgames.fear.mesh;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MeshDataCombiner {
	private final String name;
	private VertexIndexMode currentIndexIndexMode;
	private final CombinedMesh combinedMesh = new CombinedMesh();
	private final AtomicInteger indicesOffset = new AtomicInteger(0);

	public MeshDataCombiner(String name) {
		this.name = name;
	}


	public MeshDataCombiner addMeshData(MeshData meshData, Matrix4 transform) {
		if (currentIndexIndexMode == null) {
			currentIndexIndexMode = meshData.getVertexIndexMode();
		}

		if (currentIndexIndexMode != meshData.getVertexIndexMode()) {
			throw new RuntimeException("Index mode have to be the same on all meshes for combining them, offending mesh:" + meshData.getName());
		}

		meshData = meshData.duplicate();

		for (int i = 0; i < meshData.getVertexBuffer().limit() / 3; i++) {
			Vector3 vertex = transform.multiply(new Vector3(meshData.getVertexBuffer().get(), meshData.getVertexBuffer().get(), meshData.getVertexBuffer().get()));
			combinedMesh.vertexBuffer.add((float) vertex.getX());
			combinedMesh.vertexBuffer.add((float) vertex.getY());
			combinedMesh.vertexBuffer.add((float) vertex.getZ());

			Vector3 normal = transform.multiplyNormal(new Vector3(meshData.getNormalBuffer().get(), meshData.getNormalBuffer().get(), meshData.getNormalBuffer().get()));
			combinedMesh.normalBuffer.add((float) normal.getX());
			combinedMesh.normalBuffer.add((float) normal.getY());
			combinedMesh.normalBuffer.add((float) normal.getZ());

			for (Integer texCoordsIndex : meshData.getTextureCoordsMap().keySet()) {
				FloatBuffer textureBuffer = meshData.getTextureCoordsMap().get(texCoordsIndex);
				Collection<Float> floats = combinedMesh.textureCoordsMap.get(texCoordsIndex);
				floats.add(textureBuffer.get());
				floats.add(textureBuffer.get());
			}
		}

		for (int i = 0; i < meshData.getIndices().limit(); i++) {
			combinedMesh.indices.add(meshData.getIndices().get() + indicesOffset.get());
		}

		indicesOffset.addAndGet(meshData.getIndices().limit());

		return this;

	}

	public MeshData build() {
		MeshData meshData = new MeshData(name);
		meshData.setVertexBuffer(toFloatBuffer(combinedMesh.vertexBuffer));
		meshData.setNormalBuffer(toFloatBuffer(combinedMesh.normalBuffer));
		meshData.setIndices(toIntBuffer(combinedMesh.indices));

		for (Integer texCoordsIndex : combinedMesh.textureCoordsMap.keySet()) {
			meshData.setTextureCoords(texCoordsIndex, toFloatBuffer(combinedMesh.textureCoordsMap.get(texCoordsIndex)));
		}

		meshData.setVertexIndexMode(currentIndexIndexMode);

		return meshData;
	}


	private IntBuffer toIntBuffer(List<Integer> indices) {
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());
		for (Integer indice : indices) {
			buffer.put(indice);
		}
		return buffer;
	}


	private FloatBuffer toFloatBuffer(Collection<Float> vertexBuffer) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertexBuffer.size());
		for (Float aFloat : vertexBuffer) {
			buffer.put(aFloat);
		}
		return buffer;
	}

	private static class CombinedMesh {
		private List<Float> vertexBuffer = new ArrayList<Float>();
		private List<Float> normalBuffer = new ArrayList<Float>();
		private Multimap<Integer, Float> textureCoordsMap = ArrayListMultimap.create();

		private List<Integer> indices = new ArrayList<Integer>();
	}
}
