package se.fearlessgames.fear;

import org.junit.Before;
import org.junit.Test;

import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.*;

public class FearSceneTest {

	private FearScene scene;
	private FearNode rootNode;
	private FearOutput output;

	@Before
	public void setUp() throws Exception {
		rootNode = new FearNode();
		scene = new FearScene(rootNode);
		output = mock(FearOutput.class);
	}

	@Test
	public void renderSimpleNode() throws Exception {
		scene.render(output);
		verifyNever().on(output).add(any(GlCommand.class));
	}


}
