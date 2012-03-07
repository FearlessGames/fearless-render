package se.fearlessgames.fear;

import se.fearlessgames.fear.mesh.MeshType;

import java.util.*;

public class Sorters {
    private static final int DEPTH_ROW = 2;
    private static final int DEPTH_COL = 3;

    /**
     * Stable sort of mesh types. This will effectively group meshes by meshtype
     * @param list list of meshes
     */
    public static void sortbyMeshType(List<TransformedMesh> list) {
        final Map<MeshType, Integer> ordering = new IdentityHashMap<MeshType, Integer>();
        int counter = 0;
        for (TransformedMesh addedMesh : list) {
            MeshType type = addedMesh.mesh.getMeshType();
            Integer order = ordering.get(type);
            if (order == null) {
                order = ++counter;
                ordering.put(type, order);
            }
        }
        Collections.sort(list, new Comparator<TransformedMesh>() {
            @Override
            public int compare(TransformedMesh o1, TransformedMesh o2) {
                MeshType meshType1 = o1.mesh.getMeshType();
                MeshType meshType2 = o2.mesh.getMeshType();
                if (meshType1 == meshType2) {
                    return 0;
                }
                Integer order1 = ordering.get(meshType1);
                Integer order2 = ordering.get(meshType2);
                return order1 - order2;
            }
        });
    }

    private static int compareDepth(TransformedMesh o1, TransformedMesh o2) {
        double o1Depth = o1.transform.getValue(DEPTH_ROW, DEPTH_COL);
        double o2Depth = o2.transform.getValue(DEPTH_ROW, DEPTH_COL);
        return Double.compare(o1Depth, o2Depth);
    }

    public static final Comparator<TransformedMesh> BACK_TO_FRONT = new Comparator<TransformedMesh>() {
        @Override
        public int compare(TransformedMesh o1, TransformedMesh o2) {
            return compareDepth(o1, o2);
        }
    };

    public static final Comparator<TransformedMesh> FRONT_TO_BACK = new Comparator<TransformedMesh>() {
        @Override
        public int compare(TransformedMesh o1, TransformedMesh o2) {
            return -compareDepth(o1, o2);
        }
    };


}
