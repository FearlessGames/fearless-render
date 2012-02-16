#version 140

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

uniform vec3 pointLightingLocation;
uniform vec3 pointLightingColor;
uniform vec3 pointLightningAmbientColor;


in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec2 texCoord0;
out vec3 lightWeighting;

vec3 calcPointLightningWeight(vec4 modelViewPosition);

void main(){

	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

	lightWeighting = calcPointLightningWeight(modelViewPosition);

	texCoord0 = textureCoord;
}


vec3 calcPointLightningWeight(vec4 modelViewPosition) {
	vec3 lightDirection = normalize(pointLightingLocation - modelViewPosition.xyz);
	vec3 transformedNormal = normalMatrix * normal;
	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);
	return pointLightningAmbientColor + (pointLightingColor * directionalLightWeighting);
}
