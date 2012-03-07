package se.fearlessgames.fear.collada;

import se.fearlessgames.fear.collada.data.IndexMode;
import se.fearlessgames.fear.collada.data.Mesh;
import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.nio.FloatBuffer;
import java.util.List;

public class Collada2Vbo {
	private final FearGl fearGl;

	public Collada2Vbo(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public VertexBufferObject create(ColladaStorage colladaStorage) {
		Mesh mesh = findFirstMesh(colladaStorage.getScene());

		VboBuilder vboBuilder = VboBuilder.fromBuffer(fearGl, mesh.getVertexBuffer());

		vboBuilder.indices(mesh.getIndices());
		vboBuilder.normals(mesh.getNormalBuffer());
		FloatBuffer texCoords = mesh.getTextureCoordsMap().get(0);

		if (texCoords != null) {
			vboBuilder.textureCoords(texCoords);
		}

		if (mesh.getIndexMode() == IndexMode.Triangles) {
			vboBuilder.triangles();
		} else if (mesh.getIndexMode() == IndexMode.Quads) {
			vboBuilder.quads();
		}

		return vboBuilder.build();

	}

	private Mesh findFirstMesh(Node rootNode) {
		if (rootNode.getMeshes().isEmpty()) {
			List<Node> children = rootNode.getChildren();
			for (Node node : children) {
				return findFirstMesh(node);
			}
		} else {
			return rootNode.getMeshes().get(0);
		}
		return null;
	}
}
