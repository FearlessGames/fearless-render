package se.fearlessgames.fear;

import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.renderbucket.RenderBucket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Renderer {
	protected final MeshRenderer meshRenderer;
	protected final List<RenderBucket> buckets;
	private final Set<RenderBucket> bucketSet;

	public Renderer(MeshRenderer meshRenderer, RenderBucket... buckets) {
		this.meshRenderer = meshRenderer;
		this.buckets = Arrays.asList(buckets);
		this.bucketSet = new HashSet<RenderBucket>(this.buckets);
	}

	public void render(CameraPerspective cameraPerspective) {
		for (RenderBucket bucket : buckets) {
			bucket.render(meshRenderer, cameraPerspective);
		}
	}

	public boolean containsBucket(RenderBucket bucket) {
		return bucketSet.contains(bucket);
	}
}
