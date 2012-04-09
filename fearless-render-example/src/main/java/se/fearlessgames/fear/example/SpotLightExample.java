package se.fearlessgames.fear.example;

import se.fearlessgames.fear.ColorRGBA;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.light.MutableSpotLight;
import se.fearlessgames.fear.light.SpotLight;
import se.fearlessgames.fear.light.SpotLightRenderState;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
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
import java.util.Arrays;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class SpotLightExample extends ExampleBase {

	private double rot;


	public SpotLightExample() throws Exception {
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

	@Override
	public Scene createScene() throws IOException {
		MeshData meshData = new SphereFactory(100, 100, 1.5, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();


		Node root = new Node("root");
		MutableSpotLight spotLight = new MutableSpotLight();
		spotLight.setDirection(new Vector3(0, 0, -1));
		spotLight.setLocation(new Vector3(0, 0, 7));
		spotLight.setAngle(10);
		spotLight.setExponent(1);
		spotLight.setConstantAttenuation(1);
		spotLight.setLinearAttenuation(0.22f);
		spotLight.setQuadraticAttenuation(0.37f);
		spotLight.setLightColor(new ColorRGBA(1, 1, 1, 0));

		String textureName = "src/main/resources/texture/earth.png";
		Texture texture = textureManager.load(textureName, TextureFileType.PNG, new FileInputStream(textureName), TextureType.TEXTURE_2D, true);
		MeshType meshType = new MeshType(shaderProgram, renderer.opaqueBucket, new SpotLightRenderState(Arrays.asList((SpotLight) spotLight)), new SingleTextureRenderState(texture));
		Mesh earth = new Mesh(vertexArrayObject, meshType);


		Node boxNode = new Node("Box", earth);
		//boxNode.setScale(new Vector3(1, 1.4, 0.2));
		root.addChild(boxNode);

		return new Scene(root);
	}


	public static void main(String[] args) throws Exception {
		new SpotLightExample().startRenderLoop();
	}


}
