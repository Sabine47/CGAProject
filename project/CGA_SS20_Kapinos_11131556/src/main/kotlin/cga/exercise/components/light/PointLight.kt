package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgramType
import org.joml.Vector3f;

open class PointLight(var pointLightPosition: Vector3f, var pointLightColor: Vector3f) : IPointLight, Transformable() {

    var attenuation: Vector3f

    init {
        translateGlobal(pointLightPosition)
        attenuation = Vector3f(1.0f, 0.5f, 0.1f)
    }

    override fun bind(shaderProgram: ShaderProgramType, name: String) {

        shaderProgram.setUniform(name + "LightColor", pointLightColor)
        shaderProgram.setUniform(name + "LightPosition", getWorldPosition())
        shaderProgram.setUniform(name + "Attenuation", attenuation)

    }

}
