package se.fearlessgames.fear.collada;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector4;
import se.fearlessgames.fear.mesh.IndexMode;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Collada2Vbo {
	private final FearGl fearGl;

	public Collada2Vbo(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public VertexBufferObject create(MeshData meshData) {
		rewindMesh(meshData);
		VboBuilder vboBuilder = VboBuilder.fromBuffer(fearGl, meshData.getVertexBuffer());

		vboBuilder.indices(meshData.getIndices());
		vboBuilder.normals(meshData.getNormalBuffer());
		FloatBuffer texCoords = meshData.getTextureCoordsMap().get(0);

		if (texCoords != null) {
			vboBuilder.textureCoords(texCoords);
		}

		if (meshData.getIndexMode() == IndexMode.Triangles) {
			vboBuilder.triangles();
		} else if (meshData.getIndexMode() == IndexMode.Quads) {
			vboBuilder.quads();
		}

		return vboBuilder.build();
	}

	public VertexBufferObject create(Node node) {
		MeshData meshData = createCombinedMesh(node);
		rewindMesh(meshData);
		return create(meshData);
	}

	protected MeshData createCombinedMesh(Node node) {
		CombinedMesh combinedMesh = new CombinedMesh();

		combinedMeshes(combinedMesh, new Matrix4(), node, new AtomicInteger());

		MeshData meshData = new MeshData("CombinedMesh-" + node.getName());
		meshData.setVertexBuffer(toFloatBuffer(combinedMesh.vertexBuffer));
		meshData.setNormalBuffer(toFloatBuffer(combinedMesh.normalBuffer));
		for (Integer texCoordsIndex : combinedMesh.textureCoordsMap.keySet()) {
			meshData.setTextureCoords(texCoordsIndex, toFloatBuffer(combinedMesh.textureCoordsMap.get(texCoordsIndex)));
		}
		meshData.setIndices(toIntBuffer(combinedMesh.indices));
		meshData.setIndexMode(combinedMesh.indexMode);
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

	private void combinedMeshes(CombinedMesh combinedMesh, Matrix4 matrix4, Node rootNode, AtomicInteger indicesOffset) {
		for (MeshData meshData : rootNode.getMeshes()) {
			if (combinedMesh.indexMode == null) {
				combinedMesh.indexMode = meshData.getIndexMode();
			}

			if (combinedMesh.indexMode != meshData.getIndexMode()) {
				throw new ColladaException("Index mode have to be the same on all submeshes for combining them, offending mesh:" + meshData.getName(), meshData);
			}

			rewindMesh(meshData);

			for (int i = 0; i < meshData.getVertexBuffer().limit() / 3; i++) {
				Vector4 vertex = new Vector4(meshData.getVertexBuffer().get(), meshData.getVertexBuffer().get(), meshData.getVertexBuffer().get(), 1);
				vertex = matrix4.applyPost(vertex);
				combinedMesh.vertexBuffer.add((float) vertex.getX());
				combinedMesh.vertexBuffer.add((float) vertex.getY());
				combinedMesh.vertexBuffer.add((float) vertex.getZ());

				combinedMesh.normalBuffer.add(meshData.getNormalBuffer().get());
				combinedMesh.normalBuffer.add(meshData.getNormalBuffer().get());
				combinedMesh.normalBuffer.add(meshData.getNormalBuffer().get());

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

		}

		for (Node node : rootNode.getChildren()) {
			Matrix4 transform = matrix4;
			if (node.getTransform() != null) {
				transform = matrix4.multiply(node.getTransform());
			}
			combinedMeshes(combinedMesh, transform, node, indicesOffset);
		}
	}

	private void rewindMesh(MeshData meshData) {
		meshData.getVertexBuffer().rewind();
		meshData.getNormalBuffer().rewind();
		meshData.getIndices().rewind();

		for (Integer integer : meshData.getTextureCoordsMap().keySet()) {
			meshData.getTextureCoordsMap().get(integer).rewind();
		}
	}


	private static class CombinedMesh {
		private IndexMode indexMode;
		private List<Float> vertexBuffer = new ArrayList<Float>();
		private List<Float> normalBuffer = new ArrayList<Float>();
		private Multimap<Integer, Float> textureCoordsMap = ArrayListMultimap.create();

		private List<Integer> indices = new ArrayList<Integer>();
	}
}
