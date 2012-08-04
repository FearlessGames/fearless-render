package se.fearlessgames.fear.renderbucket;

import se.fearlessgames.fear.TransformedMesh;
import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshRenderer;

import java.util.ArrayList;
import java.util.List;

public abstract class SortableBucket implements RenderBucket {
	private List<TransformedMesh> buffered = new ArrayList<TransformedMesh>();

	@Override
	public void render(MeshRenderer meshRenderer, Camera camera) {
		List<TransformedMesh> meshes = buffered;
		if (!buffered.isEmpty()) {
			buffered = new ArrayList<TransformedMesh>();
			sortMeshes(meshes);
			meshRenderer.renderMeshes(meshes, false, camera);
		}
	}

	protected abstract void sortMeshes(List<TransformedMesh> meshes);

	@Override
	public void add(Mesh mesh, Matrix4 transform) {
		buffered.add(new TransformedMesh(mesh, transform));
	}
}
