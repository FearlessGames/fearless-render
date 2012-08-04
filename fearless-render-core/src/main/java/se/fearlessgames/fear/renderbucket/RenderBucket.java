package se.fearlessgames.fear.renderbucket;

import se.fearlessgames.fear.camera.Camera;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshRenderer;

public interface RenderBucket {
	void add(Mesh mesh, Matrix4 transform);

	void render(MeshRenderer meshRenderer, Camera camera);
}
