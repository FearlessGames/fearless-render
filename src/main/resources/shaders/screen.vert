uniform mat4 projection;
uniform mat4 translation;
varying vec4 vertColor;

void main(){
    gl_Position= projection*translation*gl_Vertex;
    vertColor = vec4(gl_Normal.xyz, 1.0);
}