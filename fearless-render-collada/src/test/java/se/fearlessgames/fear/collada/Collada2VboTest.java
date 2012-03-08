package se.fearlessgames.fear.collada;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.collada.data.Mesh;
import se.fearlessgames.fear.gl.FearGl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.junit.Assert.*;
import static se.mockachino.Mockachino.mock;

public class Collada2VboTest {
	private Collada2Vbo collada2Vbo;

	@Before
	public void setUp() throws Exception {

		collada2Vbo = new Collada2Vbo(mock(FearGl.class));
	}

	@Test
	public void testCombineMeshWithJustOneMeshAndNoTransforms() {

		ColladaImporter colladaImporter = new ColladaImporter();
		ColladaStorage colladaStorage = colladaImporter.load(getInputData("fearless-render-collada/src/test/resources/boll.dae"));
		assertNotNull("Collada storage was null", colladaStorage);

		Mesh mesh = colladaStorage.getScene().getChildren().get(0).getChildren().get(0).getMeshes().get(0);
		colladaStorage.getScene().setTransform(null);
		colladaStorage.getScene().getChildren().get(0).setTransform(null);
		colladaStorage.getScene().getChildren().get(0).getChildren().get(0).setTransform(null);

		Mesh combinedMesh = collada2Vbo.createCombinedMesh(colladaStorage.getScene());

		assertEqualsBuffer(mesh.getVertexBuffer(), combinedMesh.getVertexBuffer());
		assertEqualsBuffer(mesh.getNormalBuffer(), combinedMesh.getNormalBuffer());
		assertEqualsBuffer(mesh.getIndices(), combinedMesh.getIndices());
	}

	private void assertEqualsBuffer(IntBuffer buffer1, IntBuffer buffer2) {
		buffer1.rewind();
		buffer2.rewind();
		assertEquals(buffer1.limit(), buffer2.limit());

		for (int i = 0; i < buffer1.limit(); i++) {
			assertEquals(buffer1.get(), buffer2.get());
		}
	}


	private void assertEqualsBuffer(FloatBuffer buffer1, FloatBuffer buffer2) {
		buffer1.rewind();
		buffer2.rewind();
		assertEquals(buffer1.limit(), buffer2.limit());

		for (int i = 0; i < buffer1.limit(); i++) {
			assertEquals(buffer1.get(), buffer2.get(), 0.00001);
		}
	}

	private InputStream getInputData(String fileName) {
		try {
			return new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e) {

			fail("Failed to find file " + e.getMessage());
		}
		return null;

	}
}
