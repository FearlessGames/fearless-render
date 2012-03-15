#version 120

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

attribute vec3 vertex;
varying vec4 vertColor;

void main(){
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

    vertColor = vec4(vertex.xyz, 1.0);
}