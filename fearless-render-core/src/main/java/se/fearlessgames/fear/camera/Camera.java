package se.fearlessgames.fear.camera;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector3;

public interface Camera {
	CameraPerspective getPerspective();

	Vector3 getLocation();

	Matrix4 getViewProjectionMatrix();

	Matrix4 getViewMatrix();
}
