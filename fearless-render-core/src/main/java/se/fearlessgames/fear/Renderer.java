package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.renderbucket.RenderBucket;

import java.util.*;


public class Renderer {
	protected final MeshRenderer meshRenderer;
	protected final List<RenderBucket> buckets;
	private final Set<RenderBucket> bucketSet;

	public Renderer(MeshRenderer meshRenderer, RenderBucket... buckets) {
		this.meshRenderer = meshRenderer;
		this.buckets = Arrays.asList(buckets);
		this.bucketSet = new HashSet<RenderBucket>(this.buckets);
	}

	public void render() {
		for (RenderBucket bucket : buckets) {
			bucket.render(meshRenderer);
		}
	}

	public boolean containsBucket(RenderBucket bucket) {
		return bucketSet.contains(bucket);
	}
}
