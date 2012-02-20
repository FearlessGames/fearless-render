package se.fearlessgames.fear;

import se.fearlessgames.fear.light.DefaultAmbientOmniLight;
import se.fearlessgames.fear.light.OmniLight;
import se.fearlessgames.fear.light.SpotLight;
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	private final VertexBufferObject vbo;
	private final ShaderProgram shaderProgram;

	private Texture texture;
	private OmniLight omniLight = new DefaultAmbientOmniLight();
	private List<SpotLight> spotLights = new ArrayList<SpotLight>();

	public Mesh(VertexBufferObject vbo, ShaderProgram shaderProgram) {
		this.vbo = vbo;
		this.shaderProgram = shaderProgram;
	}

	public boolean isOpaque() {
		return true;
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public OmniLight getOmniLight() {
		return omniLight;
	}

	public void setOmniLight(OmniLight omniLight) {
		this.omniLight = omniLight;
	}

	public boolean hasTexture() {
		return texture != null;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public List<SpotLight> getSpotLights() {
		return spotLights;
	}
}
