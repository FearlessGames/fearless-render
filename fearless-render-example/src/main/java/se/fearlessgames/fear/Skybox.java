package se.fearlessgames.fear;

public class Skybox {
	private final Node root;

	public Skybox() {
		root = new Node("Skybox");
	}

	public void moveToCamera(Camera camera) {
		root.setPosition(camera.getPosition());
		System.out.println("root position: " + root.getPosition() + " , camera position: " + camera.getPosition());
	}

	public Node getRoot() {
		return root;
	}
}
