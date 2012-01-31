package se.fearlessgames.fear;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import se.fearlessgames.fear.math.TransformBuilder;

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
		render(renderer, root);
	}

	private void render(Renderer renderer, FearNode node) {
		if (!node.isVisible()) {
			return;
		}
		Vector3D position = node.getPosition();
		Rotation rotation = node.getRotation();

		TransformBuilder transformBuilder = new TransformBuilder();
		transformBuilder.translate(position);
		transformBuilder.rotate(rotation);
		List<FearMesh> meshes = node.getMeshes();
		for (FearMesh mesh : meshes) {
			renderMesh(mesh, renderer, transformBuilder);
		}
		for (FearNode child : node.getChildNodes()) {
			render(renderer, child);
		}
	}

	private void renderMesh(FearMesh mesh, Renderer renderer, TransformBuilder transformBuilder) {
		Vector3D position = mesh.getPosition();
		Rotation rotation = mesh.getRotation();
		//TransformBuilder transformBuilder = new TransformBuilder();
		//transformBuilder.translate(position);
		//transformBuilder.rotate(rotation);
		renderer.render(mesh, transformBuilder);
	}

	private void renderSkybox(Renderer output) {
	}

}
