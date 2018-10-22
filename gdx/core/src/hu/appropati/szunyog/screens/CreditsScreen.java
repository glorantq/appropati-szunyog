package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.GuiScreen;
import hu.appropati.szunyog.input.InputHandler;

public class CreditsScreen extends GuiScreen implements InputHandler {
    private Viewport viewport;
    private TextRenderer textRenderer;

    private Texture backgroundTexture;
    private Vector2 backgroundSize;

    private String creditsText = "Jelentem alássan cikis ügy, de nincs meg a file amiben ennek a szövegnek lennie kellene";
    private float textY = 0;

    @Override
    public void show() {
        Trainer trainer = Trainer.getTrainer();
        TextureManager textureManager = trainer.getTextureManager();

        viewport = trainer.getViewport();
        textRenderer = trainer.getTextRenderer();

        backgroundTexture = textureManager.getTexture("gui/background.jpg");

        trainer.setBackgroundMusic(trainer.getAssetManager().get("audio/music.ogg", Music.class));

        if(trainer.getPreferences().getBoolean("audio")) {
            trainer.playBackgroundMusic();
        }

        scaleBackground();

        FileHandle creditsFile = Gdx.files.internal("credits");
        if(creditsFile.exists()) {
            creditsText = creditsFile.readString("UTF-8");
        }

        trainer.getInputHandler().addInputHandler(this);
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(.3f, .3f, .3f, 1f);
        spriteBatch.draw(backgroundTexture, viewport.getWorldWidth() / 2 - backgroundSize.x / 2, viewport.getWorldHeight() / 2 - backgroundSize.y / 2, backgroundSize.x, backgroundSize.y);
        spriteBatch.setColor(1f, 1f, 1f, 1f);

        textY += 50 * Gdx.graphics.getDeltaTime();
        textRenderer.drawCenteredText("Appropati Szúnyogfitnesz", viewport.getWorldWidth() / 2, textY, 50, "Maiandra", FontStyle.BOLD, Color.WHITE);
        textRenderer.drawWrappedText(creditsText, 0, textY - 66, 32, "Maiandra", FontStyle.NORMAL, Color.WHITE, viewport.getWorldWidth(), Align.center);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Trainer.getTrainer().setScreen(new SettingsScreen());

        return true;
    }

    @Override
    public void hide() {
        Trainer.getTrainer().getInputHandler().removeInputHandler(this);
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
}
