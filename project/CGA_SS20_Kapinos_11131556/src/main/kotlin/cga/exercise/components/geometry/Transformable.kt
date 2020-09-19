package cga.exercise.components.geometry

import org.joml.*

open class Transformable : ITransformable {

    private var modelMatrix: Matrix4f = Matrix4f()

    var parent: Transformable? = null

    //
    override fun setModelMatrix(matrix: Matrix4f) {
        this.modelMatrix = matrix
    }

    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        modelMatrix.rotateXYZ(pitch, yaw, roll)
    }

    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
        val rotateModelMatrix = Matrix4f()
        rotateModelMatrix.translate(altMidpoint)
        rotateModelMatrix.rotateXYZ(pitch, yaw, roll)
        rotateModelMatrix.translate(Vector3f(altMidpoint).negate())
        modelMatrix = rotateModelMatrix.mul(modelMatrix)
    }
    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    override fun translateLocal(deltaPos: Vector3f) {
        modelMatrix.translate(deltaPos)
    }
    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    override fun translateGlobal(deltaPos: Vector3f) {
        //Translation auf Basis des parent-Koordinatensystems
        //left-multiplied
        modelMatrix = Matrix4f().translate(deltaPos).mul(modelMatrix)
    }

    override fun scaleLocal(scale: Vector3f) {
        modelMatrix.scale(scale)
    }

    override fun getPosition(): Vector3f {
        //Returns position based on aggregated translations.
        //Hint: last column of model matrix
        return Vector3f(
                modelMatrix.m30(),
                modelMatrix.m31(),
                modelMatrix.m32()
        )
    }
    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    override fun getWorldPosition(): Vector3f {
        //Returns position based on aggregated translations incl. parents.
        //Hint: last column of world model matrix
              var worldModelMatrix = getWorldModelMatrix()
                return Vector3f(
                        worldModelMatrix.m30(),
                        worldModelMatrix.m31(),
                        worldModelMatrix.m32()
                )
            }

    override fun getXAxis(): Vector3f {
        //Hint: First normalized column of model_matrix
        //var modelMatrixNormalisiert = modelMatrix.normalize3x3()
        return Vector3f(
                modelMatrix.m00(),
                modelMatrix.m01(),
                modelMatrix.m02()
        ).normalize()
    }

    override fun getYAxis(): Vector3f {
        //Hint: second normalized column of model_matrix
        //var modelMatrixNormalisiert = modelMatrix.normalize3x3()
        return Vector3f(
                modelMatrix.m10(),
                modelMatrix.m11(),
                modelMatrix.m12()

        ).normalize()
    }

    override fun getZAxis(): Vector3f {
        //Hint: third normalized column of model_matrix
        //var modelMatrixNormalisiert = modelMatrix.normalize3x3()
        return Vector3f(
                modelMatrix.m20(),
                modelMatrix.m21(),
                modelMatrix.m22()
        ).normalize()

    }
    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    override fun getWorldXAxis(): Vector3f {
        var worldModelMatrix = getWorldModelMatrix()
        return Vector3f(
                worldModelMatrix.m00(),
                worldModelMatrix.m01(),
                worldModelMatrix.m02()
        ).normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    override fun getWorldYAxis(): Vector3f {
        var worldModelMatrix = getWorldModelMatrix()
        return Vector3f(
                worldModelMatrix.m10(),
                worldModelMatrix.m11(),
                worldModelMatrix.m12()
        ).normalize()
    }
    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    override fun getWorldZAxis(): Vector3f {
        var worldModelMatrix = getWorldModelMatrix()
        return Vector3f(
                worldModelMatrix.m20(),
                worldModelMatrix.m21(),
                worldModelMatrix.m22()
        ).normalize()
        }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    override fun getWorldModelMatrix(): Matrix4f {
        val worldMatrix = Matrix4f(modelMatrix)
        parent?.getWorldModelMatrix()?.mul(modelMatrix, worldMatrix)
        return worldMatrix
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    override fun getLocalModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }
}