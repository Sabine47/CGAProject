
#version 330 core

//input
layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;
layout(location = 2) in vec3 norm;

//uniforms
//translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
                //uniform mat4 projection_matrix;
uniform vec2 tcMultiplier;
uniform vec3 sunPointLightPosition;


//output
out  vec2 texturVex;
//out  vec3 normalVex;
out  vec3 toPointLightVex;
out  vec3 toCameraVex;


void main() {

    //modelView-Matrix
        mat4 modelView = view_matrix * model_matrix;

    //out textur
        texturVex = tex * tcMultiplier;

    //out vertexPosition
        //vec4 positionVex = projection_matrix * modelView * vec4(pos, 1.0f);
        vec4 positionVex = modelView * vec4(pos, 1.0f);
        gl_Position = positionVex;

    //out norm
         //normalVex = ((transpose(inverse(modelView))) * vec4(norm, 0.0f)).xyz;

    //out toPointLight
        //Lights und vertex in viewspace bringen (* viewMatrix)
        vec4 lp = view_matrix * vec4(sunPointLightPosition, 1.0);
        vec4 P = modelView * vec4(pos, 1.0f);
        //vertexpositon abziehen, da camera im Ursprung
        toPointLightVex = (lp - P).xyz;

    //out toCamera
        //P abziehen, da camera im Ursrpung
        toCameraVex = (-P).xyz;

}

