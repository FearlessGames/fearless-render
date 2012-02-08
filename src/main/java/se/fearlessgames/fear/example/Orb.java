package se.fearlessgames.fear.example;

import se.fearlessgames.fear.FearMesh;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Orb {
	private final Node rotationCenterNode;
	private final Node meshCenterNode;
	private final FearMesh orbMesh;
	private final double orbitSpeed;
	private final double rotationSpeed;
	private final Node meshNode;

	public Orb(String name, VertexBufferObject vbo, double radius, double orbitSpeed, double rotationSpeed) {
		this.orbitSpeed = orbitSpeed;
		this.rotationSpeed = rotationSpeed;
		orbMesh = new FearMesh(vbo);
		rotationCenterNode = new Node(name + "-rotationCenter");
		meshCenterNode = new Node(name + "-meshCenter");
		meshNode = new Node(name + "-mesh", orbMesh);
		meshNode.setScale(new Vector3(radius * 2, radius * 2, radius * 2));

		rotationCenterNode.addChild(meshCenterNode);
		meshCenterNode.addChild(meshNode);
	}

	public void setRotationRadius(Vector3 radius) {
		meshCenterNode.setPosition(radius);
	}

	public void addChild(Orb orb) {
		meshCenterNode.addChild(orb.rotationCenterNode);
	}

	public void update(long timeInMillis) {
		rotationCenterNode.setRotation(Quaternion.fromEulerAngles(orbitSpeed * timeInMillis, 0, 0));
		meshNode.setRotation(Quaternion.fromEulerAngles(rotationSpeed * timeInMillis, 0, 0));
	}

	public Node getRoot() {
		return rotationCenterNode;
	}
}
