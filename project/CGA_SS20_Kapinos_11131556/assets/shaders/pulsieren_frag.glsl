#version 330 core

//input from geometry shader
in vec2 texturGeo;
in vec3 normalGeo;
in vec3 toPointLightGeo;
in vec3 toCameraGeo;

//uniforms
uniform sampler2D emit, diff, spec;
uniform float shininess;
uniform vec3 emissive;
uniform vec3 sunPointLightColor, sunPointAttenuation;

//output
out vec4 color;


//components###########################################################

vec4 emissiveC = vec4(texture(emit, texturGeo).rgb * emissive, 1.0);

vec4 diffSpecC (vec3 toLight){

    //diffuse component
    vec4 texDiff = vec4(texture(diff, texturGeo).rgb, 1.0);
    vec3 N = normalize(normalGeo);
    vec3 NTL = normalize(toLight);
    float cosAlpha = max(0.0, dot(N, NTL));
    vec4 diffuseComponent = texDiff * cosAlpha;

    //specular component
    vec4 texSpec = vec4(texture(spec, texturGeo).rgb, 1.0);
    vec3 NRTL = reflect(-NTL, N);
    vec3 NTC = normalize(toCameraGeo);
    float cosBeta = max(0.0, dot(NRTL,NTC));
    float cosBetaK = pow(cosBeta,shininess);
    vec4 specularComponent = texSpec * cosBetaK;

    //Summe
    vec4 diffSpec = diffuseComponent + specularComponent;
    return diffSpec;
}

void main(){

    vec4 colorPoint = diffSpecC(toPointLightGeo) * vec4(sunPointLightColor, 1.0f);
    color = emissiveC + colorPoint + vec4(texture(diff, texturGeo).rgb, 1.0);

}
