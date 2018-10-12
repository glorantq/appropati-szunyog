package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.elements.GuiButton;

public class MainScreen extends MenuScreen {
    private Viewport viewport;
    private TextRenderer textRenderer;

    private GuiButton timeButton;
    private GuiButton distanceButton;

    @Override
    public void show() {
        Trainer trainer = Trainer.getTrainer();
        TextureManager textureManager = trainer.getTextureManager();

        viewport = trainer.getViewport();
        textRenderer = trainer.getTextRenderer();

        Vector2 buttonSize = new Vector2(250, 100);

        timeButton = new GuiButton(viewport.getWorldWidth() / 2 - buttonSize.x / 2,
                viewport.getWorldHeight() / 2 + 5,
                buttonSize.x, buttonSize.y, "Indulási Idő", GuiButton.Style.builder().build());

        distanceButton = new GuiButton(viewport.getWorldWidth() / 2 - buttonSize.x / 2,
                viewport.getWorldHeight() / 2 - buttonSize.y - 5,
                buttonSize.x, buttonSize.y, "Megtett Út", GuiButton.Style.builder().build());

        GuiButton settingsButton = new GuiButton(5, 5, 64, 64, "", GuiButton.Style.builder().build(), textureManager.getTexture("gui/settings.png"));

        createElement(timeButton);
        createElement(distanceButton);
        createElement(settingsButton);

        super.show();
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        drawElements(spriteBatch);
        textRenderer.drawRightText("Copyright © 2018, Appropati", viewport.getWorldWidth() - 5, 5, 22, "Roboto", FontStyle.NORMAL, Color.WHITE, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        timeButton.setX(viewport.getWorldWidth() / 2 - timeButton.getWidth() / 2);
        distanceButton.setX(viewport.getWorldWidth() / 2 - distanceButton.getWidth() / 2);

        timeButton.setY(viewport.getWorldHeight() / 2 + 5);
        distanceButton.setY(viewport.getWorldHeight() / 2 - distanceButton.getHeight() - 5);
    }
}
