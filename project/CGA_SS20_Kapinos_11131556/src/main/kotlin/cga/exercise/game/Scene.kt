package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.light.PointLight
import cga.exercise.components.shader.GeometryShaderProgram
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.shader.ShaderProgramType
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL20.*
import kotlin.math.abs
import kotlin.math.absoluteValue

class Scene(private val window: GameWindow) {

    //shader
    private var shader: ShaderProgramType

    private val phongShader: ShaderProgram
    private val skyBoxShader: ShaderProgram
    private val toonShader: ShaderProgram
    private val pulsierenShader: GeometryShaderProgram
    private val pulsierenZweiShader: GeometryShaderProgram
    private val pochenShader: GeometryShaderProgram

    //camera
    private val camera: TronCamera

    //Sonne
    private var sunRenderable: Renderable

    //Planeten
    private var earthRenderable: Renderable
    private var merkurRenderable: Renderable
    private var venusRenderable: Renderable
    private var marsRenderable: Renderable
    private var jupiterRenderable: Renderable
    private var saturnRenderable: Renderable
    private var uranusRenderable: Renderable
    private var neptunRenderable: Renderable

    //Monde
    private var erdMondRenderable: Renderable
    private var jupiter1MondRenderable: Renderable
    private var jupiter2MondRenderable: Renderable

    //andere 3D Objekte
    private val apricotRenderable: Renderable
    private val statuetteRenderable: Renderable

    //private val statuetteRenderableNormals: RenderableNormals
    //
    private val statuette2Renderable: Renderable
    private var statuette2ShowYourself: Boolean

    //
    private val youSavedUniverseRenderable: Renderable
    private var youSavedUniverseShowYourself: Boolean

    //Spritzen
    private var spritzenMunition = arrayListOf<Renderable>()
    private var fliegendeSpritzen = arrayListOf<Boolean>()
    private var unsichtbareSpritzen = arrayListOf<Boolean>()
    private var spritzenModelMatrizen = arrayListOf<Matrix4f>()

    //Spritzen Fly-Komponente
    private var fly: Boolean

    //Viren
    private var virusEinsRenderable: Renderable
    private var virusZweiRenderable: Renderable
    private var virusDreiRenderable: Renderable
    private var virusVierRenderable: Renderable
    private var virusFuenfRenderable: Renderable
    private var virusSechsRenderable: Renderable

    //VirusArray
    private var virusArray = arrayListOf<Renderable>()
    private var virusMitBeinenGroesse: Float

    //Virus Alive-Komponente
    private var aliveArray = arrayListOf<Boolean>()

    //spaceBox
    private var rightRenderable: Renderable
    private var leftRenderable: Renderable
    private var frontRenderable: Renderable
    private var backRenderable: Renderable
    private var topRenderable: Renderable
    private var bottomRenderable: Renderable
    private var skyBoxColor: Vector3f

    //pointLight
    private var pointLightColor: Vector3f
    private var sunPointLight: PointLight

    //spotLight
    //private var cameraSpotLight: SpotLight

    //onMouseMove
    var changedXpos: Double = 0.0
    var changedYpos: Double = 0.0

    private var emissiveSun: Vector3f


