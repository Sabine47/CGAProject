
package cga.exercise.game

import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.texture.Texture2D

import org.joml.Vector2f

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute

import cga.framework.ModelLoader
import cga.framework.OBJLoader
import cga.framework.Vertex
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL11
import kotlin.math.abs



open class RenderableLoader{

    open fun loadRenderable(modelName: String, shininess: Float, textureName_diff: String, textureName_emit: String, textureName_spec: String): Renderable{

        //Textures
        val modelName_diff = Texture2D("assets/textures/" + textureName_diff, genMipMaps = true)
        val modelName_emit = Texture2D("assets/textures/" + textureName_emit, genMipMaps = true)
        val modelName_spec = Texture2D("assets/textures/" + textureName_spec, genMipMaps = true)

        modelName_diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        modelName_emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        modelName_spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

        //tcMultiplier für Material
        var tcMultiplier = Vector2f(1.0f, 1.0f)

        //objectLoader
        val modelNameMaterial = Material(modelName_diff, modelName_emit, modelName_spec, shininess, tcMultiplier)
        val modelNameObjRes = OBJLoader.loadOBJ("assets/models_textures/objectLoader/$modelName.obj")
        val modelNameMesh: MutableList<Mesh> = mutableListOf()

        //Vertexattributes
        var attributes: Array<VertexAttribute>
            val position = VertexAttribute(3, GL_FLOAT, 32, 0)
            val texture = VertexAttribute(2, GL_FLOAT, 32, 12)
            val norm = VertexAttribute(3, GL_FLOAT, 32, 20)
            //2. Attribut-Array anlegen und Attribute hineinlegen. Drei Attribute in Meshklasse enablen.
            attributes = arrayOf(position, texture, norm)

        for (modelNameObjMesh in modelNameObjRes.objects[0].meshes) {
            modelNameMesh.add(Mesh(modelNameObjMesh.vertexData, modelNameObjMesh.indexData, attributes, modelNameMaterial))
        }
        //ich erstelle mir das Renderable hier und gebe es zurück
        return Renderable(modelNameMesh)
    }

    //loadModel##########################
    //Pfad anpassen
    //Transformationen in Scene


    open fun loadRenderable (modelName: String) : Renderable{

        val objectPath = "assets/models_textures/modelloader/$modelName.obj"

        var pitch = 0.0f
        var yaw = 0.0f
        //var yaw= Math.toRadians(70.0).toFloat()
        var roll= 0.0f

        return ModelLoader.loadModel (objectPath, pitch, yaw, roll)?: throw IllegalArgumentException("Could not load the model")
        }

}