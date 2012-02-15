package se.fearlessgames.fear;

import org.junit.Test;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.when;

public class NodeTest {

	@Test
	public void noVertexCount() throws Exception {
		Node node = new Node();

		assertEquals(0, node.getVertexCount());
	}

	@Test
	public void simpleVertexCount() throws Exception {
		VertexBufferObject vbo = mock(VertexBufferObject.class);
		when(vbo.getIndexBufferSize()).thenReturn(4);
		Mesh mesh = new Mesh(vbo, mock(ShaderProgram.class));
		Node node = new Node("foo", mesh);

		assertEquals(4, node.getVertexCount());
	}

	@Test
	public void compoundNodeVertexCount() throws Exception {
		VertexBufferObject vbo = mock(VertexBufferObject.class);
		when(vbo.getIndexBufferSize()).thenReturn(4);
		Mesh mesh = new Mesh(vbo, mock(ShaderProgram.class));

		Node root = new Node("root");
		Node left = new Node("left", mesh);
		root.addChild(left);
		Node right = new Node("right");
		root.addChild(right);
		Node rightOne = new Node("rightOne", mesh);
		right.addChild(rightOne);
		Node rightTwo = new Node("rightTwo", mesh);
		right.addChild(rightTwo);

		assertEquals(12, root.getVertexCount());
	}
}
