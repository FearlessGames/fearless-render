package se.fearlessgames.fear;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;

import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyNever;
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
		scene.render(renderer);
		verifyNever().on(renderer).render(any(Mesh.class), any(Matrix4.class));
	}


}
