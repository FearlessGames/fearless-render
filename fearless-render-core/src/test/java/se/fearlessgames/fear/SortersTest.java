package se.fearlessgames.fear;

import junit.framework.TestCase;
import se.fearlessgames.fear.mesh.Mesh;
import se.fearlessgames.fear.mesh.MeshType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortersTest extends TestCase {
    public void testByMeshType() throws Exception {
        List<TransformedMesh> list = new ArrayList<TransformedMesh>();
        MeshType meshType1 = new MeshType(null, null);
        TransformedMesh mesh1 = new TransformedMesh(new Mesh(null, meshType1), null);
        TransformedMesh mesh2 = new TransformedMesh(new Mesh(null, new MeshType(null, null)), null);
        TransformedMesh mesh3 = new TransformedMesh(new Mesh(null, new MeshType(null, null)), null);
        TransformedMesh mesh4 = new TransformedMesh(new Mesh(null, meshType1), null);
        list.add(mesh1);
        list.add(mesh2);
        list.add(mesh3);
        list.add(mesh4);
        Sorters.sortbyMeshType(list);
        assertEquals(Arrays.asList(mesh1, mesh4, mesh2, mesh3), list);
    }

    public void testByMeshType2() throws Exception {
        List<TransformedMesh> list = new ArrayList<TransformedMesh>();
        MeshType meshType1 = new MeshType(null, null);
        TransformedMesh mesh1 = new TransformedMesh(new Mesh(null, meshType1), null);
        MeshType meshType2 = new MeshType(null, null);
        TransformedMesh mesh2 = new TransformedMesh(new Mesh(null, meshType2), null);
        TransformedMesh mesh3 = new TransformedMesh(new Mesh(null, new MeshType(null, null)), null);
        TransformedMesh mesh4 = new TransformedMesh(new Mesh(null, meshType1), null);
        TransformedMesh mesh5 = new TransformedMesh(new Mesh(null, meshType2), null);
        list.add(mesh1);
        list.add(mesh2);
        list.add(mesh3);
        list.add(mesh4);
        list.add(mesh5);
        Sorters.sortbyMeshType(list);
        assertEquals(Arrays.asList(mesh1, mesh4, mesh2, mesh5, mesh3), list);
    }

    public void testByMeshType3() throws Exception {
        MeshType meshType1 = new MeshType(null, null);
        MeshType meshType2 = new MeshType(null, null);
        for (int N = 0; N < 100; N++) {
            List<TransformedMesh> list = new ArrayList<TransformedMesh>();
            for (int i = 0; i < N; i++) {
                list.add(new TransformedMesh(new Mesh(null, meshType1), null));
                list.add(new TransformedMesh(new Mesh(null, meshType2), null));
            }
            Sorters.sortbyMeshType(list);
            for (int i = 0; i < N; i++) {
                assertEquals(i + " of " + N, meshType1, list.get(i).mesh.getMeshType());
                assertEquals(i + " of " + N, meshType2, list.get(N + i).mesh.getMeshType());
            }
        }
    }
}
