package se.fearlessgames.fear;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.math.TransformBuilder;

import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyNever;
import static se.mockachino.matchers.Matchers.any;

public class FearSceneTest {

	private FearScene scene;
	private FearNode rootNode;
	private Renderer renderer;

	@Before
	public void setUp() throws Exception {
		rootNode = new FearNode();
		scene = new FearScene(rootNode);
		renderer = mock(Renderer.class);
	}

	@Test
	public void renderSimpleNode() throws Exception {
		scene.render(renderer);
		verifyNever().on(renderer).render(any(FearMesh.class), new TransformBuilder());
	}


}
