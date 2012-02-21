package se.fearlessgames.fear;

import se.fearlessgames.fear.mesh.MeshRenderer;

import java.util.Collections;
import java.util.List;

public enum RenderBucket {
	OPAQUE {
		@Override
		public void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer) {
			Collections.sort(meshes, Sorters.FRONT_TO_BACK);
			for (Renderer.AddedMesh addedMesh : meshes) {
				meshRenderer.render(addedMesh.mesh, addedMesh.transform);
			}
		}
	},
	TRANSPARENT {
		@Override
		public void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer) {
			Collections.sort(meshes, Sorters.BACK_TO_FRONT);
			for (Renderer.AddedMesh addedMesh : meshes) {
				meshRenderer.render(addedMesh.mesh, addedMesh.transform);
			}
		}
	};

	public abstract void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer);
}
