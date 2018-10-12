package hu.appropati.szunyog.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.dialog.DialogBox;

public class GdxInputHandler implements GestureDetector.GestureListener, InputProcessor {
    private final Viewport viewport = Trainer.getTrainer().getViewport();
    private final Trainer trainer = Trainer.getTrainer();
    private final List<InputHandler> inputHandlers = new CopyOnWriteArrayList<>();

    public void addInputHandler(InputHandler handler) {
        synchronized (inputHandlers) {
            inputHandlers.add(handler);
        }
    }

    public void removeInputHandler(InputHandler handler) {
        synchronized (inputHandlers) {
            inputHandlers.remove(handler);
        }
    }

    @Override
    public boolean tap(float v, float v1, int i, int i1) {
        Vector3 coordinates = viewport.unproject(new Vector3(v, v1, 0));

        synchronized (inputHandlers) {
            for(InputHandler handler : inputHandlers) {
                if(trainer.getCurrentDialogBox() != null && !(handler instanceof DialogBox)) {
                    continue;
                }

                handler.tap(coordinates.x, coordinates.y, i, i1);
            }
        }

        return false;
    }

    @Override
    public boolean longPress(float v, float v1) {
        Vector3 coordinates = viewport.unproject(new Vector3(v, v1, 0));

        synchronized (inputHandlers) {
            for(InputHandler handler : inputHandlers) {
                if(trainer.getCurrentDialogBox() != null && !(handler instanceof DialogBox)) {
                    continue;
                }

                handler.longPress(coordinates.x, coordinates.y);
            }
        }

        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        Vector3 coordinates = viewport.unproject(new Vector3(i, i1, 0));

        synchronized (inputHandlers) {
            for(InputHandler handler : inputHandlers) {
                if(trainer.getCurrentDialogBox() != null && !(handler instanceof DialogBox)) {
                    continue;
                }

                handler.touchDown((int) coordinates.x, (int) coordinates.y, i, i1);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        Vector3 coordinates = viewport.unproject(new Vector3(i, i1, 0));

        synchronized (inputHandlers) {
            for(InputHandler handler : inputHandlers) {
                if(trainer.getCurrentDialogBox() != null && !(handler instanceof DialogBox)) {
                    continue;
                }

                handler.touchUp((int) coordinates.x, (int) coordinates.y, i, i1);
            }
        }

        return false;
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    @Override
    public boolean fling(float v, float v1, int i) {
        return false;
    }

    @Override
    public boolean pan(float v, float v1, float v2, float v3) {
        return false;
    }

    @Override
    public boolean panStop(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean zoom(float v, float v1) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
