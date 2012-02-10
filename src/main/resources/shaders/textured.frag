#version 140

uniform sampler2D colorMap;
in vec4 texCoord0;
out vec4 fragColor;

void main(){
    fragColor = texture(colorMap, texCoord0.xy);
}