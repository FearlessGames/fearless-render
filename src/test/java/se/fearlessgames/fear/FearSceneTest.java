package se.fearlessgames.fear;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

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
		verifyNever().on(renderer).render(any(FearMesh.class), any(Matrix4.class));
	}


}
