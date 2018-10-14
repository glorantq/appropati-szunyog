package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.gui.GuiScreen;

public class MenuScreen extends GuiScreen {
    private Viewport viewport;

    private Texture backgroundTexture;
    private Vector2 backgroundSize;

    private Texture logoTexture;

    @Override
    public void show() {
        Trainer trainer = Trainer.getTrainer();
        TextureManager textureManager = trainer.getTextureManager();

        viewport = trainer.getViewport();

        logoTexture = textureManager.getTexture("gui/logo.png");
        backgroundTexture = textureManager.getTexture("gui/background.jpg");

        scaleBackground();
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(.7f, .7f, .7f, 1f);
        spriteBatch.draw(backgroundTexture, viewport.getWorldWidth() / 2 - backgroundSize.x / 2, viewport.getWorldHeight() / 2 - backgroundSize.y / 2, backgroundSize.x, backgroundSize.y);
        spriteBatch.setColor(1f, 1f, 1f, 1f);
        spriteBatch.draw(logoTexture, viewport.getWorldWidth() / 2 - logoTexture.getWidth() / 2, viewport.getWorldHeight() - logoTexture.getHeight() - 5);
    }

    @Override
    public void hide() {

    }

    private void scaleBackground() {
        backgroundSize = new Vector2(backgroundTexture.getWidth(), backgroundTexture.getHeight());

        if(viewport.getWorldWidth() > backgroundTexture.getWidth()) {
            float ratio = viewport.getWorldWidth() / backgroundTexture.getWidth();
            backgroundSize.scl(ratio);
        } else if(viewport.getWorldHeight() > backgroundTexture.getHeight()) {
            float ratio = viewport.getWorldHeight() / backgroundTexture.getHeight();
            backgroundSize.scl(ratio);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        scaleBackground();
    }
}
