#version 330 core

//input from vertex shader

in vec2 textur;

//uniforms
uniform sampler2D diff;
uniform vec3 emissive;

//out
out vec4 color;

void main() {

    color = vec4(texture(diff, textur).rgb * emissive, 1.0);

}




