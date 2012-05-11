#version 140

uniform mat4 modelViewProjectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 modelMatrix;
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
	mat4 modelView = mat4(1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, -9,
				0, 0, 0, 1);
	mat4 proj = mat4(	1.67, 0, 0, 0,
						0, 	1.67, 0, 0,
						0, 0, -1.08, -2.08,
						0, 0, -1, 0);
	//vec4 modelViewPosition = modelViewProjectionMatrix * vec4(vertex, 1.0);
	//vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	vec4 modelViewPosition = modelView * vec4(vertex, 1.0);
	//vec4 modelViewPosition = modelView * proj * vec4(vertex, 1.0);

	gl_Position = modelViewPosition;

	transformedNormal = normalize(normalMatrix * normal);

    directionalLightColor = calcOmniLightColor(modelViewPosition, transformedNormal);

	texCoord0 = textureCoord;
}

vec4 calcOmniLightColor(vec4 modelViewPosition, vec3 transformedNormal) {
	vec3 lightDirection = normalize(directionalLight.location - modelViewPosition.xyz);

	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);
	return directionalLight.ambient + (directionalLight.diffuse * directionalLightWeighting);
}
