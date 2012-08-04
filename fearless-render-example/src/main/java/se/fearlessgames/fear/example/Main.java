package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.shape.BoxFactory;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.texture.SingleTextureRenderState;
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.texture.TextureFileType;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class Main extends ExampleBase {
	private double rot;

	public Main() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/textured.vert",
				"src/main/resources/shaders/textured.frag");

	}

	@Override
	public void beforeRender() {
//		rot += 0.0005d;
//		scene.getRoot().setRotation(Quaternion.fromEulerAngles(rot / 2, rot, 0));
	}

	@Override
	public void afterRender() {

	}

	public Scene createScene() throws IOException {
		MeshData meshData = new SphereFactory(100, 100, 1.5, SphereFactory.TextureMode.PROJECTED).create();
		//MeshData meshData = new BoxFactory().create();

		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		Node root = new Node("root");

		String textureName = "src/main/resources/texture/earth.png";
		Texture texture = textureManager.load(textureName, TextureFileType.GUESS, new FileInputStream(textureName), TextureType.TEXTURE_2D, true);

		MeshType earthMeshType = new MeshType(shaderProgram, renderer.opaqueBucket, new DirectionalLightRenderState(new SunLight()), new SingleTextureRenderState(texture));
		Mesh earth = new Mesh(vertexArrayObject, earthMeshType);

		dodo(root, earth, new Vector3(0, 0, 0));
		dodo(root, earth, new Vector3(0, 0, 10));
		dodo(root, earth, new Vector3(10, 0, 0));
		dodo(root, earth, new Vector3(-10, 0, 0));
		dodo(root, earth, new Vector3(0, 10, 0));
		dodo(root, earth, new Vector3(0, -10, 0));



		return new Scene(root);
	}

	private void dodo(Node root, Mesh earth, Vector3 position1) {
		Node node = new Node("Box", earth);
		node.setPosition(position1);
		root.addChild(node);
	}


	public static void main(String[] args) throws Exception {
		ExampleBase example = new Main();
		example.startRenderLoop();
	}

}