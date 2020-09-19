package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgramType
import org.joml.Matrix4f

interface ISpotLight {
    fun bind(shaderProgram: ShaderProgramType, name: String, viewMatrix: Matrix4f)
}