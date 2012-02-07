package se.fearlessgames.fear.shape;

import org.lwjgl.BufferUtils;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.math.MathUtils;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.vbo.VboBuilder;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class SphereFactory implements ShapeFactory {
	private final FearGl fearGl;
	private final int zSamples;
	private final int radialSamples;
	private final double radius;
	private final Vector3 center = Vector3.ZERO;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private IntBuffer indexBuffer;
	private FloatBuffer colorBuffer;

	public SphereFactory(FearGl fearGl, int zSamples, int radialSamples, double radius) {
		this.fearGl = fearGl;
		this.zSamples = zSamples;
		this.radialSamples = radialSamples;
		this.radius = radius;

		createGeometryData();
		createIndexData();
	}

	@Override
	public VertexBufferObject create() {
		return VboBuilder.fromBuffer(fearGl, vertexBuffer).normals(normalBuffer).indices(indexBuffer).colors(colorBuffer).triangles().build();
	}

	private void createGeometryData() {
		// allocate vertices
		final int verts = (zSamples - 2) * (radialSamples + 1) + 2;
		vertexBuffer = ByteBuffer.allocateDirect(4 * 3 * verts).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normalBuffer = ByteBuffer.allocateDirect(4 * 3 * verts).order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer = ByteBuffer.allocateDirect(4 * 4 * verts).order(ByteOrder.nativeOrder()).asFloatBuffer();


		// generate geometry
		final double fInvRS = 1.0 / radialSamples;
		final double fZFactor = 2.0 / (zSamples - 1);

		// Generate points on the unit circle to be used in computing the mesh
		// points on a sphere slice.
		final double[] afSin = new double[(radialSamples + 1)];
		final double[] afCos = new double[(radialSamples + 1)];
		for (int iR = 0; iR < radialSamples; iR++) {
			final double fAngle = MathUtils.TWO_PI * fInvRS * iR;
			afCos[iR] = MathUtils.cos(fAngle);
			afSin[iR] = MathUtils.sin(fAngle);
		}
		afSin[radialSamples] = afSin[0];
		afCos[radialSamples] = afCos[0];

		Random random = new Random();

		// generate the sphere itself
		int i = 0;

		for (int iZ = 1; iZ < (zSamples - 1); iZ++) {
			final double fAFraction = MathUtils.HALF_PI * (-1.0f + fZFactor * iZ); // in (-pi/2, pi/2)
			final double fZFraction = MathUtils.sin(fAFraction); // in (-1,1)
			final double fZ = radius * fZFraction;

			// compute center of slice
			final Vector3 kSliceCenter = new Vector3(0, 0, fZ);


			// compute radius of slice
			final double fSliceRadius = Math.sqrt(Math.abs(radius * radius - fZ * fZ));

			// compute slice vertices with duplication at end point

			final int iSave = i;
			for (int iR = 0; iR < radialSamples; iR++) {
				final Vector3 kRadial = new Vector3(afCos[iR], afSin[iR], 0);
				Vector3 tempVa = kRadial.multiply(fSliceRadius);
				float x = (float) (kSliceCenter.getX() + tempVa.getX());
				float y = (float) (kSliceCenter.getY() + tempVa.getY());
				float z = (float) (kSliceCenter.getZ() + tempVa.getZ());
				vertexBuffer.put(x).put(y).put(z);

				Vector3 kNormal = new Vector3(x, y, z).normalize();

				normalBuffer.put((float) kNormal.getX()).put((float) kNormal.getY()).put((float) kNormal.getZ());

				colorBuffer.
						put(random.nextFloat()).
						put(random.nextFloat()).
						put(random.nextFloat()).
						put(0.0f);

				i++;
			}

			copyInternalVector3(vertexBuffer, iSave, i);
			copyInternalVector3(normalBuffer, iSave, i);

			i++;
		}


		// south pole
		vertexBuffer.position(i * 3);
		vertexBuffer.put((float) center.getX()).put((float) center.getY()).put((float) (center.getZ() - radius));
		normalBuffer.position(i * 3);
		normalBuffer.put(0).put(0).put(-1);

		// north pole
		vertexBuffer.put((float) center.getX()).put((float) center.getY()).put((float) (center.getZ() + radius));
		normalBuffer.put(0).put(0).put(1);

		normalBuffer.flip();
		vertexBuffer.flip();

	}

	private void copyInternalVector3(final FloatBuffer buf, final int fromPos, final int toPos) {
		final float[] data = new float[3];
		buf.position(fromPos * 3);
		buf.get(data);
		buf.position(toPos * 3);
		buf.put(data);
	}

	private void createIndexData() {
		// allocate connectivity
		final int verts = (zSamples - 2) * (radialSamples + 1) + 2;
		final int tris = 2 * (zSamples - 2) * radialSamples;
		indexBuffer = BufferUtils.createIntBuffer(3 * tris);


		// generate connectivity
		int index = 0;
		for (int iZ = 0, iZStart = 0; iZ < (zSamples - 3); iZ++) {
			int i0 = iZStart;
			int i1 = i0 + 1;
			iZStart += (radialSamples + 1);
			int i2 = iZStart;
			int i3 = i2 + 1;
			for (int i = 0; i < radialSamples; i++, index += 6) {
				indexBuffer.put(i0++);
				indexBuffer.put(i1);
				indexBuffer.put(i2);
				indexBuffer.put(i1++);
				indexBuffer.put(i3++);
				indexBuffer.put(i2++);

			}
		}

		// south pole triangles
		for (int i = 0; i < radialSamples; i++, index += 3) {
			indexBuffer.put(i);
			indexBuffer.put(verts - 2);
			indexBuffer.put(i + 1);
		}

		// north pole triangles
		final int iOffset = (zSamples - 3) * (radialSamples + 1);
		for (int i = 0; i < radialSamples; i++, index += 3) {
			indexBuffer.put(i + iOffset);
			indexBuffer.put(i + 1 + iOffset);
			indexBuffer.put(verts - 1);
		}

		indexBuffer.flip();
	}

}
