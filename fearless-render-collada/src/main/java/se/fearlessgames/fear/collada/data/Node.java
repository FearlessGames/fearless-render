package se.fearlessgames.fear.collada.data;

import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.MeshData;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private final String name;
	private final List<Node> children = new ArrayList<Node>();
	private List<MeshData> meshDatas = new ArrayList<MeshData>();
	private Matrix4 transform;


	public Node(String name) {
		this.name = name;
	}

	public void attachChild(Node child) {
		children.add(child);
	}

	public void attachChild(MeshData child) {
		meshDatas.add(child);
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

	public List<MeshData> getMeshes() {
		return meshDatas;
	}

	public Matrix4 getTransform() {
		return transform;
	}
}
