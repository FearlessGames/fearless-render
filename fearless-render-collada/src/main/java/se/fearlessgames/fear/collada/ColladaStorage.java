package se.fearlessgames.fear.collada;

import se.fearlessgames.fear.collada.data.AssetData;

import java.nio.FloatBuffer;

public class ColladaStorage {
	private FloatBuffer vertices = FloatBuffer.allocate(0);
	private FloatBuffer normals = FloatBuffer.allocate(0);
	private FloatBuffer colors = FloatBuffer.allocate(0);
	private FloatBuffer textureCoords = FloatBuffer.allocate(0);
	private AssetData assetdata;

	public void setAssetData(AssetData assetData) {
		this.assetdata = assetData;
	}

	public AssetData getAssetdata() {
		return assetdata;
	}
}
