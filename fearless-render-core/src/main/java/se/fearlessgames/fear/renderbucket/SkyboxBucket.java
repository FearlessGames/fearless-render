package se.fearlessgames.fear.renderbucket;

import se.fearlessgames.fear.camera.CameraPerspective;
import se.fearlessgames.fear.gl.ClearBit;
import se.fearlessgames.fear.mesh.MeshRenderer;

import java.util.EnumSet;

public class SkyboxBucket extends OpaqueBucket {
	@Override
	public void render(MeshRenderer meshRenderer, CameraPerspective cameraPerspective) {
		meshRenderer.fearGl.glClear(EnumSet.of(ClearBit.GL_DEPTH_BUFFER_BIT));
		super.render(meshRenderer, cameraPerspective);
		meshRenderer.fearGl.glClear(EnumSet.of(ClearBit.GL_DEPTH_BUFFER_BIT));
	}
}
