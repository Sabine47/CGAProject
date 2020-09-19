#version 330 core
#extension GL_EXT_geometry_shader4: enable

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec2 texturVex[];
in vec3 toPointLightVex[];
in vec3 toCameraVex[];
//Normals im GeoShader per Primitiv "herstellen"

uniform mat4 projection_matrix;
uniform float time;//fürs Pulsieren

out vec2 texturGeo;
out vec3 toPointLightGeo;
out vec3 toCameraGeo;
out vec3 normalGeo;


//Verschieben der Dreiecke entlang der Normalen
//hin und her durch timeKomponente, +1 für positive Werte beim sin

vec4 pulsieren(vec4 position, vec3 normal){
    vec3 direction = normal *((sin(10*time) + 1.0)/10.0f);
    return (position + vec4(direction, 1.0));
}

void main(){
    //Normale eines Dreiecks durch Kreuzprodukt zweier Seiten des Dreiecks

    //Vertexpostitionen des Dreiecks
    vec3 a = vec3(gl_in[0].gl_Position);
    vec3 b =vec3(gl_in[1].gl_Position);
    vec3 c = vec3(gl_in[2].gl_Position);

    //Seite 1
    vec3 sideA = a-b;

    //Seite 2
    vec3 sideB = c-b;

    //Kreuzprodukt zweier Seiten ergibt Normale des Dreiecks
    vec3 normalGeo = normalize(cross(sideB, sideA));

    gl_Position = projection_matrix * pulsieren(gl_in[0].gl_Position, normalGeo);
    texturGeo = texturVex[0];
    toPointLightGeo = toPointLightVex[0];
    toCameraGeo = toCameraVex[0];
    EmitVertex();

    gl_Position = projection_matrix * pulsieren(gl_in[1].gl_Position, normalGeo);
    texturGeo = texturVex[1];
    toPointLightGeo = toPointLightVex[1];
    toCameraGeo = toCameraVex[1];
    EmitVertex();

    gl_Position = projection_matrix * pulsieren(gl_in[2].gl_Position, normalGeo);
    texturGeo = texturVex[2];
    EmitVertex();
    toPointLightGeo = toPointLightVex[2];
    toCameraGeo = toCameraVex[2];
    EndPrimitive();

}
