package se.fearlessgames.fear.example;

import com.google.common.base.Predicate;
import org.lwjgl.opengl.Display;
import se.fearlessgames.fear.Node;
import se.fearlessgames.fear.Scene;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.input.hw.HardwareKeyboardController;
import se.fearlessgames.fear.input.hw.HardwareMouseController;
import se.fearlessgames.fear.light.DirectionalLightRenderState;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshData;
import se.fearlessgames.fear.mesh.MeshType;
import se.fearlessgames.fear.shape.BoxFactory;
import se.fearlessgames.fear.texture.SingleTextureRenderState;
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.texture.TextureFileType;
import se.fearlessgames.fear.vbo.VaoBuilder;
import se.fearlessgames.fear.vbo.VertexArrayObject;

import java.io.FileInputStream;
import java.io.IOException;

public class InputTestExample extends ExampleBase {

	private InputHandler inputHandler;

	public InputTestExample() throws Exception {
		super(800, 600,
				"src/main/resources/shaders/textured.vert",
				"src/main/resources/shaders/textured.frag");


		inputHandler = new InputHandler(new InputController(new HardwareKeyboardController(), new HardwareMouseController()));
		inputHandler.addTrigger(
				new InputTrigger(new TriggerAction() {
					@Override
					public void perform(InputState inputState) {
						MouseState mouseState = inputState.getMouseState();
						System.out.println("x:" + mouseState.getX() + " y: " + mouseState.getY());


					}
				}, new Predicate<InputState>() {
					@Override
					public boolean apply(InputState input) {
						return true;
					}
				}
				));
	}


	@Override
	public void beforeRender() {
		inputHandler.poll();
	}

	@Override
	public void startRenderLoop() {
		while (true) {
			beforeRender();
			render();
			afterRender();

			Display.update();
		}
	}

	@Override
	public void afterRender() {
	}

	public Scene createScene() {
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
		node.setPosition(new Vector3(0, -15, -80));

		return new Scene(node);
	}


	public static void main(String[] args) throws Exception {
		new InputTestExample().startRenderLoop();
	}


}
