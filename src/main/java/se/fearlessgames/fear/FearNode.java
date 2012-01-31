package se.fearlessgames.fear;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class FearNode {
	private final List<FearNode> childNodes;
	private final List<FearMesh> meshes;
	private boolean visible = true;

	private Vector3D position = Vector3D.ZERO;
	private Rotation rotation = Rotation.IDENTITY;

	public FearNode() {
		this(new ArrayList<FearMesh>());
	}

	public FearNode(List<FearMesh> meshes) {
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

	public Vector3D getPosition() {
		return position;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void addMesh(FearMesh mesh) {
		meshes.add(mesh);
	}

	public void setPosition(Vector3D translation) {
		this.position = translation;
	}
}