    //scene setup
    init {

        //Shader

        phongShader = ShaderProgram(
                "assets\\shaders\\phong_vert.glsl",
                "assets\\shaders\\phong_frag.glsl"
        )
        skyBoxShader = ShaderProgram(
                "assets\\shaders\\skyBox_vert.glsl",
                "assets\\shaders\\skyBox_frag.glsl"
        )
        toonShader = ShaderProgram(
                "assets\\shaders\\toon_vert.glsl",
                "assets\\shaders\\toon_frag.glsl"
        )
        pulsierenShader = GeometryShaderProgram(
                "assets\\shaders\\pulsieren_vert.glsl",
                "assets\\shaders\\pulsieren_geom.glsl",
                "assets\\shaders\\pulsieren_frag.glsl"
        )
        pulsierenZweiShader = GeometryShaderProgram(
                "assets\\shaders\\pulsieren_vert.glsl",
                "assets\\shaders\\pulsierenZwei_geom.glsl",
                "assets\\shaders\\pulsieren_frag.glsl"
        )
        pochenShader = GeometryShaderProgram(
                "assets\\shaders\\pulsieren_vert.glsl",
                "assets\\shaders\\pochen_geom.glsl",
                "assets\\shaders\\pulsieren_frag.glsl"
        )

        shader = phongShader

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow()
        //glDisable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()


//**************************************************************************************************************
//CameraAnlegen
//**************************************************************************************************************
        camera = TronCamera(Math.toRadians(60.0f), (16.0f / 9.0f), 0.1f, 2000.0f)
        camera.translateLocal(Vector3f(0.0f, 0.0f, 130f))


//**************************************************************************************************************
//ModelsAnlegen       "solarsystemscope.com"
//**************************************************************************************************************

        val renderableLoader = RenderableLoader()

//Sun###########################################################################################################
        sunRenderable = renderableLoader.loadRenderable("sun", 25.0f, "sun_diff.jpg", "sun_diff.jpg", "sun_diff.jpg")
        sunRenderable.scaleLocal(Vector3f(12f))
        emissiveSun = Vector3f(0.0f, 0.0f, 1f)

//Merkur########################################################################################################
        merkurRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "merkur_diff.png", "merkur_diff.png", "merkur_diff.png")
        merkurRenderable.parent = sunRenderable
        merkurRenderable.translateGlobal(Vector3f(0f, 0.0f, 3.5f))
        merkurRenderable.rotateAroundPoint(0.0f, Math.toRadians(-40.0).toFloat(), 0.0f, Vector3f(0.0f))
        merkurRenderable.scaleLocal(Vector3f(0.25f))

//Venus#########################################################################################################
        venusRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "venus_diff.jpg", "venus_diff.jpg", "venus_diff.jpg")
        venusRenderable.parent = sunRenderable
        venusRenderable.translateGlobal(Vector3f(0f, 0.0f, 5.0f))
        venusRenderable.rotateAroundPoint(0.0f, Math.toRadians(220.0).toFloat(), 0.0f, Vector3f(0.0f))
        venusRenderable.scaleLocal(Vector3f(0.3f))

//Earth#########################################################################################################
        earthRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "earth_diff.jpg", "earth_diff.jpg", "earth_diff.jpg")
        earthRenderable.parent = sunRenderable
        earthRenderable.translateGlobal(Vector3f(0f, 0.0f, 6.5f))
        earthRenderable.rotateAroundPoint(0.0f, Math.toRadians(140.0).toFloat(), 0.00f, Vector3f(0.0f))
        earthRenderable.scaleLocal(Vector3f(0.4f))

//Mars##########################################################################################################
        marsRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "mars_diff.jpg", "mars_diff.jpg", "mars_diff.jpg")
        marsRenderable.parent = sunRenderable
        marsRenderable.translateGlobal(Vector3f(0f, 0.0f, 8.0f))
        marsRenderable.rotateAroundPoint(0.0f, Math.toRadians(20.0).toFloat(), 0.0f, Vector3f(0.0f))
        marsRenderable.scaleLocal(Vector3f(0.25f))

//Jupiter#######################################################################################################
        jupiterRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "jupiter_diff.jpg", "jupiter_diff.jpg", "jupiter_diff.jpg")
        jupiterRenderable.parent = sunRenderable
        jupiterRenderable.translateGlobal(Vector3f(0f, 0.0f, 9.5f))
        jupiterRenderable.rotateAroundPoint(0.0f, Math.toRadians(-10.0).toFloat(), 0.00f, Vector3f(0.0f))
        jupiterRenderable.scaleLocal(Vector3f(0.6f))

//Saturn########################################################################################################
        saturnRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "saturn_diff.jpg", "saturn_diff.jpg", "saturn_diff.jpg")
        saturnRenderable.parent = sunRenderable
        saturnRenderable.translateGlobal(Vector3f(0.0f, 0.0f, -11.0f))
        saturnRenderable.rotateAroundPoint(0.0f, Math.toRadians(-40.0).toFloat(), 0.00f, Vector3f(0.0f))
        saturnRenderable.scaleLocal(Vector3f(0.5f))

//Uranus########################################################################################################
        uranusRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "uranus_diff.jpg", "uranus_diff.jpg", "uranus_diff.jpg")
        uranusRenderable.parent = sunRenderable
        uranusRenderable.translateGlobal(Vector3f(0f, 0.0f, -12.5f))
        uranusRenderable.rotateAroundPoint(0.0f, Math.toRadians(-135.0).toFloat(), 0.00f, Vector3f(0.0f))
        uranusRenderable.scaleLocal(Vector3f(0.4f))

