#version 140

#define MAX_SPOT_LIGHTS 10

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

uniform struct OmniLight {
	vec3 location;
	vec3 lightingColor;
	vec3 ambientColor;
} omniLight;


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

uniform int nrOfSpotLights = 0;

in vec3 vertex;
in vec3 normal;
in vec4 color;
in vec2 textureCoord;

out vec2 texCoord0;
out vec3 transformedNormal;
out vec3 omniLightColor;

struct SpotLightDistances {
	float distance;
	vec3 halfVector;
	vec3 lightDir;
};

out SpotLightDistances spotLightDistances;

vec3 calcOmniLightColor(vec4 modelViewPosition, vec3 transformedNormal);

void main() {
	vec4 modelViewPosition = modelViewMatrix * vec4(vertex, 1.0);

	gl_Position = projectionMatrix * modelViewPosition;

	transformedNormal = normalize(normalMatrix * normal);

    omniLightColor = calcOmniLightColor(modelViewPosition, transformedNormal);



	for (int i = 0; i < nrOfSpotLights; i++) {
		vec3 aux = vec3( vec4(spotLights[i].location, 1.0) - modelViewPosition);
		//glsl seems to have problem with arrays of struct, need to solve it some other way
		//spotLightDistances[i].lightDir = normalize(aux);
		//spotLightDistances[i].distance = length(aux);
		//spotLightDistances[i].halfVector = normalize(normalize(spotLights[i].location.xyz) + vec3(0, 0, 1));

		spotLightDistances.lightDir = normalize(aux);
		spotLightDistances.distance = length(aux);
		spotLightDistances.halfVector = normalize(normalize(spotLights[i].location.xyz) + vec3(0, 0, 1));
	}


	texCoord0 = textureCoord;
}

vec3 calcOmniLightColor(vec4 modelViewPosition, vec3 transformedNormal) {
	vec3 lightDirection = normalize(omniLight.location - modelViewPosition.xyz);

	float directionalLightWeighting = max(dot(transformedNormal, lightDirection), 0.0);
	return omniLight.ambientColor + (omniLight.lightingColor * directionalLightWeighting);
}
