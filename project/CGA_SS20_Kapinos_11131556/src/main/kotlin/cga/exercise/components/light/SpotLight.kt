package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgramType
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f

class SpotLight(pointLightPosition: Vector3f, pointLightColor: Vector3f, private var gammaInner: Float, private var phiOuter: Float)
    : ISpotLight, PointLight(pointLightPosition, pointLightColor) {

    init {
        translateGlobal(pointLightPosition)
        attenuation = Vector3f(0.5f, 0.05f, 0.01f)

    }

    override fun bind(shaderProgram: ShaderProgramType, name: String, viewMatrix: Matrix4f) {
        super.bind(shaderProgram, name) //color, position, kc, kl, kq
        shaderProgram.setUniform(name + "LightGammaInner", gammaInner)
        shaderProgram.setUniform(name + "LightPhiOuter", phiOuter)
        shaderProgram.setUniform(name + "Direction", getWorldZAxis().negate().mul(Matrix3f(viewMatrix)))

    }

}