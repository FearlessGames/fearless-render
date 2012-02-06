package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import se.fearlessgames.fear.FearMesh;
import se.fearlessgames.fear.FearNode;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VertexBufferObject;

public class Orb {
	private final FearNode root;
	private final FearMesh orb;
	private final double orbitSpeed;
	private final double rotationSpeed;

	public Orb(String name, VertexBufferObject vbo, double radius, double orbitSpeed, double rotationSpeed) {
		this.orbitSpeed = orbitSpeed;
		this.rotationSpeed = rotationSpeed;
		orb = new FearMesh(vbo);
		orb.setScale(new Vector3(radius * 2, radius * 2, radius * 2));
		root = new FearNode(name, Lists.newArrayList(orb));
	}

	public FearNode getRoot() {
		return root;
	}

	public FearMesh getOrb() {
		return orb;
	}

	public void update(long timeInMillis) {
		root.setRotation(Quaternion.fromEulerAngles(orbitSpeed * timeInMillis, 0, 0));
		orb.setRotation(Quaternion.fromEulerAngles(rotationSpeed * timeInMillis, 0, 0));
	}
}
