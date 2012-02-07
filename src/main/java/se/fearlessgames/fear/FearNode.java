package se.fearlessgames.fear;

import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class FearNode {
	private final String name;
	private final List<FearNode> childNodes;
	private final List<FearMesh> meshes;
	private boolean visible = true;

	private Vector3 position = Vector3.ZERO;
	private Quaternion rotation = Quaternion.IDENTITY;
	private Vector3 scale = Vector3.ONE;

	public FearNode() {
		this("", new ArrayList<FearMesh>());
	}

	public FearNode(String name, List<FearMesh> meshes) {
		this.name = name;
		this.meshes = meshes;
		this.childNodes = new ArrayList<FearNode>();
	}

	public void addChild(FearNode node) {
		childNodes.add(node);
	}

	public List<FearNode> getChildNodes() {
		return childNodes;
	}

	public List<FearMesh> getMeshes() {
		return meshes;
	}

	public boolean isVisible() {
		return visible;
	}


	public void addMesh(FearMesh mesh) {
		meshes.add(mesh);
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public Vector3 getScale() {
		return scale;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "FearNode{" +
				"name='" + name + '\'' +
				'}';
	}
}
