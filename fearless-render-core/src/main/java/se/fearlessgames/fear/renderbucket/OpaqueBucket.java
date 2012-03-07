package se.fearlessgames.fear.renderbucket;

import se.fearlessgames.fear.Sorters;
import se.fearlessgames.fear.TransformedMesh;

import java.util.Collections;
import java.util.List;

public class OpaqueBucket extends SortableBucket {

	@Override
	protected void sortMeshes(List<TransformedMesh> meshes) {
		Collections.sort(meshes, Sorters.FRONT_TO_BACK);
		Sorters.sortbyMeshType(meshes);
	}

}
