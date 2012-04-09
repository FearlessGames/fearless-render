package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.collada.ColladaImporter;
import se.fearlessgames.fear.collada.ColladaStorage;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.texture.SingleTextureRenderState;
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.texture.TextureFileType;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;


public class ColladaExample extends ExampleBase {

	public ColladaExample() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/textured.vert",
				"src/main/resources/shaders/textured.frag");
	}

	@Override
	public void beforeRender() {
	}

	@Override
	public void afterRender() {
	}

	@Override
	public Scene createScene() throws IOException {
		Node root = new Node("root");

		String textureName = "src/main/resources/texture/bender.png";
		Texture texture = textureManager.load(textureName, TextureFileType.GUESS, new FileInputStream(textureName));

		ColladaImporter colladaImporter = new ColladaImporter();
		ColladaStorage colladaStorage = colladaImporter.load(new FileInputStream("src/main/resources/dae/bender.dae"));
		MeshData combinedMeshData = colladaStorage.getCombinedMeshData();

		VertexArrayObject vao = VaoBuilder.fromMeshData(fearGl, shaderProgram, combinedMeshData).build();

		MeshType chairType = new MeshType(shaderProgram, renderer.opaqueBucket, new DirectionalLightRenderState(new SunLight()), new SingleTextureRenderState(texture));
		Mesh chair = new Mesh(vao, chairType);

		Node boxNode = new Node("Chair", chair);
		boxNode.setPosition(new Vector3(0, 0, 0));
		//boxNode.setRotation(Quaternion.fromEulerAngles(90, 0, -90));
		//boxNode.setScale(new Vector3(0.1, 0.1, 0.1));
		root.addChild(boxNode);

		return new Scene(root);
	}


	public static void main(String[] args) throws Exception {
		ExampleBase example = new ColladaExample();
		example.startRenderLoop();
	}


}