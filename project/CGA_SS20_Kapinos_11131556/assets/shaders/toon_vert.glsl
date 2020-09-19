
#version 330 core

//input
layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;
layout(location = 2) in vec3 norm;

//uniforms
//translation object to world
uniform mat4 model_matrix, view_matrix, projection_matrix;
uniform vec2 tcMultiplier;
uniform vec3 sunPointLightPosition;

//output
out struct VertexData
{
    vec2 textur;
    vec3 normal;
    vec3 toPointLight;
    vec3 toCamera;

} vertexData;


void main() {

    //modelView-Matrix
    mat4 modelView = view_matrix * model_matrix;

    //out textur
    vertexData.textur = tex * tcMultiplier;

    //out vertexPosition
    vec4 positionVex = projection_matrix * modelView * vec4(pos, 1.0f);
    gl_Position = positionVex;

    //out norm
    vertexData.normal = ((transpose(inverse(modelView))) * vec4(norm, 0.0f)).xyz;

    //out toPointLight und toSpotLight
    //Lights und vertex in viewspace bringen (* viewMatrix)
    vec4 lp = view_matrix * vec4(sunPointLightPosition, 1.0);
    vec4 P = modelView * vec4(pos, 1.0f);
    //vertexpositon abziehen, da camera im Ursprung
    vertexData.toPointLight = (lp - P).xyz;

    //out toCamera
    //P abziehen, da camera im Ursrpung
    vertexData.toCamera = (-P).xyz;

}
