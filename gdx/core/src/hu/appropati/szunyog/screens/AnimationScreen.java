package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Gdx;
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

public class AnimationScreen extends MenuScreen {
    private Trainer trainer = Trainer.getTrainer();
    private TextRenderer textRenderer;
    private Viewport viewport;

    private final float metersToPixels = 5f;

    private final float humanASpeed;
    private final float humanBSpeed;
    private float humansDistance;
    private final float flySpeed;
    private final float windSpeed;
    private final float startTime;

    private float totalTripTime = 0f;
    private float totalDistance = 0f;

    private float humanAX = 0f;
    private float humanBX = 0f;
    private float flyX = 0f;
    private float flyDirection = 1f;

    private Texture humanTexture;
    private Texture flyTexture;

    public AnimationScreen(float humanASpeed, float humanBSpeed, float humansDistance, float flySpeed, float windSpeed, float startTime) {
        this.humanASpeed = humanASpeed;
        this.humanBSpeed = humanBSpeed;
        this.humansDistance = humansDistance;
        this.flySpeed = flySpeed;
        this.windSpeed = windSpeed;
        this.startTime = startTime;
    }

    @Override
    public void show() {
        super.show();

        textRenderer = trainer.getTextRenderer();
        viewport = trainer.getViewport();

        TextureManager textureManager = trainer.getTextureManager();
        humanTexture = textureManager.getTexture("gui/human.png");
        flyTexture = textureManager.getTexture("gui/fly.png");

        humanAX = viewport.getWorldWidth() / 2 - (humansDistance / 2) * metersToPixels;
        humanBX = viewport.getWorldWidth() / 2 + (humansDistance / 2) * metersToPixels;

        flyX = humanAX;

        GuiButton menuButton = new GuiButton(viewport.getWorldWidth() - 135, 5, 130, 75, "Vissza", GuiButton.Style.builder().build());
        menuButton.onClick((longPress) -> trainer.setScreen(new MainScreen()));

        createElement(menuButton);
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);

        drawElements(spriteBatch);

        float textBaseY = 125;

        textRenderer.drawText("Távolság:", 5, textBaseY, 60, "Niramit", FontStyle.BOLD, Color.WHITE, true);
        textRenderer.drawText("Menetidő:", 5, textBaseY + 65, 60,"Niramit", FontStyle.BOLD, Color.WHITE, true);
        textRenderer.drawText("Start:", 5, textBaseY + 130, 60, "Niramit", FontStyle.BOLD, Color.WHITE, true);

        textRenderer.drawRightText(formatDistance(totalDistance) + " m", viewport.getWorldWidth() - 5, textBaseY, 60, "Niramit", FontStyle.NORMAL, Color.WHITE, true);
        textRenderer.drawRightText(formatTime(Math.max(0, totalTripTime - startTime)), viewport.getWorldWidth() - 5, textBaseY + 65, 60, "Niramit", FontStyle.NORMAL, Color.WHITE, true);
        textRenderer.drawRightText(formatTime(startTime), viewport.getWorldWidth() - 5, textBaseY + 130, 60, "Niramit", FontStyle.NORMAL, Color.WHITE, true);

        if(humansDistance > 0) {
            update(Gdx.graphics.getDeltaTime());
        }

        spriteBatch.draw(humanTexture, humanAX - humanTexture.getWidth(), viewport.getWorldHeight() / 2);
        spriteBatch.draw(humanTexture, humanBX, viewport.getWorldHeight() / 2);

        spriteBatch.draw(flyTexture, flyX - flyTexture.getWidth() / 2, viewport.getWorldHeight() / 2 + humanTexture.getHeight() / 2 - flyTexture.getHeight() / 2);
    }

    private void update(float delta) {
        float humanAMovement = humanASpeed * metersToPixels * delta;
        float humanBMovement = -humanBSpeed * metersToPixels * delta;

        if(startTime < totalTripTime) {
            float relativeWindSpeed = windSpeed * flyDirection;

            if(flyX >= humanBX) {
                flyDirection = -1;
            }

            if(flyX <= humanAX) {
                flyDirection = 1;
            }

            float flyMovement = (flySpeed + relativeWindSpeed) * metersToPixels * flyDirection * delta;

            flyX += flyMovement;

            totalDistance += Math.abs(flyMovement) / metersToPixels;
        } else {
            flyX += humanAMovement;
        }

        humanAX += humanAMovement;
        humanBX += humanBMovement;

        humansDistance = humanBX - humanAX;

        totalTripTime += delta;
    }

    @Override
    public void hide() {
        super.hide();
    }

    private String formatTime(float seconds) {
        int secondsInt = Math.round(seconds);

        int minutes = (int) (seconds - (secondsInt % 60)) / 60;
        seconds = seconds - minutes * 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, (int) seconds);
    }

    private String formatDistance(float distance) {
        //distance = Math.round(distance * 100) / 100;

        return String.format(Locale.getDefault(), "%.2f", distance);
    }

}
