
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
 * Created by Fabian on 16.09.2017.
 * Ergänzt by Sabine um GeometryShader am 27.08.2020.
 */

open class ShaderProgram (vertexShaderPath: String, fragmentShaderPath: String) : ShaderProgramType() {

    protected var programID: Int = 0
    protected var vShader: Int = 0;
    protected var fShader: Int = 0;

    // Matrix buffers for setting matrix uniforms. Prevents allocation for each uniform
    protected var m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)

    /**
     * Sets the active shader program of the OpenGL render pipeline to this shader
     * if this isn't already the currently active shader
     */
    override fun use() {
        val curprog = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
        if (curprog != programID) GL20.glUseProgram(programID)

    }

    /**
     * Frees the allocated OpenGL objects
     */
    override fun cleanup() {
        GL20.glDeleteProgram(programID)
    }

    //setUniform() functions are added later during the course

    // float vector uniforms
    /**
     * Sets a single float uniform
     * @param name  Name of the uniform variable in the shader
     * @param value Value
     * @return returns false if the uniform was not found in the shader
     */
    override fun setUniform(name: String, value: Float): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1f(loc, value)
            return true
        }
        return false
    }

    // Matrix4f uniforms
    /**
     * Sets a mat4 uniform (joml to mat4)
     */
    override fun setUniform(name: String, value: Matrix4f, transpose: Boolean): Boolean{
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            // m4x4buf per value[buffer] mit Daten aus Matrix4f befüllen.
            m4x4buf = value.get(m4x4buf)

            GL20.glUniformMatrix4fv(loc, false, this.m4x4buf)
            return true
        }
        return false
    }

    //sampler2D uniforms
    /**
     * Sets a sampler2D uniform
     */
    override fun setUniform(name: String, value: Int): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1i(loc, value)
            return true
        }
        return false
    }

    // vec2 uniforms
    /**
     * Sets a vec2 uniform
     */
    override fun setUniform(name: String, value: Vector2f): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform2f(loc, value[0], value[1])
            return true
        }
        return false
    }

    // vec3 uniforms
    /**
     * Sets a vec3 uniform
     */
    override fun setUniform(name: String, value: Vector3f): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform3f(loc, value[0], value[1], value[2])
            return true
        }
        return false
    }

    // mat3 uniforms
    /**
     * Sets a mat3 uniform
     */
    override fun setUniform(name: String, value: Matrix3f): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniformMatrix3fv(loc, false, floatArrayOf(
                    value.get(0, 0), value.get(0, 1), value.get(0, 2),
                    value.get(1, 0), value.get(1, 1), value.get(1, 2),
                    value.get(2, 0), value.get(2, 1), value.get(2, 2)
            ))
            return true
        }
        return false
    }

    /**
     * Creates a shader object from vertex and fragment shader paths
     * @param vertexShaderPath      vertex shader path
     * @param fragmentShaderPath    fragment shader path
     * @throws Exception if shader compilation failed, an exception is thrown
     */
    init {
        val vPath = Paths.get(vertexShaderPath)
        val fPath = Paths.get(fragmentShaderPath)

        val vSource = String(Files.readAllBytes(vPath))
        val fSource = String(Files.readAllBytes(fPath))

        vShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        if (vShader == 0) throw Exception("Vertex shader object couldn't be created.")


        fShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        if (fShader == 0) {
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader object couldn't be created.")
        }
        GL20.glShaderSource(vShader, vSource)
        GL20.glShaderSource(fShader, fSource)


        //Compile Shader
        GL20.glCompileShader(vShader)
        if (GL20.glGetShaderi(vShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(vShader)
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Vertex shader compilation failed:\n$log")
        }

        GL20.glCompileShader(fShader)
        if (GL20.glGetShaderi(fShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(fShader)
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader compilation failed:\n$log")
        }

        //Create Program
        programID = GL20.glCreateProgram()
        if (programID == 0) {
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program object creation failed.")
        }

        //Attach Shader
        GL20.glAttachShader(programID, vShader)
        GL20.glAttachShader(programID, fShader)

        //Link Program
        GL20.glLinkProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetProgramInfoLog(programID)
            GL20.glDetachShader(programID, vShader)
            GL20.glDetachShader(programID, fShader)
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program linking failed:\n$log")
        }

        //Detach Shader
        GL20.glDetachShader(programID, vShader)
        GL20.glDetachShader(programID, fShader)
        GL20.glDeleteShader(vShader)
        GL20.glDeleteShader(fShader)
    }

}
//package cga.exercise.components.shader
//
//import org.joml.*
//import org.lwjgl.BufferUtils
//import org.lwjgl.opengl.GL11
//import org.lwjgl.opengl.GL20.*
//import java.nio.FloatBuffer
//import java.nio.file.Files
//import java.nio.file.Paths
//
///**
// * Created by Fabian on 16.09.2017.
// */
//
//
//open class ShaderProgram(vertexShaderPath: String, fragmentShaderPath: String) {
//    protected var programID: Int = 0
//
//    protected var vShader: Int = 0;
//
//    protected var fShader: Int = 0;
//
//    // Matrix buffers for setting matrix uniforms. Prevents allocation for each uniform
//    protected var m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)
//
//    /**
//     * Sets the active shader program of the OpenGL render pipeline to this shader
//     * if this isn't already the currently active shader
//     */
//    fun use() {
//        val curprog = GL11.glGetInteger(GL_CURRENT_PROGRAM)
//        if (curprog != programID) glUseProgram(programID)
//
//    }
//
//    /**
//     * Frees the allocated OpenGL objects
//     */
//    fun cleanup() {
//        glDeleteProgram(programID)
//    }
//
//    //setUniform() functions are added later during the course
//
//    // float vector uniforms
//    /**
//     * Sets a single float uniform
//     * @param name  Name of the uniform variable in the shader
//     * @param value Value
//     * @return returns false if the uniform was not found in the shader
//     */
//    fun setUniform(name: String, value: Float): Boolean {
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            glUniform1f(loc, value)
//            return true
//        }
//        return false
//    }
//
//    // Matrix4f uniforms
//    /**
//     * Sets a mat4 uniform (joml to mat4)
//     */
//    fun setUniform(name: String, value: Matrix4f, transpose: Boolean): Boolean{
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            // m4x4buf per value[buffer] mit Daten aus Matrix4f befüllen.
//                m4x4buf = value.get(m4x4buf)
//
//            glUniformMatrix4fv(loc, false, this.m4x4buf)
//            return true
//            }
//    return false
//    }
//
//    //sampler2D uniforms
//    /**
//     * Sets a sampler2D uniform
//     */
//    fun setUniform(name: String, value: Int): Boolean {
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            glUniform1i(loc, value)
//            return true
//        }
//        return false
//    }
//
//
//    //PRÜFEN
////    //samplerCube uniforms
////    /**
////     * Sets a samplerCube uniform
////     */
////    fun setUniform(name: String, value: Int): Boolean {
////        if (programID == 0) return false
////        val loc = glGetUniformLocation(programID, name)
////        if (loc != -1) {
////            glUniform   (loc, value)
////            return true
////        }
////        return false
////    }
//
//    // vec2 uniforms
//    /**
//     * Sets a vec2 uniform
//     */
//    fun setUniform(name: String, value: Vector2f): Boolean {
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            glUniform2f(loc, value[0], value[1])
//            return true
//        }
//        return false
//    }
//
//    // vec3 uniforms
//    /**
//     * Sets a vec3 uniform
//     */
//    fun setUniform(name: String, value: Vector3f): Boolean {
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            glUniform3f(loc, value[0], value[1], value[2])
//            return true
//        }
//        return false
//    }
//
//    // mat3 uniforms
//    /**
//     * Sets a mat3 uniform
//     */
//    fun setUniform(name: String, value: Matrix3f): Boolean {
//        if (programID == 0) return false
//        val loc = glGetUniformLocation(programID, name)
//        if (loc != -1) {
//            glUniformMatrix3fv(loc, false, floatArrayOf(
//                value.get(0, 0), value.get(0, 1), value.get(0, 2),
//                value.get(1, 0), value.get(1, 1), value.get(1, 2),
//                value.get(2, 0), value.get(2, 1), value.get(2, 2)
//            ))
//            return true
//        }
//        return false
//    }
//
//    /**
//     * Creates a shader object from vertex and fragment shader paths
//     * @param vertexShaderPath      vertex shader path
//     * @param fragmentShaderPath    fragment shader path
//     * @throws Exception if shader compilation failed, an exception is thrown
//     */
//    init {
//        val vPath = Paths.get(vertexShaderPath)
//        val fPath = Paths.get(fragmentShaderPath)
//        val vSource = String(Files.readAllBytes(vPath))
//        val fSource = String(Files.readAllBytes(fPath))
//        vShader = glCreateShader(GL_VERTEX_SHADER)
//        if (vShader == 0) throw Exception("Vertex shader object couldn't be created.")
//        fShader = glCreateShader(GL_FRAGMENT_SHADER)
//        if (fShader == 0) {
//            glDeleteShader(vShader)
//            throw Exception("Fragment shader object couldn't be created.")
//        }
//        glShaderSource(vShader, vSource)
//        glShaderSource(fShader, fSource)
//        glCompileShader(vShader)
//        if (glGetShaderi(vShader, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
//            val log = glGetShaderInfoLog(vShader)
//            glDeleteShader(fShader)
//            glDeleteShader(vShader)
//            throw Exception("Vertex shader compilation failed:\n$log")
//        }
//        glCompileShader(fShader)
//        if (glGetShaderi(fShader, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
//            val log = glGetShaderInfoLog(fShader)
//           glDeleteShader(fShader)
//            glDeleteShader(vShader)
//            throw Exception("Fragment shader compilation failed:\n$log")
//        }
//        programID = glCreateProgram()
//        if (programID == 0) {
//            glDeleteShader(vShader)
//            glDeleteShader(fShader)
//            throw Exception("Program object creation failed.")
//        }
//        glAttachShader(programID, vShader)
//        glAttachShader(programID, fShader)
//        glLinkProgram(programID)
//        if (glGetProgrami(programID, GL_LINK_STATUS) == GL11.GL_FALSE) {
//            val log = glGetProgramInfoLog(programID)
//            glDetachShader(programID, vShader)
//            glDetachShader(programID, fShader)
//            glDeleteShader(vShader)
//            glDeleteShader(fShader)
//            throw Exception("Program linking failed:\n$log")
//        }
//        glDetachShader(programID, vShader)
//        glDetachShader(programID, fShader)
//        glDeleteShader(vShader)
//        glDeleteShader(fShader)
//    }
//}