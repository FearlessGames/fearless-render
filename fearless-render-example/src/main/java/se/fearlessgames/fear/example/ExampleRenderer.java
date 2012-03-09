package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Renderer;
import se.fearlessgames.fear.TranslucentBucket;
import se.fearlessgames.fear.mesh.MeshRenderer;
import se.fearlessgames.fear.renderbucket.OpaqueBucket;
import se.fearlessgames.fear.renderbucket.RenderBucket;
import se.fearlessgames.fear.renderbucket.SkyboxBucket;


public class ExampleRenderer extends Renderer {

	public final RenderBucket opaqueBucket;
	public final RenderBucket translucentBucket;
	public final RenderBucket skyboxBucket;

	public ExampleRenderer(MeshRenderer meshRenderer) {
		super(meshRenderer, new SkyboxBucket(), new OpaqueBucket(), new TranslucentBucket());
		skyboxBucket = buckets.get(0);
		opaqueBucket = buckets.get(1);
		translucentBucket = buckets.get(2);
	}
}
