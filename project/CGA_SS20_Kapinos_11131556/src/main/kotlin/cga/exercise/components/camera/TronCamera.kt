package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgramType
import org.joml.Matrix4f
import org.joml.Vector3f

class TronCamera (var fov : Float,
                  var aspect : Float,
                  var near: Float,
                  var far : Float) : ICamera, Transformable() {

    override fun getCalculateViewMatrix(): Matrix4f {

        var viewMatrix = Matrix4f()


        var up: Vector3f = getWorldYAxis()
        var eye : Vector3f = getWorldPosition()
        var center: Vector3f = getWorldPosition().sub(getWorldZAxis())

        return viewMatrix.lookAt(eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0], up[1], up[2])
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        var projectionMatrix = Matrix4f()
        return projectionMatrix.perspective(fov, aspect, near, far)

    }

    override fun bind(shaderProgramType: ShaderProgramType) {
        shaderProgramType.setUniform("view_matrix", getCalculateViewMatrix(), false)
        shaderProgramType.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)
    }
}