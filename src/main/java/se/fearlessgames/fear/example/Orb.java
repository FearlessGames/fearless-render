package se.fearlessgames.fear.example;

import se.fearlessgames.fear.FearMesh;
import se.fearlessgames.fear.FearNode;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Orb {
	private final FearNode rotationCenterNode;
	private final FearNode meshCenterNode;
	private final FearMesh orbMesh;
	private final double orbitSpeed;
	private final double rotationSpeed;

	public Orb(String name, VertexBufferObject vbo, double radius, double orbitSpeed, double rotationSpeed) {
		this.orbitSpeed = orbitSpeed;
		this.rotationSpeed = rotationSpeed;
		orbMesh = new FearMesh(vbo);
		orbMesh.setScale(new Vector3(radius * 2, radius * 2, radius * 2));
		rotationCenterNode = new FearNode(name, orbMesh);
		meshCenterNode = new FearNode();
		rotationCenterNode.addChild(meshCenterNode);
	}

	public void setRotationRadius(Vector3 radius) {
		meshCenterNode.setPosition(radius);
		orbMesh.setPosition(radius);
	}

	public void addChild(Orb orb) {
		meshCenterNode.addChild(orb.rotationCenterNode);
	}

	public void update(long timeInMillis) {
		rotationCenterNode.setRotation(Quaternion.fromEulerAngles(orbitSpeed * timeInMillis, 0, 0));
		orbMesh.setRotation(Quaternion.fromEulerAngles(rotationSpeed * timeInMillis, 0, 0));
	}

	public FearNode getRoot() {
		return rotationCenterNode;
	}
}
