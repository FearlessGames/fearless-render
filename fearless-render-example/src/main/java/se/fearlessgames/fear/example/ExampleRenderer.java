package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.TranslucentBucket;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.renderbucket.OpaqueBucket;
import se.fearlessgames.fear.renderbucket.RenderBucket;


public class ExampleRenderer extends Renderer {

	public final RenderBucket opaqueBucket;
	public final RenderBucket translucentBucket;

	public ExampleRenderer(MeshRenderer meshRenderer) {
		super(meshRenderer, new OpaqueBucket(), new TranslucentBucket());
		opaqueBucket = buckets.get(0);
		translucentBucket = buckets.get(1);
	}
}