//Neptun########################################################################################################
        neptunRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "neptun_diff.jpg", "neptun_diff.jpg", "neptun_diff.jpg")
        neptunRenderable.parent = sunRenderable
        neptunRenderable.translateGlobal(Vector3f(0f, 0.0f, -14.0f))
        neptunRenderable.rotateAroundPoint(0.0f, Math.toRadians(115.0).toFloat(), 0.00f, Vector3f(0.0f))
        neptunRenderable.scaleLocal(Vector3f(0.3f))

//ErdMond#######################################################################################################
        erdMondRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "erdMond_diff.jpg", "erdMond_diff.jpg", "erdMond_diff.jpg")
        erdMondRenderable.parent = earthRenderable
        erdMondRenderable.translateGlobal(Vector3f(0.0f, 0.0f, 2.0f))
        erdMondRenderable.scaleLocal(Vector3f(0.2f))

//Jupiter1Mond##################################################################################################
        jupiter1MondRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "erdMond_diff.jpg", "erdMond_diff.jpg", "erdMond_diff.jpg")
        jupiter1MondRenderable.parent = jupiterRenderable
        jupiter1MondRenderable.translateGlobal(Vector3f(0.0f, 0.0f, 1.2f))
        jupiter1MondRenderable.scaleLocal(Vector3f(0.2f))

//Jupiter2Mond##################################################################################################
        jupiter2MondRenderable = renderableLoader.loadRenderable("merkur", 25.0f, "erdMond_diff.jpg", "erdMond_diff.jpg", "erdMond_diff.jpg")
        jupiter2MondRenderable.parent = jupiterRenderable
        jupiter2MondRenderable.translateGlobal(Vector3f(0.0f, 0.0f, 1.8f))
        jupiter2MondRenderable.scaleLocal(Vector3f(0.25f))

//SpaceBox######################################################################################################
        backRenderable = renderableLoader.loadRenderable("back", 10.0f, "back.png", "back.png", "back.png")
        bottomRenderable = renderableLoader.loadRenderable("bottom", 10.0f, "bottom.png", "bottom.png", "bottom.png")
        frontRenderable = renderableLoader.loadRenderable("front", 10.0f, "front.png", "front.png", "front.png")
        leftRenderable = renderableLoader.loadRenderable("left", 10.0f, "left.png", "left.png", "left.png")
        rightRenderable = renderableLoader.loadRenderable("right", 10.0f, "right.png", "right.png", "left.png")
        topRenderable = renderableLoader.loadRenderable("top", 10.0f, "top.png", "top.png", "top.png")
        skyBoxColor = Vector3f(0.6f, 0.6f, 1.2f)

//Apricot#######################################################################################################
        apricotRenderable = renderableLoader.loadRenderable("Apricot_02_hi_poly")
        apricotRenderable.translateLocal(Vector3f(0.0f, 0.0f, -5.0f))
        apricotRenderable.scaleLocal(Vector3f(0.5f))

//Statuette#####################################################################################################
        statuetteRenderable = renderableLoader.loadRenderable("Statuette")
        statuetteRenderable.translateLocal(Vector3f(0.0f, -10.0f, -60.0f))
        //statutteRenderable.rotateAroundPoint(0.0f, Math.toRadians(90.0).toFloat(), 0.0f, Vector3f(0f))
        statuetteRenderable.scaleLocal(Vector3f(1.5f))

//Statuette2####################################################################################################
        statuette2Renderable = renderableLoader.loadRenderable("Statuette")
        statuette2Renderable.parent = camera
        statuette2Renderable.translateGlobal(Vector3f(-3.0f, -1.0f, -5.0f))
        statuette2Renderable.scaleLocal(Vector3f(0.3f))
        statuette2ShowYourself = false

//youSavedTheUniverse ##########################################################################################
        youSavedUniverseRenderable = renderableLoader.loadRenderable("youSavedUniverse", 10.0f, "youSavedUniverse.png", "youSavedUniverse.png", "youSavedUniverse.png")
        youSavedUniverseRenderable.parent = statuette2Renderable
        youSavedUniverseRenderable.translateGlobal(Vector3f(-3.0f, 3.4f, 7.0f))
        youSavedUniverseRenderable.rotateLocal(0.0f, Math.toRadians(-180.0).toFloat(), 0.0f)
        youSavedUniverseRenderable.scaleLocal(Vector3f(0.5f))
        youSavedUniverseShowYourself = false


