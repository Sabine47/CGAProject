#version 330 core

//input
layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 tex;
layout(location = 2) in vec3 norm;

//uniforms
uniform mat4 view_matrix, projection_matrix;
uniform vec2 tcMultiplier;

//output
out vec2 textur;

void main() {

 textur = tex * tcMultiplier;

 //out vertexPosition //model_matrix nicht nötig
 vec4 positionVex = projection_matrix * view_matrix * vec4(pos, 1.0f);
 gl_Position = positionVex;

}

