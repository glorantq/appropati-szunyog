package hu.appropati.szunyog.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

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

    protected Vector2 getMousePosWorld() {
        Vector3 mouse = Trainer.getTrainer().getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        return new Vector2(mouse.x, mouse.y);
    }

    @Override
    public void dispose() {
        guiElements.forEach(GuiElement::destroy);
    }
}
