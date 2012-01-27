package se.fearlessgames.fear;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.TransformBuilder;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;


/**
 * Source: http://en.wikipedia.org/wiki/Vertex_Buffer_Object
 */

public class VboBox {

	private int shaderProgram;
	private double angle;
	private VertexBufferObject vbo;
	private final FearGl fearGl;

	public VboBox(FearGl fearGl) {
		this.fearGl = fearGl;
		createShaders();
		vbo = createVbo();
	}

	private void createShaders() {
		Shaders shaders = new Shaders();

		shaderProgram = shaders.createProgram();
		int vertexShader = shaders.loadAndCompileVertexShader("src/main/resources/shaders/screen.vert");
		int fragmentShader = shaders.loadAndCompileFragmentShader("src/main/resources/shaders/screen.frag");
		shaders.attachToProgram(shaderProgram, vertexShader, fragmentShader);

	}

	private VertexBufferObject createVbo() {
		float[] data = {
				// Front face (facing viewer), correct winding order.
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,

				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f
		};

		int[] indices = {
				0, 1, 2, 3,
				7, 6, 5, 4,
				1, 5, 6, 2,
				0, 3, 7, 4
		};

		return VboBuilder.fromArray(fearGl, data).indices(indices).quads().build();
	}


	public void draw(PerspectiveBuilder perspectiveBuilder) {
		fearGl.glUseProgram(shaderProgram);

		int projection = fearGl.glGetUniformLocation(shaderProgram, "projection");
		fearGl.glUniformMatrix4(projection, false, perspectiveBuilder.getMatrix());
		TransformBuilder builder = new TransformBuilder();
		builder.translate(new Vector3D(0, 0, -10));
		builder.rotate(new Rotation(RotationOrder.XYZ, angle, angle * 0.5, angle * 0.3));

		int translation = fearGl.glGetUniformLocation(shaderProgram, "translation");
		if (translation != -1) {
			fearGl.glUniformMatrix4(translation, false, builder.asFloatBuffer());
		} else {
			throw new RuntimeException("Failed to get translation location");
		}


		vbo.draw();

		fearGl.glUseProgram(0);
	}

	public void update() {
		angle += 0.001;
	}


}
