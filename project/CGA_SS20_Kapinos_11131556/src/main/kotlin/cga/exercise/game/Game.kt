package cga.exercise.game

import cga.framework.GameWindow
import org.lwjgl.glfw.GLFW

/*
  Created by Fabian on 16.09.2017.
 */
class Game(width: Int,
           height: Int,
           fullscreen: Boolean = false,
           vsync: Boolean = false,
           title: String = "Testgame",
           GLVersionMajor: Int = 3,
           GLVersionMinor: Int = 3) : GameWindow(width, height, fullscreen, vsync, GLVersionMajor, GLVersionMinor, title, 4, 120.0f) {

    private val scene: Scene

    init {
        setCursorVisible(false)
        scene = Scene(this)
    }

    override fun shutdown() = scene.cleanup()

    override fun update(dt: Float, t: Float) = scene.update(dt, t)

    override fun render(dt: Float, t: Float) = scene.render(dt, t)

    override fun onMouseMove(xpos: Double, ypos: Double) = scene.onMouseMove(xpos, ypos)

    //onMouseButton hinzu
    override fun onMouseButton(button: Int, action: Int, mode: Int) = scene.onMouseButton(button, action, mode)

    //onMouseScroll hinzu
    override fun onMouseScroll(xoffset: Double, yoffset: Double) = scene.onMouseScroll(xoffset,yoffset)

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int) = scene.onKey(key, scancode, action, mode)
}
