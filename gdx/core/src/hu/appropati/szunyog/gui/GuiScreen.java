package hu.appropati.szunyog.gui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hu.appropati.szunyog.Trainer;

public abstract class GuiScreen implements Screen {

    @Override
    public void render(float delta) {
        draw(Trainer.getTrainer().getSpriteBatch());
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
}
