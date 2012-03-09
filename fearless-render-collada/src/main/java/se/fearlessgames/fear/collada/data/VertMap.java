package se.fearlessgames.fear.collada.data;

import se.fearlessgames.fear.mesh.MeshData;

import java.util.Map;

public class VertMap {

	private int[] lookupTable;

	public VertMap(final MeshData meshData) {
		setupTable(meshData);
	}

	private void setupTable(final MeshData meshData) {
		lookupTable = new int[meshData.getVertexCount()];
		for (int x = 0; x < lookupTable.length; x++) {
			lookupTable[x] = x;
		}
	}

	public int getNewIndex(final int oldIndex) {
		return lookupTable[oldIndex];
	}

	public int getFirstOldIndex(final int newIndex) {
		for (int i = 0; i < lookupTable.length; i++) {
			if (lookupTable[i] == newIndex) {
				return i;
			}
		}
		return -1;
	}

	public void applyRemapping(final Map<Integer, Integer> indexRemap) {
		for (int i = 0; i < lookupTable.length; i++) {
			if (indexRemap.containsKey(lookupTable[i])) {
				lookupTable[i] = indexRemap.get(lookupTable[i]);
			}
		}
	}

	public int[] getLookupTable() {
		return lookupTable;
	}
}
