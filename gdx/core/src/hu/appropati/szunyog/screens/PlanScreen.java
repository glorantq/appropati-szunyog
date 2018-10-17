package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.gui.GuiScreen;

public class PlanScreen extends GuiScreen {
    private Texture texture;

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, 0, 0, Trainer.getTrainer().getViewport().getWorldWidth(), Trainer.getTrainer().getViewport().getWorldHeight());

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Trainer.getTrainer().setScreen(new MainScreen());
        }
    }

    @Override
    public void show() {
        texture = new Texture("gui/kalesz.png");
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {

    }
}
