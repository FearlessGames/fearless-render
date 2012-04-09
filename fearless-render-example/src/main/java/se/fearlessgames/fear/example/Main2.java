package se.fearlessgames.fear.example;

import com.google.common.collect.Lists;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.TimeProvider;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.Skybox;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.renderbucket.RenderBucket;
import se.fearlessgames.fear.shape.SphereFactory;
import se.fearlessgames.fear.texture.*;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/*
* Sets up the Display, the GL context, and runs the main game
loop.
*/
public class Main2 extends ExampleBase {
	private List<Orb> orbs;
	private final TimeProvider timeProvider = new SystemTimeProvider();
	private Skybox skybox;

	public Main2() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/screen.vert",
				"src/main/resources/shaders/screen.frag");


		skybox = new Skybox();
		skybox.getRoot().addChild(new Node("skybox-sphere", createSkyboxSphere(fearGl, shaderProgram, renderer.skyboxBucket)));
		scene.getRoot().addChild(skybox.getRoot());
	}

	@Override
	public void beforeRender() {
		long now = timeProvider.now();
		for (Orb orb : orbs) {
			orb.update(now);
		}
		skybox.moveToCamera(camera);
	}

	@Override
	public void afterRender() {
	}

	private Mesh createSkyboxSphere(FearGl fearGl, ShaderProgram shaderProgram, RenderBucket skyboxBucket) {
		TextureLoader textureManager = new FearlessTextureLoader(fearGl);
		Texture texture = null;
		try {
			String resourceName = "src/main/resources/texture/earth.png";
			texture = textureManager.load(resourceName, TextureFileType.PNG, new FileInputStream(resourceName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		MeshData meshData = new SphereFactory(100, 100, 100, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vertexArrayObject = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();

		return new Mesh(vertexArrayObject, new MeshType(shaderProgram, skyboxBucket, DirectionalLightRenderState.DEFAULT, new SingleTextureRenderState(texture)));
	}

	@Override
	public Scene createScene() {
		orbs = Lists.newArrayList();
		MeshData meshData = new SphereFactory(40, 40, 1, SphereFactory.TextureMode.PROJECTED).create();
		VertexArrayObject vao = VaoBuilder.fromMeshData(fearGl, shaderProgram, meshData).build();
		Node root = new Node("root");


		Orb sun = new Orb("Sun", vao, 2.5, 0, 0, new MeshType(shaderProgram, renderer.opaqueBucket));

		Orb planet = new Orb("Planet", vao, 1, 1e-3, 1e-3, new MeshType(shaderProgram, renderer.opaqueBucket));
		planet.setRotationRadius(new Vector3(30, 0, 0));
		sun.addChild(planet);

		Orb moon = new Orb("Moon", vao, 0.25, 1e-2, 1e-5, new MeshType(shaderProgram, renderer.opaqueBucket));
		moon.setRotationRadius(new Vector3(10, 0, 0));
		planet.addChild(moon);

		orbs.add(sun);
		orbs.add(planet);
		orbs.add(moon);

		root.addChild(sun.getRoot());

		return new Scene(root);
	}


	public static void main(String[] args) throws Exception {
		new Main2().startRenderLoop();
	}


}