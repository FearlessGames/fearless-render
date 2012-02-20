package se.fearlessgames.fear;

import java.util.Comparator;

public class Sorters {
    private static final int DEPTH_ROW = 2;
    private static final int DEPTH_COL = 3;

    private static int compareDepth(Renderer.AddedMesh o1, Renderer.AddedMesh o2) {
        double o1Depth = o1.transform.getValue(DEPTH_ROW, DEPTH_COL);
        double o2Depth = o2.transform.getValue(DEPTH_ROW, DEPTH_COL);
        return Double.compare(o1Depth, o2Depth);
    }

    public static final Comparator<Renderer.AddedMesh> BACK_TO_FRONT = new Comparator<Renderer.AddedMesh>() {
        @Override
        public int compare(Renderer.AddedMesh o1, Renderer.AddedMesh o2) {
            return compareDepth(o1, o2);
        }
    };

    public static final Comparator<Renderer.AddedMesh> FRONT_TO_BACK = new Comparator<Renderer.AddedMesh>() {
        @Override
        public int compare(Renderer.AddedMesh o1, Renderer.AddedMesh o2) {
            return -compareDepth(o1, o2);
        }
    };


}
