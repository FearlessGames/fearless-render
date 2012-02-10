#version 140

uniform mat4 projection;
uniform mat4 translation;
varying vec4 vertColor;
out vec4 texCoord0;

void main(){
	texCoord0 = gl_MultiTexCoord0;
    gl_Position= projection*translation*gl_Vertex;

}