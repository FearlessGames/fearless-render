package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.renderbucket.RenderBucket;

public class Scene {

	private final Node root;

	public Scene(Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}

	public void render(Renderer renderer, Transformation camera) {
		addNode(renderer, root, camera.asMatrix());
		renderer.render();
	}

	private void renderObjects(Renderer renderer, Transformation camera) {
		addNode(renderer, root, camera.asMatrix());

		renderer.render();
	}

	private void addNode(Renderer renderer, Node node, Matrix4 parentTransform) {
		if (!node.isVisible()) {
			return;
		}

		Transformation childTransformation = new Transformation(node.getPosition(), node.getRotation(), node.getScale());
		Matrix4 multiply = parentTransform.multiply(childTransformation.asMatrix());

		addMesh(node.getMesh(), renderer, multiply);


		for (Node child : node.getChildNodes()) {
			addNode(renderer, child, multiply);
		}
	}

	private void addMesh(Mesh mesh, Renderer renderer, Matrix4 parentTransform) {
		if (mesh == null) {
			return;
		}
		RenderBucket bucket = mesh.getBucket();
		validateBucket(bucket, renderer);
		bucket.add(mesh, parentTransform);
	}

	private void validateBucket(RenderBucket bucket, Renderer renderer) {
		if (!renderer.containsBucket(bucket)) {
			throw new FearError("Object in scene has a render bucket that's not in the renderer");
		}
	}
}
