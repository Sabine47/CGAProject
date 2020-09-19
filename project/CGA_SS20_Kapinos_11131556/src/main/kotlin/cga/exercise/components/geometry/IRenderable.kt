package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgramType
import org.joml.Vector3f

//setCenterOffset hinzu
//getCenterOffset hinzu

interface IRenderable {
    fun setCenterOffset(offset: Vector3f)
    fun getCenterOffset(): Vector3f
    fun render(shaderProgram: ShaderProgramType)
}