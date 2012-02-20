package se.fearlessgames.fear;

import se.fearlessgames.fear.gl.DepthFunction;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.mesh.MeshRenderer;

import java.util.Collections;
import java.util.List;

public enum RenderBucket {
    OPAQUE {
        @Override
        public void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer) {
            Collections.sort(meshes, Sorters.FRONT_TO_BACK);
            FearGl fearGL = meshRenderer.getGL();
            fearGL.glDepthFunc(DepthFunction.GL_LEQUAL);
            fearGL.glBlendFunc(null, null); // TODO: fix blendmodes
            for (Renderer.AddedMesh addedMesh : meshes) {
                meshRenderer.render(addedMesh.mesh, addedMesh.transform);
            }
        }
    },
    TRANSPARENT {
        @Override
        public void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer) {
            Collections.sort(meshes, Sorters.BACK_TO_FRONT);
            FearGl fearGL = meshRenderer.getGL();
            fearGL.glDepthFunc(DepthFunction.GL_NEVER);
            fearGL.glBlendFunc(null, null); // TODO: fix blendmodes
            for (Renderer.AddedMesh addedMesh : meshes) {
                meshRenderer.render(addedMesh.mesh, addedMesh.transform);
            }
        }
    };

    public abstract void render(List<Renderer.AddedMesh> meshes, MeshRenderer meshRenderer);
}
