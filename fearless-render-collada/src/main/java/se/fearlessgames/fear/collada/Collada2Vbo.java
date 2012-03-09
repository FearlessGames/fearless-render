package se.fearlessgames.fear.collada;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import se.fearlessgames.fear.collada.data.IndexMode;
import se.fearlessgames.fear.collada.data.Mesh;
import se.fearlessgames.fear.collada.data.Node;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector4;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Collada2Vbo {
	private final FearGl fearGl;

	public Collada2Vbo(FearGl fearGl) {
		this.fearGl = fearGl;
	}

	public VertexBufferObject create(Mesh mesh) {
		rewindMesh(mesh);
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

	public VertexBufferObject create(Node node) {
		Mesh mesh = createCombinedMesh(node);
		rewindMesh(mesh);
		return create(mesh);
	}

	protected Mesh createCombinedMesh(Node node) {
		CombinedMesh combinedMesh = new CombinedMesh();

		combinedMeshes(combinedMesh, new Matrix4(), node, new AtomicInteger());

		Mesh mesh = new Mesh("CombinedMesh-" + node.getName());
		mesh.setVertexBuffer(toFloatBuffer(combinedMesh.vertexBuffer));
		mesh.setNormalBuffer(toFloatBuffer(combinedMesh.normalBuffer));
		for (Integer texCoordsIndex : combinedMesh.textureCoordsMap.keySet()) {
			mesh.setTextureCoords(texCoordsIndex, toFloatBuffer(combinedMesh.textureCoordsMap.get(texCoordsIndex)));
		}
		mesh.setIndices(toIntBuffer(combinedMesh.indices));
		mesh.setIndexMode(combinedMesh.indexMode);
		return mesh;
	}

	private IntBuffer toIntBuffer(List<Integer> indices) {

		IntBuffer buffer = createIndexBufferData(indices.size());//ByteBuffer.allocateDirect(indices.size() * 4).asIntBuffer();
		for (Integer indice : indices) {
			buffer.put(indice);
		}
		return buffer;
	}

	private IntBuffer createIndexBufferData(int size) {
		final IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
		buf.clear();
		return buf;
	}

	private FloatBuffer toFloatBuffer(Collection<Float> vertexBuffer) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(vertexBuffer.size() * 4).asFloatBuffer();
		for (Float aFloat : vertexBuffer) {
			buffer.put(aFloat);
		}
		return buffer;
	}

	private void combinedMeshes(CombinedMesh combinedMesh, Matrix4 matrix4, Node rootNode, AtomicInteger indicesOffset) {
		for (Mesh mesh : rootNode.getMeshes()) {
			if (combinedMesh.indexMode == null) {
				combinedMesh.indexMode = mesh.getIndexMode();
			}

			if (combinedMesh.indexMode != mesh.getIndexMode()) {
				throw new ColladaException("Index mode have to be the same on all submeshes for combining them, offending mesh:" + mesh.getName(), mesh);
			}

			rewindMesh(mesh);

			for (int i = 0; i < mesh.getVertexBuffer().limit() / 3; i++) {
				Vector4 vertex = new Vector4(mesh.getVertexBuffer().get(), mesh.getVertexBuffer().get(), mesh.getVertexBuffer().get(), 1);
				vertex = matrix4.applyPost(vertex);
				combinedMesh.vertexBuffer.add((float) vertex.getX());
				combinedMesh.vertexBuffer.add((float) vertex.getY());
				combinedMesh.vertexBuffer.add((float) vertex.getZ());

				combinedMesh.normalBuffer.add(mesh.getNormalBuffer().get());
				combinedMesh.normalBuffer.add(mesh.getNormalBuffer().get());
				combinedMesh.normalBuffer.add(mesh.getNormalBuffer().get());

				for (Integer texCoordsIndex : mesh.getTextureCoordsMap().keySet()) {
					FloatBuffer textureBuffer = mesh.getTextureCoordsMap().get(texCoordsIndex);
					Collection<Float> floats = combinedMesh.textureCoordsMap.get(texCoordsIndex);
					floats.add(textureBuffer.get());
					floats.add(textureBuffer.get());
				}
			}

			for (int i = 0; i < mesh.getIndices().limit(); i++) {
				combinedMesh.indices.add(mesh.getIndices().get() + indicesOffset.get());
			}

			indicesOffset.addAndGet(mesh.getIndices().limit());

		}

		for (Node node : rootNode.getChildren()) {
			Matrix4 transform = matrix4;
			if (node.getTransform() != null) {
				transform = matrix4.multiply(node.getTransform());
			}
			combinedMeshes(combinedMesh, transform, node, indicesOffset);
		}
	}

	private void rewindMesh(Mesh mesh) {
		mesh.getVertexBuffer().rewind();
		mesh.getNormalBuffer().rewind();
		mesh.getIndices().rewind();

		for (Integer integer : mesh.getTextureCoordsMap().keySet()) {
			mesh.getTextureCoordsMap().get(integer).rewind();
		}
	}


	private static class CombinedMesh {
		private IndexMode indexMode;
		private List<Float> vertexBuffer = new ArrayList<Float>();
		private List<Float> normalBuffer = new ArrayList<Float>();
		private Multimap<Integer, Float> textureCoordsMap = ArrayListMultimap.create();

		private List<Integer> indices = new ArrayList<Integer>();
	}
}
