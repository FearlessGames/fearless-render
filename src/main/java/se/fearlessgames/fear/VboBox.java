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

		int[] indices = {
				0, 1, 2, 3,
				7, 6, 5, 4,
				1, 5, 6, 2,
				0, 3, 7, 4
		};

		return VboBuilder.fromArray(fearGl, data).indices(indices).quads().build();
	}


	public void draw(PerspectiveBuilder perspectiveBuilder) {
		fearGl.glUseProgram(shaderProgram.getShaderProgram());

		TransformBuilder transformBuilder = new TransformBuilder();
		transformBuilder.translate(new Vector3D(-3, -2, -10));
		transformBuilder.rotate(new Rotation(RotationOrder.XYZ, angle, angle * 0.5, angle * 0.3));

		setupUniforms(perspectiveBuilder, transformBuilder);

		vbo.draw();

		transformBuilder = new TransformBuilder();
		transformBuilder.translate(new Vector3D(3, -2, -10));
		transformBuilder.rotate(new Rotation(RotationOrder.XYZ, angle, angle * 0.5, angle * 0.3));

		setupUniforms(perspectiveBuilder, transformBuilder);

		vbo.draw();

		transformBuilder = new TransformBuilder();
		transformBuilder.translate(new Vector3D(3, 2, -10));
		transformBuilder.rotate(new Rotation(RotationOrder.XYZ, angle, angle * 0.5, angle * 0.3));

		setupUniforms(perspectiveBuilder, transformBuilder);

		vbo.draw();


		transformBuilder = new TransformBuilder();
		transformBuilder.translate(new Vector3D(-3, 2, -10));
		transformBuilder.rotate(new Rotation(RotationOrder.XYZ, angle, angle * 0.5, angle * 0.3));

		setupUniforms(perspectiveBuilder, transformBuilder);

		vbo.draw();


		fearGl.glUseProgram(0);
	}

	private void setupUniforms(PerspectiveBuilder perspectiveBuilder, TransformBuilder transformBuilder) {
		int projection = fearGl.glGetUniformLocation(shaderProgram.getShaderProgram(), "projection");
		fearGl.glUniformMatrix4(projection, false, perspectiveBuilder.getMatrix());

		int translation = fearGl.glGetUniformLocation(shaderProgram.getShaderProgram(), "translation");
		if (translation != -1) {
			fearGl.glUniformMatrix4(translation, false, transformBuilder.asFloatBuffer());
		} else {
			throw new RuntimeException("Failed to get translation location");
		}
	}


	public void update() {
		angle += 0.001;
	}


}
