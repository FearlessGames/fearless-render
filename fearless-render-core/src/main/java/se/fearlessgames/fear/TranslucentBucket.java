package se.fearlessgames.fear;

import se.fearlessgames.fear.renderbucket.SortableBucket;

import java.util.Collections;
import java.util.List;

public class TranslucentBucket extends SortableBucket {
	@Override
	protected void sortMeshes(List<TransformedMesh> meshes) {
		Collections.sort(meshes, Sorters.BACK_TO_FRONT);
	}
}
