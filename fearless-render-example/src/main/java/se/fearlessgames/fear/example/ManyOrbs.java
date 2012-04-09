package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.shape.BoxFactory;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.texture.*;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ManyOrbs extends ExampleBase {
	private TimeProvider timeProvider = new SystemTimeProvider();
	private List<Orb> orbs;

	public ManyOrbs() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/textured.vert",
				"src/main/resources/shaders/textured.frag");
	}

	@Override
	public void beforeRender() {
		long now = timeProvider.now();
		for (Orb orb : orbs) {
			orb.update(now);
		}
	}

	@Override
	public void afterRender() {
	}

	private Node createBoxNode(ShaderProgram shaderProgram, TextureLoader textureManager) {
		Texture texture = null;
		try {
			String textureName = "src/main/resources/texture/crate.png";
			texture = textureManager.load(textureName, TextureFileType.GUESS, new FileInputStream(textureName));
		} catch (IOException ignored) {
		}
		MeshType meshType = new MeshType(shaderProgram, renderer.opaqueBucket, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture));
		MeshData meshData = new BoxFactory().create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();
		Mesh boxMesh = new Mesh(vertexArrayObject, meshType);

		Node node = new Node("Center Box", boxMesh);
		node.setScale(new Vector3(4, 4, 4));
		node.setRotation(Quaternion.fromEulerAngles(-30, -30, 30));

		return node;
	}

	public Scene createScene() {
		int numOrbs = 20;
		int numTransparent = 10;

		Node root = new Node("root");
		Scene scene = new Scene(root);

		Random rand = new Random();
		TextureLoader textureManager = new FearlessTextureLoader(fearGl);
		Texture texture = null;
		try {
			String name = "src/main/resources/texture/earth.png";
			texture = textureManager.load(name, TextureFileType.GUESS, new FileInputStream(name), TextureType.TEXTURE_2D, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		MeshData meshData = new SphereFactory(100, 100, 2, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		MeshType orbMeshType = new MeshType(shaderProgram, renderer.opaqueBucket, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture));
		MeshType orbMeshType2 = new MeshType(shaderProgram, renderer.translucentBucket, DirectionalLightRenderState.DEFAULT, new TransparentTextureRenderState(texture));

		orbs = Lists.newArrayList();
		for (int i = 0; i < numOrbs; i++) {
			MeshType type = (i < numOrbs - numTransparent) ? orbMeshType : orbMeshType2;
			Orb orb = new Orb("orb" + i, vertexArrayObject, 0.5 + 1 * rand.nextDouble(), 1e-3 * rand.nextDouble(), 1e-3 * (rand.nextDouble() - 0.5), type);
			orb.setRotationRadius(new Vector3(30 * rand.nextDouble(), 20 * rand.nextDouble(), 0));
			orbs.add(orb);
			scene.getRoot().addChild(orb.getRoot());
		}

		scene.getRoot().addChild(createBoxNode(shaderProgram, textureManager));
		log.info("Scene contains {} vertices", scene.getRoot().getVertexCount());
		scene.getRoot().setPosition(new Vector3(0, -15, -80));
		return scene;
	}


	public static void main(String[] args) throws Exception {
		new ManyOrbs().startRenderLoop();
	}


}
