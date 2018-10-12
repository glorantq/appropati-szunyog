package hu.appropati.szunyog.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.gui.GuiElement;
import hu.appropati.szunyog.input.InputHandler;

public abstract class GuiDialog implements DialogBox, InputHandler {
    private List<GuiElement> guiElements = new ArrayList<>();

    @Override
    public void show() {
        Trainer.getTrainer().getInputHandler().addInputHandler(this);
    }

    protected void createElement(GuiElement element) {
        element.create();
        guiElements.add(element);
    }

    protected void drawElements(SpriteBatch spriteBatch) {
        guiElements.forEach((it) -> it.render(spriteBatch, Gdx.graphics.getDeltaTime()));
    }

    @Override
    public void close() {
        guiElements.forEach(GuiElement::destroy);
        Trainer.getTrainer().getInputHandler().removeInputHandler(this);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        guiElements.stream().filter((it) -> it instanceof InputHandler).map((it) -> (InputHandler) it).forEach((it) -> it.tap(x, y, count, button));

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        guiElements.stream().filter((it) -> it instanceof InputHandler).map((it) -> (InputHandler) it).forEach((it) -> it.longPress(x, y));

        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        guiElements.stream().filter((it) -> it instanceof InputHandler).map((it) -> (InputHandler) it).forEach((it) -> it.touchDown(x, y, pointer, button));

        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        guiElements.stream().filter((it) -> it instanceof InputHandler).map((it) -> (InputHandler) it).forEach((it) -> it.touchUp(x, y, pointer, button));

        return false;
    }
}
