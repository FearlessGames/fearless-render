package se.fearlessgames.fear.collada;

import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshDataCombiner;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Collada2Vbo {
	private final FearGl fearGl;

	public Collada2Vbo(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public VertexBufferObject create(MeshData meshData) {
		VboBuilder vboBuilder = VboBuilder.fromMeshData(fearGl, meshData);
		return vboBuilder.build();
	}

	public VertexBufferObject create(Node node) {
		MeshData meshData = createCombinedMesh(node);
		return create(meshData);
	}

	protected MeshData createCombinedMesh(Node node) {
		MeshDataCombiner meshDataCombiner = new MeshDataCombiner("CombinedMesh-" + node.getName());
		combinedMeshes(meshDataCombiner, new Matrix4(), node);
		return meshDataCombiner.build();
	}

	private void combinedMeshes(MeshDataCombiner meshDataCombiner, Matrix4 matrix4, Node rootNode) {
		for (MeshData meshData : rootNode.getMeshes()) {
			meshDataCombiner.addMeshData(meshData, matrix4);
		}

		for (Node node : rootNode.getChildren()) {
			Matrix4 transform = matrix4;
			if (node.getTransform() != null) {
				transform = matrix4.multiply(node.getTransform());
			}
			combinedMeshes(meshDataCombiner, transform, node);
		}
	}

}
