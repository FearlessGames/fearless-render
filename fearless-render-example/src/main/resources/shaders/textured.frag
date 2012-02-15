#version 140

uniform sampler2D colorMap;

in vec2 texCoord0;
in vec3 lightWeighting;

out vec4 fragColor;

void main(){

	vec4 textureColor =  texture(colorMap, texCoord0.xy);

    fragColor = vec4(textureColor.rgb * lightWeighting, textureColor.a);
    //fragColor = textureColor;
}