//Viren#########################################################################################################
        virusMitBeinenGroesse = 18.0f

//VirusEins
        virusEinsRenderable = renderableLoader.loadRenderable("virusEinsZwei")
        virusEinsRenderable.translateLocal(marsRenderable.getWorldPosition())
        virusEinsRenderable.translateLocal(Vector3f(0f, -20f, 0f))
        virusEinsRenderable.scaleLocal(Vector3f(30.0f))
        virusEinsRenderable.setCenterOffset(Vector3f(0f, 20f, 0f))

//VirusZwei (Klon von 1)
        virusZweiRenderable = renderableLoader.loadRenderable("virusEinsZwei")
        virusZweiRenderable.translateLocal(jupiterRenderable.getWorldPosition())
        virusZweiRenderable.translateLocal(Vector3f(4f, -30f, 0f))
        virusZweiRenderable.scaleLocal(Vector3f(40.0f))
        virusZweiRenderable.setCenterOffset(Vector3f(0f, 30f, 0f))

//VirusDrei (mit Beinen)
        virusDreiRenderable = renderableLoader.loadRenderable("VirusVier/virus")
        virusDreiRenderable.translateLocal(Vector3f(uranusRenderable.getWorldPosition()))
        virusDreiRenderable.translateLocal(Vector3f(0.0f, -25.0f, 0.0f))
        virusDreiRenderable.scaleLocal(Vector3f(virusMitBeinenGroesse))
        virusDreiRenderable.setCenterOffset(Vector3f(0f, 25f, 0f))

//VirusVier (mit Beinen)
        virusVierRenderable = renderableLoader.loadRenderable("virusVier/virus")
        virusVierRenderable.translateLocal(Vector3f(saturnRenderable.getWorldPosition()))
        virusVierRenderable.translateLocal(Vector3f(0.0f, -20.0f, 0f))
        virusVierRenderable.scaleLocal(Vector3f(14.0f))
        virusVierRenderable.setCenterOffset(Vector3f(0f, 20f, 0f))

//VirusFuenf
        virusFuenfRenderable = renderableLoader.loadRenderable("virusFuenfSechs")
        virusFuenfRenderable.translateLocal(neptunRenderable.getWorldPosition())
        virusFuenfRenderable.translateLocal(Vector3f(0f, -6f, 0f))
        virusFuenfRenderable.rotateLocal(0.0f, (Math.toRadians(90.0)).toFloat(), 0.0f)
        virusFuenfRenderable.scaleLocal(Vector3f(3.0f))
        virusFuenfRenderable.setCenterOffset(Vector3f(0f, 6f, 0f))

//VirusSechs
        virusSechsRenderable = renderableLoader.loadRenderable("virusFuenfSechs")
        virusSechsRenderable.translateLocal(earthRenderable.getWorldPosition())
        virusSechsRenderable.translateLocal(Vector3f(-2.0f, -9f, 0.0f))
        virusFuenfRenderable.rotateLocal(0.0f, (Math.toRadians(90.0)).toFloat(), 0.0f)
        virusSechsRenderable.scaleLocal(Vector3f(4f))
        virusSechsRenderable.setCenterOffset(Vector3f(0f, 9f, 0f))

//VirusArray anlegen
        virusArray = arrayListOf(virusEinsRenderable, virusZweiRenderable, virusDreiRenderable, virusVierRenderable, virusFuenfRenderable, virusSechsRenderable)

//AliveArray anlegen
        for (v in virusArray) {
            aliveArray.add(true)
        }

//Spritzen######################################################################################################

        for (i in 0..5) {
            var spritzeRenderable = ModelLoader.loadModel(
                    "assets/models_textures/modelloader/syringe.obj",
                    Math.toRadians(-90.0f),
                    Math.toRadians(0.0f),
                    Math.toRadians(0f))
                    ?: throw IllegalArgumentException("Could not load the model")
            spritzeRenderable.parent = camera
            spritzeRenderable.translateGlobal(Vector3f(-0.21f + i * 0.06f, -0.25f, -0.4f))
            spritzeRenderable.scaleLocal(Vector3f(0.05f))

            spritzenMunition.add(spritzeRenderable)
            fliegendeSpritzen.add(false)
            unsichtbareSpritzen.add(false)
            spritzenModelMatrizen.add(spritzeRenderable.getLocalModelMatrix()) //ursprungs modelMatrix im Verhältnis zur Kamera (köchermatrixarray?)///////////////////////////////////////
        }
        fly = false

