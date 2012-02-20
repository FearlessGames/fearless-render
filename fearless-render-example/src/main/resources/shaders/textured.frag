#version 140
#define MAX_SPOT_LIGHTS 10

uniform sampler2D texture0;

uniform struct SpotLight {
	vec3 location;
	vec3 lightingColor;
	vec3 direction;
	float exponent;
	float spotCosCutoff;
	float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
} spotLights[MAX_SPOT_LIGHTS];

uniform int nrOfSpotLights;

in vec2 texCoord0;
in vec3 transformedNormal;
in vec3 omniLightColor;


struct SpotLightDistances {
	float distance;
	vec3 halfVector;
	vec3 lightDir;
};

in SpotLightDistances spotLightDistances;


out vec4 fragColor;



void main(){

	vec4 textureColor =  texture(texture0, texCoord0.xy);
    vec4 color = vec4(omniLightColor,0.0);//vec4(textureColor.rgb * omniLightColor, textureColor.a);


	for (int i = 0; i < nrOfSpotLights; i++) {
		//should use spotLightDistances[i], but problems with arrays or structs
		float NdotL = max(dot(transformedNormal, normalize(spotLightDistances.lightDir)), 0.0);

		if (NdotL > 0.0) {

			float spotEffect = dot(normalize(spotLights[i].direction), normalize(-spotLightDistances.lightDir));

			if (spotEffect > spotLights[i].spotCosCutoff) {
				spotEffect = pow(spotEffect, spotLights[i].exponent);
				float att = spotEffect / (spotLights[i].constantAttenuation +
						spotLights[i].linearAttenuation * spotLightDistances.distance +
						spotLights[i].quadraticAttenuation * spotLightDistances.distance * spotLightDistances.distance);

				color += att * (vec4(spotLights[i].lightingColor,0.0) * NdotL);

				/*
				//not handling any shininess and specular right now...
				halfV = normalize(halfVector);
				NdotHV = max(dot(n,halfV),0.0);
				color += att * gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
				*/
			}
		}

	}


    fragColor = vec4(textureColor.rgb * color.rgb, textureColor.a);;

}