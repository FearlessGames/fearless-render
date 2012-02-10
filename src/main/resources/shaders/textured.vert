#version 140

uniform mat4 projection;
uniform mat4 translation;
varying vec4 vertColor;

void main(){
	gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position= projection*translation*gl_Vertex;

}