//**************************************************************************************************************
//Lights
//**************************************************************************************************************

//Point Light (position/color)
        pointLightColor = Vector3f(0.4f)
        sunPointLight = PointLight(Vector3f(0.0f, 0.0f, 0.0f), Vector3f(pointLightColor))
    }


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// R E N D E R
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        fun render(dt: Float, t: Float) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            //Farben********************************************************************************************************

            //verschobene und zusätzlich absolute Sinuskurven = keine Schwarzblenden
            var r: Float = abs(Math.sin(t * 3 + ((Math.PI.toFloat()))))
            var g: Float = abs(Math.sin(t * 3 + (Math.PI.toFloat() / 3)))
            var b: Float = abs(Math.sin(t * 3 + (Math.PI.toFloat() * 2 / 3)))
            val changingColor = Vector3f(r, g, b)

            val leuchten = Vector3f(1.0f)
            val nichtLeuchten = Vector3f(0.0f)
            val rot = Vector3f(1.0f, 0.0f, 0.0f)
            val grün = Vector3f(0.0f, 1.0f, 0.0f)
            val blau = Vector3f(0.0f, 0.0f, 1.0f)


            //Shader********************************************************************************************************

            //skyBoxShader (use, bind)#####################################
            skyBoxShader.use()
            camera.bind(skyBoxShader)
            //skyBoxColor wenn Viren aktiv 0.5-0.5-1, wenn Viren tot 3-2-5
            skyBoxShader.setUniform("emissive", Vector3f(skyBoxColor))//****
            backRenderable.render(skyBoxShader)
            bottomRenderable.render(skyBoxShader)
            frontRenderable.render(skyBoxShader)
            leftRenderable.render(skyBoxShader)
            rightRenderable.render(skyBoxShader)
            topRenderable.render(skyBoxShader)


            //variableShader (use, bind)####################################
            shader.use()
            camera.bind(shader)
            sunPointLight.bind(shader, "sunPoint")

            shader.setUniform("emissive", nichtLeuchten)//*********
            merkurRenderable.render(shader)
            venusRenderable.render(shader)
            earthRenderable.render(shader)
            marsRenderable.render(shader)
            jupiterRenderable.render(shader)
            saturnRenderable.render(shader)
            uranusRenderable.render(shader)
            neptunRenderable.render(shader)
            erdMondRenderable.render(shader)
            jupiter1MondRenderable.render(shader)
            jupiter2MondRenderable.render(shader)
            statuetteRenderable.render(shader)
            //statuetteRenderableNormals.render(shader)

            shader.setUniform("emissive", Vector3f(0.4f))//******
            //SpritzenMunition rendern
            for (i in 0..(fliegendeSpritzen.size - 1)) {
                if (!unsichtbareSpritzen[i]) {
                    spritzenMunition[i].render(shader)
                }
            }
            //sun emissive wenn tot 0-0-1, wenn gerettet 1-1-1
            shader.setUniform("emissive", emissiveSun)//************
            sunRenderable.render(shader)

            //shader.setUniform("emissive", Vector3f(nichtLeuchten))
            //apricotRenderable.render(shader)


            //pochenShader (use, bind)##################################################
            pochenShader.use()
            camera.bind(pochenShader)

            pochenShader.setUniform("time", glfwGetTime().toFloat())//*********************
            pochenShader.setUniform("emissive", Vector3f(0.1f, 0.4f, 0.1f))//******

            //VirusArray rendern
            for (i in 0..(virusArray.size - 5)) {
                if (aliveArray[i]) {
                    virusArray[i].render(pochenShader)
                }
            }
            pochenShader.setUniform("emissive", Vector3f(0.2f, 0.2f, 0.2f))
            //youSavedUniverse
            if (youSavedUniverseShowYourself) {
                youSavedUniverseRenderable.render(shader)
            }


            //pulsierenShader (use, bind)################################################################################
            pulsierenShader.use()
            camera.bind(pulsierenShader)
            pulsierenShader.setUniform("time", glfwGetTime().toFloat())
            pulsierenShader.setUniform("emissive", Vector3f(0.1f, 0.1f, 0.4f))

            //VirusArray rendern
            for (i in 4..(virusArray.size - 1)) {
                if (aliveArray[i]) {
                    virusArray[i].render(shader)
                }
            }

            //pulsierenZweiShader (use, bind)#########################################################################
            pulsierenZweiShader.use()
            camera.bind(pulsierenZweiShader)

            pulsierenZweiShader.setUniform("time", glfwGetTime().toFloat())//*************
            pulsierenZweiShader.setUniform("emissive", Vector3f(0.4f, 0.1f, 0.1f))//**************

            //VirusArray rendern
            for (i in 2..(virusArray.size - 3)) {
                if (aliveArray[i]) {
                    virusArray[i].render(pulsierenZweiShader)
                }
            }
        }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// U P D A T E
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        fun update(dt: Float, t: Float) {

            // Shader-Wechsel ######################################

            if (window.getKeyStatePress(GLFW.GLFW_KEY_B)) {
                shader = phongShader
            }
            if (window.getKeyStatePress(GLFW.GLFW_KEY_T)) {
                shader = toonShader
            }


            //Spiel #########################################################

            // Spritzen- und Viren-Lebenszyklus //

            val toleranz = 3.0f

            var maxVirenDistanz = 0.0f

            for (i in 0..(virusArray.size - 1)) {
                val wahreVirenposition = virusArray[i].getWorldPosition().add(virusArray[i].getCenterOffset())
                maxVirenDistanz = Math.max(maxVirenDistanz, camera.getWorldPosition().sub(
                        wahreVirenposition)
                        .length().absoluteValue)
            }
            for (i in 0..(virusArray.size - 1)) {
                for (j in 0..(fliegendeSpritzen.size - 1)) {

                    val wahreVirenposition = virusArray[i].getWorldPosition().add(virusArray[i].getCenterOffset())
                    val distanzSpritzeZuVirus = spritzenMunition[j].getWorldPosition().sub(
                            wahreVirenposition)
                            .length().absoluteValue

                    //println("Distanz Spritze " + j + " zum Virus " + i + ": " + distanzSpritzeZuVirus);

                    if // Treffer
                            (aliveArray[i] &&                               //WENN **DAS ENTSPRECHENDE** alive true ist (**ENTSPRECHENDES** Virus lebt)
                            fliegendeSpritzen[j] &&                         //UND die Spritze im Spritzenarray in die definierte Nähe des **ENTSPRECHENDEN** Virus kommt
                            distanzSpritzeZuVirus < toleranz
                    ) {
                        //println("Visurs " + i + " getroffen bei Entfernung von " + distanzSpritzeZuVirus + " durch Spritze " + j);
                        aliveArray[i] = false                                //DANN wird **DER ENTSPRECHENDE** alive false gesetzt (** ENTSPRECHENDES** Virus stirbt) UND
                        fliegendeSpritzen[j] = false                         //UND die Spritze stoppt den Flug
                        unsichtbareSpritzen[j] = true                        //UND die Spritze wird unsichtbar
                    } else { // Vorbei geflogen
                        if (fliegendeSpritzen[j] &&                             //WENN die Spritze fliegt
                                spritzenMunition[j].getWorldPosition().sub(     //UND die Spritze 2 * so weit von der camera entfernt ist, wie das **ENTSPRECHENDEN**Virus
                                        camera.getWorldPosition())
                                        .length().absoluteValue >
                                1.1 * maxVirenDistanz
                        ) {
                            fliegendeSpritzen[j] = false                      //dann stoppt die Spritze den Flug
                            unsichtbareSpritzen[j] = true                     //UND die Spritze wird unsichtbar
                        }
                    }
                }
            }

            // Spritzenflug //

            for (i in 0..(fliegendeSpritzen.size - 1)) {
                if (fliegendeSpritzen[i]) {
                    spritzenMunition[i].translateLocal(Vector3f(0.0f, 0f, -0.4f * t))
                }
            }

            //Rotationen
            //wenn ALLE Viren tot: Rotationen starten und Sonne leuchtet

            var toteViren = 0
            for (i in 0..(aliveArray.size - 1)) {
                if (aliveArray[i] == true) {
                    break
                } else {
                    toteViren += 1
                }
            }

            if (toteViren == 6) {
                //Sonne leuchtet
                emissiveSun = Vector3f(1.0f)
                pointLightColor = Vector3f(1f)
                sunPointLight = PointLight(Vector3f(0.0f, 0.0f, 0.0f), Vector3f(pointLightColor))
                skyBoxColor = Vector3f(3f, 2f, 5f)

                //YOUSAVEDTHEUNIVERSE
                statuette2ShowYourself = true
                youSavedUniverseShowYourself = true

                //Rotationen****************************************************************************************************
                /**Drehung um sich selbst ####################################################################################*/
                var rotationsGeschwindigkeit = 0.001f

                sunRenderable.rotateLocal(0.0f, rotationsGeschwindigkeit * 1, 0.0f)
                merkurRenderable.rotateLocal(0.0f, rotationsGeschwindigkeit * 5.8f, 0.0f)
                earthRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 5.5f, 0.0f)
                marsRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 5, 0.0f)
                jupiterRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 4.6f, 0.0f)
                saturnRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 4, 0.0f)
                uranusRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 3, 0.0f)
                neptunRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 2, 0.0f)
                erdMondRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit, 0.0f)
                jupiter1MondRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 1.8f, 0.0f)
                jupiter2MondRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 1.2f, 0.0f)
                statuetteRenderable.rotateLocal(0.00f, rotationsGeschwindigkeit * 10f, 0.0f)
                statuette2Renderable.translateGlobal(Vector3f(0.025f, 0.0f, 0.00f))

                /**Drehung um die Sonne ######################################################################################*/
                var umkreisungsGeschwindigkeit = 0.006f

                merkurRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 1, 0.0f, Vector3f(0.0f))
                venusRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 2, 0.00f, Vector3f(0.0f))
                earthRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 4, 0.00f, Vector3f(0.0f))
                marsRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 6, 0.0f, Vector3f(0.0f))
                jupiterRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 8, 0.0f, Vector3f(0.0f))
                saturnRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 10, 0.00f, Vector3f(0.0f))
                uranusRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 14, 0.00f, Vector3f(0.0f))
                neptunRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 18, 0.0f, Vector3f(0.0f))
                statuetteRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit / 2, 0.0f, Vector3f(0.0f))

                /**Drehung um einen Planeten##################################################################################*/
                erdMondRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit * 5, 0.0f, Vector3f(0.0f))
                jupiter1MondRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit * 1.2f, 0.0f, Vector3f(0.0f))
                jupiter2MondRenderable.rotateAroundPoint(0.0f, umkreisungsGeschwindigkeit * 2, 0.0f, Vector3f(0.0f))

            }

