package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgramType

interface IPointLight {
    fun bind(shaderProgram: ShaderProgramType, name: String)
}