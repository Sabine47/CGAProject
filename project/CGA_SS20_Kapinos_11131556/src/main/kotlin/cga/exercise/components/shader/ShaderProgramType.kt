package cga.exercise.components.shader

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Sabine am 15.09.2020.
 */

open class ShaderProgramType {

    open fun use() {
    }

    open fun cleanup() {
    }

    open fun setUniform() : Boolean{
        return true
    }

    open fun setUniform(name: String, value: Float) : Boolean{
        return true
    }

    open fun setUniform(name: String, value: Matrix4f, transpose: Boolean): Boolean {
        return true
    }

    open fun setUniform(name: String, value: Int): Boolean {
        return true
    }

    open fun setUniform(name: String, value: Vector2f): Boolean {
        return true
    }

    open fun setUniform(name: String, value: Vector3f): Boolean {
        return true
    }

    open fun setUniform(name: String, value: Matrix3f): Boolean {
        return true
    }

}
