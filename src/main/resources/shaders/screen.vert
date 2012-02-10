#version 140

uniform mat4 projection;
uniform mat4 translation;
in vec4 vertex;
varying vec4 vertColor;


void main(){
    gl_Position = projection*translation*vertex;
    vertColor = vec4(gl_Normal.xyz, 1.0);
}