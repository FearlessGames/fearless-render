#version 140

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec2 texCoord0;
out vec3 lightWeighting;

void main(){

	//port from http://learningwebgl.com/lessons/lesson12/index.html
	vec3 pointLightingLocation = vec3(60.0, 60.0, 0.0);
	vec3 pointLightingColor = vec3(0.8, 0.8, 0.8);
	vec3 ambientColor = vec3(0.2, 0.2, 0.2);

	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

	vec3 lightDirection = normalize(pointLightingLocation - modelViewPosition.xyz);
	vec3 transformedNormal = normalMatrix * normal;

	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);

	lightWeighting = (ambientColor + pointLightingColor) * directionalLightWeighting;


	texCoord0 = textureCoord;

}