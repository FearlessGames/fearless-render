package se.fearlessgames.fear;

import se.fearlessgames.fear.light.DefaultAmbientPointLight;
import se.fearlessgames.fear.light.PointLight;
import se.fearlessgames.fear.light.SpotLight;
import se.fearlessgames.fear.texture.Texture;
import se.fearlessgames.fear.vbo.VertexBufferObject;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	private final VertexBufferObject vbo;
	private final ShaderProgram shaderProgram;

	private Texture texture;
	private PointLight pointLight = new DefaultAmbientPointLight();
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

	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
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
