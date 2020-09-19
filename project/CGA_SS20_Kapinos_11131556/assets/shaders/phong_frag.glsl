#version 330 core

//input from vertex shader

in struct VertexData
{   vec2 textur;
    vec3 normal;
    vec3 toPointLight;
    vec3 toSpotLight;
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

//SpotLight
uniform vec3 cameraSpotLightColor, cameraSpotDirection, cameraSpotAttenuation;
uniform float cameraSpotLightPhiOuter, cameraSpotLightGammaInner;

//output

out vec4 color;


//components############################################################################################################

vec4 emissiveC = vec4(texture(emit, vertexData.textur).rgb * emissive, 1.0);

//ambient hinzu
vec4 ambientC = vec4(texture(diff, vertexData.textur).rgb * vec3(0.02f, 0.02f, 0.2f),1.0f);

vec4 diffSpecC (vec3 toLight){

    //diffuse component
    vec4 texDiff = vec4(texture(diff, vertexData.textur).rgb, 1.0);
    vec3 N = normalize(vertexData.normal);
    vec3 NTL = normalize(toLight);
    float cosAlpha = max(0.0, dot(N, NTL));
    vec4 diffuseComponent = texDiff * cosAlpha;

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

float intensity (vec3 toLight){
    vec3 NCSD = normalize(cameraSpotDirection);
    vec3 toSL = normalize(toLight);
    float cosTheta = max(0.0f, dot(NCSD, -toSL));
    float I = clamp(   ( cosTheta-cos(cameraSpotLightPhiOuter) )  /  ( cos(cameraSpotLightGammaInner)-cos(cameraSpotLightPhiOuter) ), 0.0f, 1.0f    );
    return I;
}

void main() {

    //Attenuation zu stark für universe
//    vec4 colorPoint = diffSpecC(vertexData.toPointLight) *
//                      vec4(sunPointLightColor * attenuation(vertexData.toPointLight, sunPointAttenuation), 1.0);

    vec4 colorPoint = diffSpecC(vertexData.toPointLight) *
                      vec4(sunPointLightColor, 1.0f);

    //vec4 colorSpot =  diffSpecC(vertexData.toSpotLight) *
    //                  vec4(cameraSpotLightColor * attenuation(vertexData.toSpotLight, cameraSpotAttenuation) *
    //                  intensity (vertexData.toSpotLight), 1.0);


    color = emissiveC + colorPoint + ambientC;
}
