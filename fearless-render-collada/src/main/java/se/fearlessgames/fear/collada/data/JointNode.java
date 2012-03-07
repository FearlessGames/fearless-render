package se.fearlessgames.fear.collada.data;

import com.google.common.collect.Lists;

import java.util.List;

public class JointNode {
	private JointNode parent;
	private final List<JointNode> children = Lists.newArrayList();
	private final Joint joint;

	public JointNode(final Joint joint) {
		this.joint = joint;
	}

	public List<JointNode> getChildren() {
		return children;
	}

	public Joint getJoint() {
		return joint;
	}

	public JointNode getParent() {
		return parent;
	}

	public void setParent(final JointNode parent) {
		this.parent = parent;
	}
}