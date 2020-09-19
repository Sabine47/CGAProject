package cga.exercise.components.geometry


import cga.exercise.components.shader.ShaderProgramType
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */

class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, var material: Material? = null) {

    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = indexdata.size

    init {

        //VAO
        vao = glGenVertexArrays() //create VAO
        glBindVertexArray(vao) //activate VAO

        //VBO
        vbo = glGenBuffers() //create VBO
        glBindBuffer(GL_ARRAY_BUFFER, vbo) //activate VBO

        //IBO
        ibo = glGenBuffers() //create IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo) //activate IBO


        // todo: upload your mesh data

        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW) //upload data to VBO
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW) //upload data to IBO

        //activate attributes

        for (i in attributes.indices){
            glEnableVertexAttribArray(i)
            glVertexAttribPointer(i, attributes[i].n, attributes[i].type, true, attributes[i].stride, attributes[i].offset.toLong() )
        }
        glBindVertexArray(0)
    }

     /**
     * renders the mesh
     */
    fun render() {

        // call the rendering method every frame

        // VAO binden
        glBindVertexArray(vao)

        //indexcount = Anzahl indices (DrawElements) im Array
        glDrawElements(GL_TRIANGLES, indexcount, GL_UNSIGNED_INT,0)

        //Bindung aufheben. Auch wenn Inhalte in cleanup gelöscht werden.
        glBindVertexArray(0)

    }
    //In Renderable.render "shader" ergänzt
    fun render (shader: ShaderProgramType){
        material?.bind(shader)
        render()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) glDeleteBuffers(ibo)
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }

}