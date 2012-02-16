#version 140

#define MAX_SPOT_LIGHTS 10

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

uniform struct PointLight {
	vec3 location;
	vec3 lightingColor;
	vec3 ambientColor;
} pointLight;


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

in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec2 texCoord0;
out vec3 lightWeighting;

vec3 calcPointLightningWeight(vec4 modelViewPosition, vec3 transformedNormal);
vec3 calcSpotLight(int i, vec4 modelViewPosition, vec3 transformedNormal);

void main() {
    vec3 transformedNormal = normalize(normalMatrix * normal);
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);
	gl_Position = projectionMatrix * modelViewPosition;

	lightWeighting = calcPointLightningWeight(modelViewPosition, transformedNormal);

	for (int i = 0; i  <= nrOfSpotLights; i++) {
		lightWeighting += calcSpotLight(i, modelViewPosition, transformedNormal);

	}



	texCoord0 = textureCoord;
}


vec3 calcPointLightningWeight(vec4 modelViewPosition, vec3 transformedNormal) {
	vec3 lightDirection = normalize(pointLight.location - modelViewPosition.xyz);

	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);
	return pointLight.ambientColor + (pointLight.lightingColor * directionalLightWeighting);
}

vec3 calcSpotLight(int i, vec4 modelViewPosition, vec3 transformedNormal ) {
	//source:

	vec3 ecPosition3 = (vec3 (modelViewPosition)) / modelViewPosition.w;


    float nDotVP;			// normal . light direction
	float nDotHV;			// normal . light half vector
	float pf;				// power factor
	float spotDot;		   // cosine of angle between spotlight
	float spotAttenuation;   // spotlight attenuation factor
	float attenuation;	   // computed attenuation factor
	float d;				 // distance from surface to light source
	vec3  VP;				// direction from surface to light position


	// Compute vector from surface to light position
    VP = vec3 (spotLights[i].location) - ecPosition3;

    // Compute distance between surface and light position
    d = length(VP);

    // Normalize the vector from surface to light position
	VP = normalize(VP);

	//Attenuation = Constant + Linear * Distance + Quadratic * Distance ^ 2
	//Luminosity = 1 / Attenuation

    attenuation = 1.0 / (
			spotLights[i].constantAttenuation +
			spotLights[i].linearAttenuation * d +
			spotLights[i].quadraticAttenuation * d * d
   		);

    // See if point on surface is inside cone of illumination
	spotDot = dot(-VP, normalize(spotLights[i].direction));

    if (spotDot < spotLights[i].spotCosCutoff) {
    	spotAttenuation = 0.0; // light adds no contribution
    } else {
		spotAttenuation = pow(spotDot, spotLights[i].exponent);
	}

     // Combine the spotlight and distance attenuation.
	attenuation *= spotAttenuation;



    nDotVP = max(0.0, dot(normal, VP));

    return spotLights[i].lightingColor * nDotVP * attenuation;

}