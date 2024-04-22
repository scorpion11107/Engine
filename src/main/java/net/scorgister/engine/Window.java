package net.scorgister.engine;

import net.scorgister.engine.sceneManagement.LevelEditorScene;
import net.scorgister.engine.sceneManagement.LevelScene;
import net.scorgister.engine.sceneManagement.Scene;
import net.scorgister.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    float a = 1.0f;

    private static Scene currentScene;

    private Window() {

        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static void changeScene(int newScene) {

        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                // currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                // currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {

        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {

        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Cleanup
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {

        // Setup err callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create GLFW window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create GLFW window.");
        }

        // Set events callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the GLFW window visible
        glfwShowWindow(glfwWindow);

        // Make OpenGL bindings available for LWJGL
        GL.createCapabilities();

        // Set current scene
        Window.changeScene(0);
    }

    public void loop() {

        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Get poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
