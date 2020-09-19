#version 330 core

//input from vertex shader

in struct VertexData
{   vec2 textur;
    vec3 normal;
    vec3 toPointLight;
    vec3 toCamera;
} vertexData;

//uniforms

//Material
uniform sampler2D emit, diff, spec;
uniform float shininess;

//emissiveComponent
uniform vec3 emissive;


//PointLight
uniform vec3 sunPointLightColor, sunPointAttenuation;


//output

out vec4 color;

//components############################################################################################################

vec4 emissiveC = vec4(texture(emit, vertexData.textur).rgb * emissive, 1.0);

//ambientLight hinzugefügt
vec4 ambientC = vec4(texture(diff, vertexData.textur).rgb * vec3(0.05f, 0.05f, 0.4f),1.0f);

vec4 diffSpecC (vec3 toLight){

    //diffuse component
    vec4 texDiff = vec4(texture(diff, vertexData.textur).rgb, 1.0);
    vec3 N = normalize(vertexData.normal);
    vec3 NTL = normalize(toLight);
    float cosAlpha = max(0.0, dot(N, NTL));
    vec4 diffuseC = texDiff * cosAlpha;

    //toon-Effekt
    //*****************************
    vec4 diffuseComponent;
    if (cosAlpha > 0.95)
    diffuseComponent = vec4(1.0,1.0,1.0,1.0) * diffuseC;
    else if (cosAlpha > 0.72)
    diffuseComponent = vec4(0.85, 0.85, 0.85, 1.0) * diffuseC;
    else if (cosAlpha > 0.5)
    diffuseComponent = vec4(0.7,0.7,0.7,1.0) * diffuseC;
    else if (cosAlpha > 0.05)
    diffuseComponent = vec4(0.35,0.35,0.35,1.0) * diffuseC;
    else
    diffuseComponent = vec4(0.0,0.0,0.0,1.0) * diffuseC;
    //*********************************************


    //specular component
    vec4 texSpec = vec4(texture(spec, vertexData.textur).rgb, 1.0);
    vec3 NRTL = reflect(-NTL, N);
    vec3 NTC = normalize(vertexData.toCamera);
    float cosBeta = max(0.0, dot(NRTL,NTC));
    float cosBetaK = pow(cosBeta,shininess);
    vec4 specularComponent = texSpec * cosBetaK;

    //Summe
    vec4 diffSpec = diffuseComponent + specularComponent;
    return diffSpec;
}

float attenuation(vec3 toLight, vec3 attenuation){
    float d = length(toLight);
    float fAttPoint = 1.0 / (attenuation.x + (attenuation.y * d) + (attenuation.z * d * d));
    return fAttPoint;
}


void main() {

    vec4 colorPoint = diffSpecC(vertexData.toPointLight) * vec4(sunPointLightColor, 1.0f);
    color = emissiveC + colorPoint + ambientC;
}



