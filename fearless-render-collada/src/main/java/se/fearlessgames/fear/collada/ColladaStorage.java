package se.fearlessgames.fear.collada;

import se.fearlessgames.fear.collada.data.AssetData;
import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshDataCombiner;

public class ColladaStorage {
	private final AssetData assetdata;
	private final Node scene;

	public ColladaStorage(AssetData assetdata, Node scene) {
		this.assetdata = assetdata;
		this.scene = scene;
	}

	public AssetData getAssetdata() {
		return assetdata;
	}

	public Node getScene() {
		return scene;
	}

	public MeshData getFirstMeshData() {
		MeshData container[] = new MeshData[1];
		findMesh(container, scene);
		return container[0];
	}

	public MeshData getCombinedMeshData() {
		MeshDataCombiner meshDataCombiner = new MeshDataCombiner("CombinedMesh-" + scene.getName());
		combinedMeshes(meshDataCombiner, new Matrix4(), scene);
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

	private void findMesh(MeshData[] container, Node node) {
		if (container[0] != null) {
			return;
		}

		if (!node.getMeshes().isEmpty()) {
			container[0] = node.getMeshes().get(0);
		}

		for (Node child : node.getChildren()) {
			findMesh(container, child);
		}


	}


}
