package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgramType
import org.joml.Vector3f


//setCenterOffset hinzu
//getCenterOffset hinzu

class Renderable (var meshObjectList: MutableList<Mesh>)
    : IRenderable, Transformable() {

    private var centerOffset : Vector3f = Vector3f(0.0f, 0.0f, 0.0f)

    override fun setCenterOffset(offset: Vector3f) {
        this.centerOffset = offset
    }

    override fun getCenterOffset(): Vector3f {
        return this.centerOffset;
    }

    override fun render(shaderProgram: ShaderProgramType) {
        var worldModelMatrix = getWorldModelMatrix()
        shaderProgram.setUniform("model_matrix", worldModelMatrix, false)
        for (m in meshObjectList) {
            m.render(shaderProgram)
        }
    }
}