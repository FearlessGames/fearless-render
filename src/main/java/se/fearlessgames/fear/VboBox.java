package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.GlMatrixBuilder;
import se.fearlessgames.fear.math.PerspectiveBuilder;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;


/**
 * Source: http://en.wikipedia.org/wiki/Vertex_Buffer_Object
 */

public class VboBox {

	private double angle;
	private VertexBufferObject vbo;
	private final FearGl fearGl;
	private final ShaderProgram shaderProgram;

	public VboBox(FearGl fearGl, ShaderProgram shaderProgram) {
		this.fearGl = fearGl;
		this.shaderProgram = shaderProgram;
		vbo = createVbo();
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

		float[] colors = {
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f,
		};

		int[] indices = {
				0, 1, 2, 3,
				7, 6, 5, 4,
				1, 5, 6, 2,
				0, 3, 7, 4
		};

		return VboBuilder.fromArray(fearGl, data).indices(indices).colors(colors).quads().build();
	}


	public void draw(PerspectiveBuilder perspectiveBuilder) {
		fearGl.glUseProgram(shaderProgram.getShaderProgram());

		for (int x = -3; x < 4; x++) {
			for (int y = -3; y < 4; y++) {
				drawBox(perspectiveBuilder, new Vector3(x, y, -11));
			}
		}

		fearGl.glUseProgram(0);
	}

	private void drawBox(PerspectiveBuilder perspectiveBuilder, Vector3 offset) {
		Transformation transformation = new Transformation(offset, Quaternion.fromEulerAngles(angle, 0.5 * angle, 0.3 * angle), Vector3.ONE);

		setupUniforms(perspectiveBuilder, transformation);

		vbo.draw();
	}

	private void setupUniforms(PerspectiveBuilder perspectiveBuilder, Transformation transformation) {
		int projection = fearGl.glGetUniformLocation(shaderProgram.getShaderProgram(), "projection");
		fearGl.glUniformMatrix4(projection, false, perspectiveBuilder.getMatrix());

		int translation = fearGl.glGetUniformLocation(shaderProgram.getShaderProgram(), "translation");
		if (translation != -1) {
			fearGl.glUniformMatrix4(translation, false, GlMatrixBuilder.convert(transformation.asMatrix()));
		} else {
			throw new RuntimeException("Failed to get translation location");
		}
	}


	public void update() {
		angle += 0.001;
	}


}
