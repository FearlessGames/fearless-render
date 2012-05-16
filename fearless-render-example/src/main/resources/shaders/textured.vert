#version 140

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 modelViewProjectionMatrix;
uniform mat3 normalMatrix;

uniform struct DirectionalLight {
	vec3 location;
	vec4 specular;
	vec4 diffuse;
	vec4 ambient;
} directionalLight;


in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec2 texCoord0;
out vec3 transformedNormal;
out vec4 directionalLightColor;


vec4 calcOmniLightColor(vec4 modelViewPosition, vec3 transformedNormal);

void main() {
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
//	vec4 modelViewPosition = modelViewProjectionMatrix * vec4(vertex, 1.0);

	gl_Position = projectionMatrix * modelViewPosition;
//	gl_Position = modelViewPosition;

	transformedNormal = normalize(normalMatrix * normal);

    directionalLightColor = calcOmniLightColor(modelViewPosition, transformedNormal);

	texCoord0 = textureCoord;
}

vec4 calcOmniLightColor(vec4 modelViewPosition, vec3 transformedNormal) {
	vec3 lightDirection = normalize(directionalLight.location - modelViewPosition.xyz);

	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);
	return directionalLight.ambient + (directionalLight.diffuse * directionalLightWeighting);
}
