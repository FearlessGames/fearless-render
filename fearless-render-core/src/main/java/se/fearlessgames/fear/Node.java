package se.fearlessgames.fear;

import com.google.common.collect.Lists;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;

import java.util.List;

public class Node {
	private final String name;
	private final List<Node> childNodes;
	private final Mesh mesh;
	private boolean visible = true;

	private Vector3 position = Vector3.ZERO;
	private Quaternion rotation = Quaternion.IDENTITY;
	private Vector3 scale = Vector3.ONE;

	public Node() {
		this("");
	}


	public Node(String root) {
		this(root, null);
	}

	public Node(String name, Mesh mesh) {
		this.name = name;
		this.mesh = mesh;
		this.childNodes = Lists.newArrayList();
	}


	public void addChild(Node node) {
		childNodes.add(node);
	}

	public List<Node> getChildNodes() {
		return childNodes;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public boolean isVisible() {
		return visible;
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
		return "Node{" +
				"name='" + name + '\'' +
				'}';
	}

	public int getVertexCount() {
		int count = 0;
		for (Node childNode : childNodes) {
			count += childNode.getVertexCount();
		}
		if (mesh != null) {
			count += mesh.getVbo().getIndexBufferSize();
		}

		return count;
	}
}
