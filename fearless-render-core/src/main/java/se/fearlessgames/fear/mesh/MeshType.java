package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.RenderBucket;
import se.fearlessgames.fear.ShaderProgram;

import java.util.ArrayList;
import java.util.Collection;

public class MeshType {
    private final ShaderProgram shaderProgram;
    private final Collection<RenderState> renderStates = new ArrayList<RenderState>();
    private final RenderBucket bucket;

    public MeshType(ShaderProgram shaderProgram, RenderBucket bucket) {
        this.shaderProgram = shaderProgram;
        this.bucket = bucket;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Collection<RenderState> getRenderStates() {
        return renderStates;
    }

    public void addRenderState(RenderState renderState) {
        renderStates.add(renderState);
    }

    public RenderBucket getBucket() {
        return bucket;
    }
}
