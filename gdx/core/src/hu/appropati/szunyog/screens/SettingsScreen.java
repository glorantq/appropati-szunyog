package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.elements.GuiButton;
import hu.appropati.szunyog.gui.elements.GuiCheckBox;

public class SettingsScreen extends MenuScreen {
    private Trainer trainer = Trainer.getTrainer();
    private TextRenderer textRenderer;
    private Viewport viewport;

    private GuiCheckBox audioCheckBox;
    private GuiButton precisionButton;

    private Preferences preferences;

    @Override
    public void show() {
        super.show();

        textRenderer = trainer.getTextRenderer();
        viewport = trainer.getViewport();
        preferences = trainer.getPreferences();

        GuiButton menuButton = new GuiButton(viewport.getWorldWidth() - 130, 5, 130, 75, "Vissza", GuiButton.Style.builder().build());
        menuButton.onClick((longPress) -> trainer.setScreen(new MainScreen()));
        menuButton.setCatchBackKey(true);

        audioCheckBox = new GuiCheckBox(viewport.getWorldWidth() - 45 - 72, viewport.getWorldHeight() - logoTexture.getHeight() - 187, 72, 72, "", GuiButton.Style.builder().build());
        precisionButton = new GuiButton(viewport.getWorldWidth() - 45 - 130, audioCheckBox.getY() - 72 - 15, 130, 72, "Nagy", GuiButton.Style.builder().build());

        GuiButton creditsButton = new GuiButton(viewport.getWorldWidth() / 2 - 125, precisionButton.getY() - 95, 250, 80, "Készítők", GuiButton.Style.builder().build());
        creditsButton.onClick((longPress) -> trainer.setScreen(trainer.getCreditsScreen()));

        audioCheckBox.setChecked(preferences.getBoolean("audio"));

        audioCheckBox.onStateChange((checked) -> {
            preferences.putBoolean("audio", checked);
            preferences.flush();

            if (checked) {
                trainer.playBackgroundMusic();
            } else {
                trainer.stopBackgroundMusic();
            }
        });

        precisionButton.setText(getTextForPrecision(preferences.getInteger("precision")));

        precisionButton.onClick((longPress) -> {
            int currentPrecision = preferences.getInteger("precision");
            int newPrecision;

            if(currentPrecision == 100) {
               newPrecision = 1000;
            } else if(currentPrecision == 1000) {
                newPrecision = 5000;
            } else if(currentPrecision == 5000) {
                newPrecision = 10000;
            } else {
                newPrecision = 100;
            }

            preferences.putInteger("precision", newPrecision);
            preferences.flush();

            precisionButton.setText(getTextForPrecision(newPrecision));
        });

        createElement(menuButton);
        createElement(audioCheckBox);
        createElement(precisionButton);
        createElement(creditsButton);
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        drawElements(spriteBatch);

        textRenderer.drawCenteredText("Beállítások", viewport.getWorldWidth() / 2, viewport.getWorldHeight() - logoTexture.getHeight() - 55, 60, "Maiandra", FontStyle.NORMAL, Color.WHITE);

        textRenderer.drawText("Zene és egyéb hangok:", 45, audioCheckBox.getY() + audioCheckBox.getHeight() / 2 - 20, 40, "Maiandra", FontStyle.BOLD, Color.WHITE, true);
        textRenderer.drawText("Számolások pontossága:", 45, precisionButton.getY() + precisionButton.getHeight() / 2 - 20, 40, "Maiandra", FontStyle.BOLD, Color.WHITE, true);
    }

    private String getTextForPrecision(int precision) {
        switch (precision) {
            case 100:
                return "Alacsony";

            case 1000:
                return "Átlagos";

            case 5000:
                return "Nagy";

            case 10000:
                return "Ultra";

            default:
                return "Game :b:roke";
        }
    }

    @Override
    public void hide() {
        super.hide();
    }
}
