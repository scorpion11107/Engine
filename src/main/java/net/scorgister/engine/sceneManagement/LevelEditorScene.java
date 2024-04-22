package net.scorgister.engine.sceneManagement;

import net.scorgister.engine.KeyListener;
import net.scorgister.engine.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("In level editor");
    }

    @Override
    public void update(float dt) {

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            timeToChangeScene = 2.0f;
            changingScene = false;
            Window.changeScene(1);
        }
    }
}
