#version 140

uniform sampler2D colorMap;

void main(){
    gl_FragColor = texture(colorMap, gl_TexCoord[0].st);
}