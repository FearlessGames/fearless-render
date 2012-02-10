#version 140

uniform mat4 projection;
uniform mat4 translation;
varying vec4 vertColor;
in vec4 vertex;
in vec2 textureCoord;
out vec2 texCoord0;

void main(){
	texCoord0 = textureCoord;
    gl_Position= projection*translation*vertex;

}