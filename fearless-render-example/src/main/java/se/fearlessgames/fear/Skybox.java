package se.fearlessgames.fear;

import se.fearlessgames.fear.camera.Camera;

public class Skybox {
	private final Node root;

	public Skybox() {
		root = new Node("Skybox");
	}

	public void moveToCamera(Camera camera) {
		root.setPosition(camera.getPosition());
	}

	public Node getRoot() {
		return root;
	}
}
