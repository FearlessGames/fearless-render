package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;

public class Scene {
	private final Node root;

	public Scene(Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}

	public void render(Renderer renderer) {
		renderSkybox(renderer);
		renderObjects(renderer);
	}

	private void renderObjects(Renderer renderer) {
		render(renderer, root, Matrix4.IDENTITY);

		renderer.render();
	}

	private void render(Renderer renderer, Node node, Matrix4 parentTransform) {
		if (!node.isVisible()) {
			return;
		}

		Transformation childTransformation = new Transformation(node.getPosition(), node.getRotation(), node.getScale());
		Matrix4 multiply = parentTransform.multiply(childTransformation.asMatrix());

		renderMesh(node.getMesh(), renderer, multiply);


		for (Node child : node.getChildNodes()) {
			render(renderer, child, multiply);
		}
	}

	private void renderMesh(Mesh mesh, Renderer renderer, Matrix4 parentTransform) {
		if (mesh == null) {
			return;
		}
		renderer.addMeshToRender(mesh, parentTransform);
	}

	private void renderSkybox(Renderer output) {
	}

}