// S T E U E R U N G //

//Rotate: Keys Q/W alternativ on onMouseMove
//Move: Keys S/A
//shoot: Key 1 alternativ MouseButton left
//reload: Key 2 alternativ MouseButton right

//Q = rotate left
            if (window.getKeyStatePress(GLFW.GLFW_KEY_Q)) {
                camera.rotateLocal(0.0f, 0.4f * Math.PI.toFloat() * dt, 0.0f)
            }
//W = rotate right
            if (window.getKeyStatePress(GLFW.GLFW_KEY_W)) {
                camera.rotateLocal(0.0f, -0.4f * Math.PI.toFloat() * dt, 0.0f)
            }
//S = move in
            if (window.getKeyStatePress(GLFW.GLFW_KEY_S)) {
                camera.translateLocal(Vector3f(0.0f, 0.0f, -6.0f * dt))
            }
//A = move out
            if (window.getKeyStatePress(GLFW.GLFW_KEY_A)) {
                camera.translateLocal(Vector3f(0.0f, 0.0f, 6.0f * dt))
            }
        }

//******************************************************************************************************************
// O N   K E Y
//******************************************************************************************************************

        fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
            //Spritzen abfeuern
            if (key == GLFW.GLFW_KEY_1 && action == GLFW.GLFW_PRESS) {

                for (i in 0..(fliegendeSpritzen.size - 1)) {

                    if (fliegendeSpritzen[i] == false && unsichtbareSpritzen[i] == false) {
                        //Spritzen fliegen in negative z-Richtung (s. Spritzenflug)
                        //Problem: Mit parent wird Spritzenflug von Kamerabewegung beeinflusst
                        //Lösung: Wenn Spritze fliegt, parent entfernen
                        //Problem: Ohne parent starten Spritzen in persönliche z Richtung
                        //Lösung: Position reseten (neu in Transformable: fun setModelMatrix)
                        val koecherMatrixVorFlug = spritzenMunition[i].getWorldModelMatrix()
                        spritzenMunition[i].setModelMatrix(koecherMatrixVorFlug)
                        spritzenMunition[i].parent = null
                        fliegendeSpritzen[i] = true
                        break
                    }
                }
            }
            //Spritzen nachladen
            //nur möglich, wenn alle Spritzen unsichtbar
            if (key == GLFW.GLFW_KEY_2 && action == GLFW.GLFW_PRESS) {
                var nachladen = true
                for (i in 0..(unsichtbareSpritzen.size - 1)) {
                    if (!unsichtbareSpritzen[i]) {
                        nachladen = false
                    }
                }
                if (nachladen) { //Spritzen werden resetet
                    for (i in 0..(spritzenMunition.size - 1)) {
                        unsichtbareSpritzen[i] = false
                        fliegendeSpritzen[i] = false
                        spritzenMunition[i].setModelMatrix(spritzenModelMatrizen[i])
                        spritzenMunition[i].parent = camera
                    }
                }
            }
        }

