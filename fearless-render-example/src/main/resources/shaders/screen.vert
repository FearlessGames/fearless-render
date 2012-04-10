#version 140

uniform mat4 modelMatrix;

in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec4 vertColor;

void main(){
	gl_Position  = modelMatrix * vec4(vertex, 1.0);
    vertColor = vec4(normal.xyz, 1.0);
}