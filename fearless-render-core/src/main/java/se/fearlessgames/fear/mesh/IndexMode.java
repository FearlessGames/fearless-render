package se.fearlessgames.fear.mesh;

public enum IndexMode {
	Triangles(true),
	/**
	 * The first three vertices referenced by the indexbuffer create a triangle, from there, every additional vertex is
	 * paired with the two preceding vertices to make a new triangle.
	 */
	TriangleStrip(true),
	/**
	 * The first three vertices (V0, V1, V2) referenced by the indexbuffer create a triangle, from there, every
	 * additional vertex is paired with the preceding vertex and the initial vertex (V0) to make a new triangle.
	 */
	TriangleFan(true),

	// QUADMESH
	/**
	 * Every four vertices referenced by the indexbuffer will be considered a stand-alone quad.
	 */
	Quads(true),
	/**
	 * The first four vertices referenced by the indexbuffer create a triangle, from there, every two additional
	 * vertices are paired with the two preceding vertices to make a new quad.
	 */
	QuadStrip(true),

	// LINE
	/**
	 * Every two vertices referenced by the indexbuffer will be considered a stand-alone line segment.
	 */
	Lines(false),
	/**
	 * The first two vertices referenced by the indexbuffer create a line, from there, every additional vertex is paired
	 * with the preceding vertex to make a new, connected line.
	 */
	LineStrip(false),
	/**
	 * Identical to <i>LineStrip</i> except the final indexed vertex is then connected back to the initial vertex to
	 * form a loop.
	 */
	LineLoop(false),

	// POINT
	/**
	 * Identical to <i>Connected</i> except the final indexed vertex is then connected back to the initial vertex to
	 * form a loop.
	 */
	Points(false);

	private final boolean hasPolygons;

	private IndexMode(final boolean hasPolygons) {
		this.hasPolygons = hasPolygons;
	}

	public boolean hasPolygons() {
		return hasPolygons;
	}

	public int getVertexCount() {
		switch (this) {
			case Triangles:
			case TriangleStrip:
			case TriangleFan:
				return 3;
			case Quads:
			case QuadStrip:
				return 4;
			case Lines:
			case LineStrip:
			case LineLoop:
				return 2;
			case Points:
				return 1;
		}
		throw new IllegalArgumentException("Unhandled type: " + this);
	}
}