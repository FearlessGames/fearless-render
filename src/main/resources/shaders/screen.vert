uniform mat4x4 translation;
varying vec4 vertColor;

void main(){
    gl_Position= gl_ModelViewProjectionMatrix*translation*gl_Vertex;
    vertColor = vec4(0.6 , 0.3 , (gl_Position.z - 10) / 10.0, 1.0);
}