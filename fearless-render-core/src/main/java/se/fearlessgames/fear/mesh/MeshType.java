package se.fearlessgames.fear.mesh;

import se.fearlessgames.fear.RenderBucket;
import se.fearlessgames.fear.ShaderProgram;

import java.util.ArrayList;
import java.util.Collection;

public class MeshType {
    private final ShaderProgram shaderProgram;
    private final Collection<RenderState> renderStates = new ArrayList<RenderState>();
    private final RenderBucket bucket;

    public MeshType(ShaderProgram shaderProgram, RenderBucket bucket, RenderState... renderStates) {
        this.shaderProgram = shaderProgram;
        this.bucket = bucket;
        for (RenderState renderState : renderStates) {
            this.renderStates.add(renderState);
        }
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Collection<RenderState> getRenderStates() {
        return renderStates;
    }

    public RenderBucket getBucket() {
        return bucket;
    }
}
