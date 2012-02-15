package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

import java.util.ArrayList;
import java.util.List;


public class Renderer {

	private final MeshRenderer meshRenderer;
	private final List<AddedMesh> addedMeshes;

	public Renderer(MeshRenderer meshRenderer) {
		this.meshRenderer = meshRenderer;
		addedMeshes = new ArrayList<AddedMesh>();
	}

	public void addMeshToRender(Mesh mesh, Matrix4 matrix) {
		addedMeshes.add(new AddedMesh(mesh, matrix));
	}

	public void render() {
		List<AddedMesh> meshesToRender = new ArrayList<AddedMesh>(addedMeshes);
		addedMeshes.clear();

		optimiseObjects(meshesToRender);

		for (AddedMesh addedMesh : meshesToRender) {
			meshRenderer.render(addedMesh.mesh, addedMesh.transform);
		}
	}

	private void optimiseObjects(List<AddedMesh> meshesToRender) {
		//todo: sort meshes, cull them etc
	}

	private static class AddedMesh {
		Mesh mesh;
		Matrix4 transform;

		private AddedMesh(Mesh mesh, Matrix4 transform) {
			this.mesh = mesh;
			this.transform = transform;
		}
	}

}
