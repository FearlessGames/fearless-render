#version 150

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

in vec3 vertex;
out vec4 vertColor;

void main() {
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

    vertColor = vec4(vertex.xyz, 1.0);
}