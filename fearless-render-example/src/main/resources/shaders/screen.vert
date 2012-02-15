#version 140

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec4 vertColor;

void main(){
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

    vertColor = vec4(normal.xyz, 1.0);
}