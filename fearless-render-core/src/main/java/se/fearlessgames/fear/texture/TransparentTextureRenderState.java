package se.fearlessgames.fear.texture;

import se.fearlessgames.fear.ShaderProgram;
import se.fearlessgames.fear.gl.BlendFunction;
import se.fearlessgames.fear.gl.Capability;
import se.fearlessgames.fear.gl.FearGl;
import se.fearlessgames.fear.gl.TextureType;
import se.fearlessgames.fear.mesh.RenderState;

public class TransparentTextureRenderState implements RenderState {
	private final Texture texture;

	public TransparentTextureRenderState(Texture texture) {
		this.texture = texture;
	}

	@Override
	public void enable(FearGl fearGl, ShaderProgram shaderProgram) {
		fearGl.glEnable(Capability.GL_TEXTURE_2D);

		fearGl.glEnable(Capability.GL_BLEND);
		fearGl.glBlendFunc(BlendFunction.SRC_ALPHA, BlendFunction.ONE_MINUS_SRC_ALPHA);

		fearGl.glBindTexture(TextureType.TEXTURE_2D, texture.getId());
	}

	@Override
	public void disable(FearGl fearGl, ShaderProgram shaderProgram) {
		fearGl.glDisable(Capability.GL_BLEND);
	}
}