//******************************************************************************************************************
// O N   M O U S E   M O V E
//******************************************************************************************************************
        fun onMouseMove(xpos: Double, ypos: Double) {

            //xpos ist die Position, die von der Maus an die Funktion gesandt wird
            //changedXpos speichert diese Position, bevor die nächste hereinkommt (für Berechnung Differenz)

            //Schwenk rechts
            var differenz = xpos - changedXpos
            camera.rotateLocal(0.0f, -0.001f * differenz.toFloat(), 0.0f)
            changedXpos = xpos

            //Schwenk links
            var differenz2 = ypos - changedYpos
            camera.rotateLocal(0.0f, 0.001f * differenz2.toFloat(), 0.0f)
            changedYpos = ypos

        }

//******************************************************************************************************************
// O N   M O U S E   B U T T O N
//******************************************************************************************************************

        fun onMouseButton(button: Int, action: Int, mode: Int) {
            //Spritzen abfeuern
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {

                for (i in 0..(fliegendeSpritzen.size - 1)) {

                    if (fliegendeSpritzen[i] == false && unsichtbareSpritzen[i] == false) {
                        //Spritzen fliegen in negative z-Richtung (s. Spritzenflug)
                        //Problem 1: Mit parent wird Spritzenflug von Kamerabewegung beeinflusst
                        //Lösung: Wenn Spritze fliegt, parent entfernen
                        //Problem 2: Ohne parent starten Spritzen in ihre persönliche z Richtung
                        //Lösung: wenn im Köcher, Position reseten (neu in Transformable: fun setModelMatrix)
                        val koecherMatrix = spritzenMunition[i].getWorldModelMatrix()
                        spritzenMunition[i].setModelMatrix(koecherMatrix)
                        spritzenMunition[i].parent = null
                        fliegendeSpritzen[i] = true
                        break
                    }
                }
            }
            //Spritzen nachladen
            //nur möglich, wenn alle Spritzen unsichtbar
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
                var nachladen = true
                for (i in 0..(unsichtbareSpritzen.size - 1)) {
                    if (!unsichtbareSpritzen[i]) {
                        nachladen = false
                    }
                }
                if (nachladen) { //Spritzen werden resetet
                    for (i in 0..(spritzenMunition.size - 1)) {
                        unsichtbareSpritzen[i] = false
                        fliegendeSpritzen[i] = false
                        spritzenMunition[i].setModelMatrix(spritzenModelMatrizen[i]) //
                        spritzenMunition[i].parent = camera
                    }
                }
            }
        }

//******************************************************************************************************************
// O N   M O U S E   S C R O L L
//******************************************************************************************************************

        //hinzu
        fun onMouseScroll(xoffset: Double, yoffset: Double) {

            //Kipp nach vorn/hinten
            //camera.rotateLocal(-0.04f * yoffset.toFloat(), 0.0f, 0.0f)

        }

        fun cleanup() {}

    }



