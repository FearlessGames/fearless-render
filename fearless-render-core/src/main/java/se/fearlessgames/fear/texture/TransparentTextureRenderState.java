package se.fearlessgames.fear.texture;

import se.fearlessgames.fear.gl.*;
import se.fearlessgames.fear.mesh.RenderState;
import se.fearlessgames.fear.shader.ShaderProgram;

public class TransparentTextureRenderState implements RenderState {
	private final Texture texture;

	public TransparentTextureRenderState(Texture texture) {
		this.texture = texture;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		shaderProgram.uniform("globalAlpha").setFloat(0.4f);
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
		fearGl.glDepthMask(false);
		fearGl.glEnable(Capability.GL_BLEND);
		fearGl.glBlendFunc(BlendFunction.SRC_ALPHA, BlendFunction.ONE_MINUS_SRC_ALPHA);
		fearGl.glBindTexture(TextureType.TEXTURE_2D, texture.getId());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {
		fearGl.glDepthFunc(DepthFunction.GL_LEQUAL);
		fearGl.glDepthMask(true);
		fearGl.glDisable(Capability.GL_BLEND);
		fearGl.glBlendFunc(BlendFunction.ONE, BlendFunction.ZERO);
	}
}
