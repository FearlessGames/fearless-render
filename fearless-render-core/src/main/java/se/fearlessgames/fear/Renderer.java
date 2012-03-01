package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshRenderer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;


public class Renderer {
	private final MeshRenderer meshRenderer;
	private final EnumMap<RenderBucket, List<AddedMesh>> addedMeshes;

	public Renderer(MeshRenderer meshRenderer) {
		this.meshRenderer = meshRenderer;
		addedMeshes = new EnumMap<RenderBucket, List<AddedMesh>>(RenderBucket.class);
		for (RenderBucket bucket : RenderBucket.values()) {
			addedMeshes.put(bucket, new ArrayList<AddedMesh>());
		}
	}

	public void addMeshToRender(Mesh mesh, Matrix4 matrix) {
		RenderBucket bucket = mesh.getBucket();
		addedMeshes.get(bucket).add(new AddedMesh(mesh, matrix));
	}

	public void render() {
		for (RenderBucket bucket : RenderBucket.values()) {
			List<AddedMesh> meshesToRender = addedMeshes.get(bucket);
			if (!meshesToRender.isEmpty()) {
				addedMeshes.put(bucket, new ArrayList<AddedMesh>());
				bucket.render(meshesToRender, meshRenderer);
			}
		}
	}

	public static class AddedMesh {
		public final Mesh mesh;
		public final Matrix4 transform;

		public AddedMesh(Mesh mesh, Matrix4 transform) {
			this.mesh = mesh;
			this.transform = transform;
		}
	}

}
