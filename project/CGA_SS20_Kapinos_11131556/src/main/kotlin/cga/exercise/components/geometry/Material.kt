package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgramType
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f


//Reihenfolge im Konstruktor MUSS Reihenfolge in assets/textures entsprechen (diff, emit, spec).

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var specular: Texture2D,
               var shininess: Float,
               var tcMultiplier : Vector2f = Vector2f(1.0f)){

    fun bind(shaderProgramType: ShaderProgramType) {

        emit.bind(0)
        diff.bind(1)
        specular.bind(2)


        shaderProgramType.setUniform("emit", 0)
        shaderProgramType.setUniform("diff", 1)
        shaderProgramType.setUniform("spec", 2)
        shaderProgramType.setUniform("tcMultiplier", tcMultiplier)
        shaderProgramType.setUniform("shininess", shininess)

    }
}