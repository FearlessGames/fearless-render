package se.fearlessgames.fear.collada;

import org.junit.Before;
import org.junit.Test;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.MeshData;

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
		ColladaStorage colladaStorage = colladaImporter.load(getInputData("fearless-render-collada/src/test/resources/notransform.dae"));
		assertNotNull("Collada storage was null", colladaStorage);

		MeshData meshData = colladaStorage.getScene().getChildren().get(0).getChildren().get(0).getMeshes().get(0);

		MeshData combinedMeshData = collada2Vbo.createCombinedMesh(colladaStorage.getScene());

		assertEqualsBuffer(meshData.getVertexBuffer(), combinedMeshData.getVertexBuffer());
		assertEqualsBuffer(meshData.getNormalBuffer(), combinedMeshData.getNormalBuffer());
		assertEqualsBuffer(meshData.getIndices(), combinedMeshData.getIndices());
		assertEqualsBuffer(meshData.getTextureCoordsMap().get(0), combinedMeshData.getTextureCoordsMap().get(0));

		assertEquals(meshData.getIndices().getClass(), combinedMeshData.getIndices().getClass());

		assertEquals(meshData.getVertexIndexMode(), combinedMeshData.getVertexIndexMode());
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
