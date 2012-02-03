package se.fearlessgames.fear;

import java.util.List;

public class FearScene {
	private final FearNode root;

	public FearScene(FearNode root) {
		this.root = root;
	}

	public FearNode getRoot() {
		return root;
	}

	public void render(Renderer renderer) {
		renderSkybox(renderer);
		renderOpaqueObjects(renderer);
		renderTransparentObjects(renderer);
	}

	private void renderTransparentObjects(Renderer renderer) {
	}

	private void renderOpaqueObjects(Renderer renderer) {
		Transformation rootTransformation = new Transformation(root.getPosition(), root.getRotation(), root.getScale());
		render(renderer, root, rootTransformation);
	}

	private void render(Renderer renderer, FearNode node, Transformation transformation) {
		if (!node.isVisible()) {
			return;
		}

		List<FearMesh> meshes = node.getMeshes();
		for (FearMesh mesh : meshes) {
			renderMesh(mesh, renderer, transformation);
		}

		for (FearNode child : node.getChildNodes()) {
			Transformation childTransformation = transformation.transformTo(child.getPosition(), child.getRotation(), child.getScale());
			render(renderer, child, childTransformation);
		}
	}

	private void renderMesh(FearMesh mesh, Renderer renderer, Transformation nodeTransformation) {
		Transformation meshTransformation = nodeTransformation.transformTo(mesh.getPosition(), mesh.getRotation(), mesh.getScale());
		renderer.render(mesh, meshTransformation);
	}

	private void renderSkybox(Renderer output) {
	}

}
