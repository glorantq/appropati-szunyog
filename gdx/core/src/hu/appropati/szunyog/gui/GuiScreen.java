package hu.appropati.szunyog.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.appropati.szunyog.Trainer;

public abstract class GuiScreen implements Screen {
    public List<GuiElement> guiElements = new ArrayList<>();

    @Override
    public void render(float delta) {
        draw(Trainer.getTrainer().getSpriteBatch());
    }

    protected void createElement(GuiElement element) {
        element.create();
        guiElements.add(element);
    }

    protected void drawElements(SpriteBatch spriteBatch) {
        guiElements.forEach((it) -> it.render(spriteBatch, Gdx.graphics.getDeltaTime()));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    protected abstract void draw(SpriteBatch spriteBatch);

    @Override
    public void dispose() {
        guiElements.forEach(GuiElement::destroy);
    }
}
