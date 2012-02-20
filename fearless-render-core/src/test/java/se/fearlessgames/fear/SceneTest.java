package se.fearlessgames.fear;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.mesh.Mesh;

import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;

public class SceneTest {

	private Scene scene;
	private Node rootNode;
	private Renderer renderer;

	@Before
	public void setUp() throws Exception {
		rootNode = new Node();
		scene = new Scene(rootNode);
		renderer = mock(Renderer.class);
	}

	@Test
	public void renderSimpleNode() throws Exception {
		Transformation mock = mock(Transformation.class);
		when(mock.asMatrix()).thenReturn(new Matrix4());
		scene.render(renderer, mock);
		verifyNever().on(renderer).addMeshToRender(any(Mesh.class), any(Matrix4.class));
	}


}
