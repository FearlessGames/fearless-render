#version 140
#define MAX_SPOT_LIGHTS 10

uniform float globalAlpha = 1.0;
uniform sampler2D texture0;

in vec2 texCoord0;
in vec3 transformedNormal;
in vec4 directionalLightColor;

out vec4 fragColor;

void main(){

	vec4 textureColor =  texture(texture0, texCoord0.xy);
    vec4 color = directionalLightColor;




    fragColor = vec4(textureColor.rgb * color.rgb, globalAlpha);;
}