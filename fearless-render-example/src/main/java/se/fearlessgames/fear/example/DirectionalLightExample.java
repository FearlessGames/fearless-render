package se.fearlessgames.fear.example;

import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
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

public class DirectionalLightExample extends ExampleBase {

	public DirectionalLightExample() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/textured.vert",
				"src/main/resources/shaders/textured.frag");
	}


	@Override
	public Scene createScene() throws IOException {
		MeshData meshData = new SphereFactory(100, 100, 1.5, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		Node root = new Node("root");
		String textureName = "src/main/resources/texture/earth.png";
		Texture texture = textureManager.load(textureName, TextureFileType.PNG, new FileInputStream(textureName), TextureType.TEXTURE_2D, true);
		MeshType meshType = new MeshType(shaderProgram, renderer.opaqueBucket, new DirectionalLightRenderState(new SunLight()), new SingleTextureRenderState(texture));
		Mesh earth = new Mesh(vertexArrayObject, meshType);

		Node boxNode = new Node("Box", earth);
		//boxNode.setScale(new Vector3(1, 1.4, 0.2));
		root.addChild(boxNode);

		return new Scene(root);
	}

	@Override
	public void beforeRender() {
	}

	@Override
	public void afterRender() {
	}

	public static void main(String[] args) throws Exception {
		new DirectionalLightExample().startRenderLoop();
	}


}