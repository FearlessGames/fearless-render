package se.fearlessgames.fear.collada.data;

import se.fearlessgames.fear.math.Matrix4;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private final String name;
	private final List<Node> children = new ArrayList<Node>();
	private List<Mesh> meshes = new ArrayList<Mesh>();
	private Matrix4 transform;


	public Node(String name) {
		this.name = name;
	}

	public void attachChild(Node child) {
		children.add(child);
	}

	public void attachChild(Mesh child) {
		child.setParent(this);
		meshes.add(child);
	}

	public void setTransform(Matrix4 matrix4) {
		this.transform = matrix4;
	}

	public String getName() {
		return name;
	}

	public List<Node> getChildren() {
		return children;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}
}
