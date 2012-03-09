package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.math.Vector4;

public class Skybox {
	private final Node root;

	public Skybox() {
		root = new Node("Skybox");
	}

	public void moveToCamera(Transformation camera) {
		// TODO: this is utterly broken - find correct way to extract position from transformation
		Vector4 v = camera.asMatrix().applyPost(new Vector4(0, 0, 0, 1));
		root.setPosition(new Vector3(-v.getX(), -v.getY(),  -v.getZ()));
	}

	public Node getRoot() {
		return root;
	}
}
