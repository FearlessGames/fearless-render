package se.fearlessgames.fear.collada;

import se.fearlessgames.fear.collada.data.AssetData;
import se.fearlessgames.fear.collada.data.Node;

public class ColladaStorage {
	private AssetData assetdata;
	private Node scene;

	public void setAssetData(AssetData assetData) {
		this.assetdata = assetData;
	}

	public AssetData getAssetdata() {
		return assetdata;
	}

	public void setScene(Node scene) {
		this.scene = scene;
	}

	public Node getScene() {
		return scene;
	}
}
