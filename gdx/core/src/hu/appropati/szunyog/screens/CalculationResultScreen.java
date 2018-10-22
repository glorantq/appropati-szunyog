package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.elements.GuiButton;

public class CalculationResultScreen extends MenuScreen {
    private Trainer trainer = Trainer.getTrainer();
    private Viewport viewport;
    private TextRenderer textRenderer;
    private TextureManager textureManager;

    private Texture clockTexture;
    private float clockSize;

    private final float startTime;
    private final float tripTime;
    private final float totalDistance;

    CalculationResultScreen(float startTime, float tripTime, float totalDistance) {
        this.startTime = startTime;
        this.tripTime = tripTime;
        this.totalDistance = totalDistance;
    }

    @Override
    public void show() {
        super.show();

        textureManager = trainer.getTextureManager();
        clockTexture = textureManager.getTexture("gui/clock.png");
        clockTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        viewport = trainer.getViewport();
        textRenderer = trainer.getTextRenderer();

        if (viewport.getWorldWidth() < viewport.getWorldHeight()) {
            clockSize = viewport.getWorldWidth() / 2 + 50;
        } else {
            clockSize = viewport.getWorldHeight() / 2 + 50;
        }

        GuiButton menuButton = new GuiButton(viewport.getWorldWidth() - 135, 5, 130, 75, "Vissza", GuiButton.Style.builder().build());
        menuButton.onClick((longPress) -> trainer.setScreen(new MainScreen()));
        menuButton.setCatchBackKey(true);

        createElement(menuButton);
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        drawBackground(spriteBatch);
        drawElements(spriteBatch);

        spriteBatch.draw(clockTexture, viewport.getWorldWidth() / 2 - clockSize / 2, viewport.getWorldHeight() - clockSize - 60, clockSize, clockSize);

        textRenderer.drawText("Útidő:", 10, viewport.getWorldHeight() / 2 - 72 / 2, 72, "Niramit", FontStyle.BOLD, Color.WHITE, true);
        textRenderer.drawText("Start:", 10, viewport.getWorldHeight() / 2 + 72, 72, "Niramit", FontStyle.BOLD, Color.WHITE, true);

        textRenderer.drawRightText(formatTime(tripTime), viewport.getWorldWidth() - 10, viewport.getWorldHeight() / 2 - 72 / 2, 72, "Niramit", FontStyle.NORMAL, Color.WHITE, true);
        textRenderer.drawRightText(formatTime(startTime), viewport.getWorldWidth() - 10, viewport.getWorldHeight() / 2 + 72, 72, "Niramit", FontStyle.NORMAL, Color.WHITE, true);

        spriteBatch.draw(textureManager.getWhite(), 10, viewport.getWorldHeight() / 2 - 72 / 2 - 15, viewport.getWorldWidth() - 20, 10);

        textRenderer.drawText("Megtett út:", 10, viewport.getWorldHeight() / 2 - 72 - 2 - 20, 60, "Niramit", FontStyle.BOLD, Color.WHITE, false);
        textRenderer.drawRightText(formatDistance(totalDistance) + " m", viewport.getWorldWidth() - 10, viewport.getWorldHeight() / 2 - 72 - 2 - 20, 60, "Niramit", FontStyle.NORMAL, Color.WHITE, false);
    }

    private String formatTime(float seconds) {
        int secondsInt = Math.round(seconds);

        int minutes = (int) (seconds - (secondsInt % 60)) / 60;
        seconds = seconds - minutes * 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, (int) seconds);
    }

    private String formatDistance(float distance) {
        distance = Math.round(distance * 100) / 100;

        return String.format(Locale.getDefault(), "%.2f", distance);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
