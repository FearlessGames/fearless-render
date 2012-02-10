#version 140

uniform mat4 projection;
uniform mat4 translation;

in vec4 vertex;
in vec4 normal;
in vec4 color;

varying vec4 vertColor;


void main(){
    gl_Position = projection*translation*vertex;
    //vertColor = color;
    vertColor = vec4(normal.xyz, 1.0);
}