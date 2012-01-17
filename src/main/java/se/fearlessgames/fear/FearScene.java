package se.fearlessgames.fear;

public class FearScene {
	public void render(FearOutput output) {
		clear(output);
		renderSkybox(output);
		renderOpaqueObjects(output);
		renderTransparentObjects(output);
	}

	private void renderTransparentObjects(FearOutput output) {
	}

	private void renderOpaqueObjects(FearOutput output) {
	}

	private void renderSkybox(FearOutput output) {
	}

	private void clear(FearOutput output) {
	}
}
