package se.fearlessgames.fear.shape;

import se.fearlessgames.fear.BufferUtils;
import se.fearlessgames.fear.gl.VertexIndexMode;
import se.fearlessgames.fear.math.MathUtils;
import se.fearlessgames.fear.math.Vector3;
import se.fearlessgames.fear.mesh.MeshData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class SphereFactory implements ShapeFactory {
	private final int zSamples;
	private final int radialSamples;
	private final double radius;
	private final Vector3 center = Vector3.ZERO;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private IntBuffer indexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer textureCoordinates;
	private final TextureMode textureMode;

	public SphereFactory(int zSamples, int radialSamples, double radius, TextureMode textureMode) {
		this.zSamples = zSamples;
		this.radialSamples = radialSamples;
		this.radius = radius;
		this.textureMode = textureMode;

		createGeometryData();
		createIndexData();
	}

	@Override
	public MeshData create() {
		int verts = vertexBuffer.limit() / 3;
		int normals = normalBuffer.limit() / 3;
		if (verts != normals) {
			throw new RuntimeException(String.format("%d vertices but %d normals", verts, normals));
		}
		int colors = colorBuffer.limit() / 4;
		if (verts != colors) {
			throw new RuntimeException(String.format("%d vertices but %d color", verts, colors));
		}
		int texCoords = textureCoordinates.limit() / 2;
		if (verts != texCoords) {
			throw new RuntimeException(String.format("%d vertices but %d texture coordinates", verts, texCoords));
		}

		return new MeshData("Sphere", vertexBuffer, normalBuffer, colorBuffer, textureCoordinates, indexBuffer, VertexIndexMode.TRIANGLES);
	}

	private void createGeometryData() {
		// allocate vertices
		final int verts = (zSamples - 2) * (radialSamples + 1) + 2;
		vertexBuffer = BufferUtils.createFloat3Buffer(verts);
		normalBuffer = BufferUtils.createFloat3Buffer(verts);
		colorBuffer = BufferUtils.createFloat4Buffer(verts);
		textureCoordinates = BufferUtils.createFloat2Buffer(verts);


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
				final double fRadialFraction = iR * fInvRS; // in [0,1)
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


				if (textureMode == TextureMode.LINEAR) {
					textureCoordinates.put((float) fRadialFraction).put(
							(float) (0.5 * (fZFraction + 1.0)));
				} else if (textureMode == TextureMode.PROJECTED) {
					textureCoordinates.put((float) fRadialFraction).put(
							(float) (MathUtils.INV_PI * (MathUtils.HALF_PI + Math.asin(fZFraction))));
				} else if (textureMode == TextureMode.POLAR) {
					final double r = (MathUtils.HALF_PI - Math.abs(fAFraction)) / MathUtils.PI;
					final double u = r * afCos[iR] + 0.5;
					final double v = r * afSin[iR] + 0.5;
					textureCoordinates.put((float) u).put((float) v);
				}

				i++;
			}

			copyInternalVector(vertexBuffer, iSave, i, 3);
			copyInternalVector(normalBuffer, iSave, i, 3);

			if (textureMode == TextureMode.LINEAR) {
				textureCoordinates.put(1.0f).put((float) (0.5 * (fZFraction + 1.0)));
			} else if (textureMode == TextureMode.PROJECTED) {
				textureCoordinates.put(1.0f).put(
						(float) (MathUtils.INV_PI * (MathUtils.HALF_PI + Math.asin(fZFraction))));
			} else if (textureMode == TextureMode.POLAR) {
				final float r = (float) ((MathUtils.HALF_PI - Math.abs(fAFraction)) / MathUtils.PI);
				textureCoordinates.put(r + 0.5f).put(0.5f);
			}

			i++;
		}


		// south pole
		vertexBuffer.position(i * 3);
		vertexBuffer.put((float) center.getX()).put((float) center.getY()).put((float) (center.getZ() - radius));
		normalBuffer.position(i * 3);
		normalBuffer.put(0).put(0).put(-1);

		if (textureMode == TextureMode.POLAR) {
			textureCoordinates.put(0.5f).put(0.5f);
		} else {
			textureCoordinates.put(0.5f).put(0.0f);
		}

		// north pole
		vertexBuffer.put((float) center.getX()).put((float) center.getY()).put((float) (center.getZ() + radius));
		normalBuffer.put(0).put(0).put(1);

		if (textureMode == TextureMode.POLAR) {
			textureCoordinates.put(0.5f).put(0.5f);
		} else {
			textureCoordinates.put(0.5f).put(1.0f);
		}


		normalBuffer.flip();
		vertexBuffer.flip();
		textureCoordinates.flip();

	}

	private void copyInternalVector(final FloatBuffer buf, final int fromPos, final int toPos, int size) {
		final float[] data = new float[size];
		buf.position(fromPos * size);
		buf.get(data);
		buf.position(toPos * size);
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

	public enum TextureMode {
		LINEAR,
		PROJECTED,
		POLAR
	}

}
