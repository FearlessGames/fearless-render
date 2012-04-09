package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
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
		rot += 0.0005d;
		scene.getRoot().setRotation(Quaternion.fromEulerAngles(rot / 2, rot, 0));
	}

	@Override
	public void afterRender() {

	}

	public Scene createScene() throws IOException {
		MeshData meshData = new SphereFactory(100, 100, 1.5, SphereFactory.TextureMode.PROJECTED).create();

		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		Node root = new Node("root");

		String textureName = "src/main/resources/texture/earth.png";
		Texture texture = textureManager.load(textureName, TextureFileType.GUESS, new FileInputStream(textureName), TextureType.TEXTURE_2D, true);

		MeshType earthMeshType = new MeshType(shaderProgram, renderer.opaqueBucket, new DirectionalLightRenderState(new SunLight()), new SingleTextureRenderState(texture));
		Mesh earth = new Mesh(vertexArrayObject, earthMeshType);

		Node boxNode = new Node("Box", earth);
		//boxNode.setScale(new Vector3(1, 1.4, 0.2));
		root.addChild(boxNode);

		return new Scene(root);
	}


	public static void main(String[] args) throws Exception {
		ExampleBase example = new Main();
		example.startRenderLoop();
	}

}