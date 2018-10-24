package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.GuiScreen;
import hu.appropati.szunyog.gui.elements.GuiButton;
import lombok.NoArgsConstructor;

public class CreditsScreen extends GuiScreen {
    private Viewport viewport;
    private TextRenderer textRenderer;

    private Texture backgroundTexture;
    private Vector2 backgroundSize;

    private LinkedList<Paragraph> data = new LinkedList<>();
    private List<EasterEggHolder> easterEggData = new ArrayList<>();

    private float headerY = 0;
    private float emailMessageY = 0;

    @Override
    public void show() {
        Trainer trainer = Trainer.getTrainer();
        TextureManager textureManager = trainer.getTextureManager();

        viewport = trainer.getViewport();
        textRenderer = trainer.getTextRenderer();

        backgroundTexture = textureManager.getTexture("gui/background.jpg");

        scaleBackground();

        headerY = 0;

        float currentY = 0;
        for(Paragraph paragraph : data) {
            paragraph.setY(currentY);

            currentY -= paragraph.getTotalHeight() + 100;

            if(paragraph.twitterButton != null) {
                createElement(paragraph.twitterButton);
            }

            if(paragraph.instagramButton != null) {
                createElement(paragraph.instagramButton);
            }
        }

        float easterEggY = -4500;
        for(int i = 0; i < easterEggData.size(); i++) {
            EasterEggHolder holder = easterEggData.get(i);
            holder.y = easterEggY;
            easterEggY -= holder.textureSize.y + 50;
        }

        emailMessageY = easterEggY - 25;
    }

    void init() {
        Trainer trainer = Trainer.getTrainer();
        TextureManager textureManager = trainer.getTextureManager();

        FileHandle creditsFile = Gdx.files.internal("credits.json");

        if(creditsFile.exists()) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            List<Paragraph> tempData = gson.fromJson(creditsFile.readString(), new TypeToken<List<Paragraph>>(){}.getType());

            for(int i = 0; i < tempData.size(); i++) {
                Paragraph paragraph = tempData.get(i);
                paragraph.init();

                data.add(paragraph);
            }
        }

        for(int i = 0; i < 4; i++) {
            Texture texture = textureManager.getTexture("credits/easteregg/" + i + ".jpg");
            EasterEggHolder holder = new EasterEggHolder(texture, 0);
            easterEggData.add(holder);
        }
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        spriteBatch.setColor(.3f, .3f, .3f, 1f);
        spriteBatch.draw(backgroundTexture, viewport.getWorldWidth() / 2 - backgroundSize.x / 2, viewport.getWorldHeight() / 2 - backgroundSize.y / 2, backgroundSize.x, backgroundSize.y);
        spriteBatch.setColor(1f, 1f, 1f, 1f);

        float movement = 50 * Gdx.graphics.getDeltaTime();

        headerY += movement;
        emailMessageY += movement;

        textRenderer.drawCenteredText("Appropati Szúnyogfitnesz", viewport.getWorldWidth() / 2, headerY + 50, 46, "Maiandra", FontStyle.BOLD, Color.WHITE);

        drawElements(spriteBatch);

        data.forEach((it) -> it.adjustY(movement));
        data.forEach(Paragraph::drawText);

        easterEggData.forEach((it) -> it.adjustY(movement));
        easterEggData.forEach((it) -> it.draw(spriteBatch));

        textRenderer.drawWrappedText("Ha ezt látod, dobj nekünk egy e-mailt!\nappropatistudios@skacce.rs", 20, emailMessageY, 36, "Maiandra", FontStyle.NORMAL, Color.WHITE, viewport.getWorldWidth() - 40, Align.center);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Trainer.getTrainer().setScreen(new SettingsScreen());
        }
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

    @NoArgsConstructor
    private static class Paragraph {
        private static Texture twitterIcon;
        private static Texture instagramIcon;

        @Expose
        private String text;

        @Expose
        private String instagram;

        @Expose
        private String twitter;

        private Vector2 textSize;
        private float y;

        private GuiButton twitterButton;
        private GuiButton instagramButton;

        private void init() {
            TextRenderer textRenderer = Trainer.getTrainer().getTextRenderer();
            Viewport viewport = Trainer.getTrainer().getViewport();
            TextureManager textureManager = Trainer.getTrainer().getTextureManager();

            if(twitterIcon == null) {
                twitterIcon = textureManager.getTexture("credits/twitter.png");
            }

            if(instagramIcon == null) {
                instagramIcon = textureManager.getTexture("credits/instagram.png");
            }

            textSize = textRenderer.getWrappedTextSize(text, "Niramit", FontStyle.NORMAL, 26, viewport.getWorldWidth() - 20, Align.center);

            float buttonY = y - 110 - textSize.y;
            float buttonWidth = 335;

            if(instagram == null && twitter != null) {
                twitterButton = new GuiButton(viewport.getWorldWidth() / 2 - buttonWidth / 2, buttonY, buttonWidth, 72, "@" + twitter, GuiButton.Style.builder().build(), twitterIcon);
            } else if(instagram != null && twitter == null) {
                instagramButton = new GuiButton(viewport.getWorldWidth() / 2 - buttonWidth / 2, buttonY, buttonWidth, 72, "@" + instagram, GuiButton.Style.builder().build(), instagramIcon);
            } else if(instagram != null) {
                twitterButton = new GuiButton(viewport.getWorldWidth() / 2 - buttonWidth - 5, buttonY, buttonWidth, 72, "@" + twitter, GuiButton.Style.builder().build(), twitterIcon);
                instagramButton = new GuiButton(viewport.getWorldWidth() / 2 + 5, buttonY, buttonWidth, 72, "@" + instagram, GuiButton.Style.builder().build(), instagramIcon);
            }

            if(instagramButton != null) {
                instagramButton.onClick((longPress) -> Trainer.getTrainer().getPlatform().openURL("https://www.instagram.com/" + instagram));
            }

            if(twitterButton != null) {
                twitterButton.onClick((longPress) -> Trainer.getTrainer().getPlatform().openURL("https://www.twitter.com/" + twitter));
            }
        }

        private void setY(float y) {
            this.y = y;
            float buttonY = y - 110 - textSize.y;

            if(twitterButton != null) {
                twitterButton.setY(buttonY);
            }

            if(instagramButton != null) {
                instagramButton.setY(buttonY);
            }
        }

        private void adjustY(float y) {
            this.y += y;

            if(twitterButton != null) {
                twitterButton.setY(twitterButton.getY() + y);
            }

            if(instagramButton != null) {
                instagramButton.setY(instagramButton.getY() + y);
            }
        }

        private void drawText() {
            TextRenderer textRenderer = Trainer.getTrainer().getTextRenderer();
            Viewport viewport = Trainer.getTrainer().getViewport();

            textRenderer.drawWrappedText(text, 20, y, 26, "Niramit", FontStyle.NORMAL, Color.WHITE, viewport.getWorldWidth() - 40, Align.center);
        }

        private float getTotalHeight() {
            return (twitterButton != null || instagramButton != null ? 72 : 0) + 38 + textSize.y;
        }
    }

    private static class EasterEggHolder {
        private final Texture texture;

        private Vector2 textureSize;
        private float y;

        private EasterEggHolder(Texture texture, float y) {
            this.texture = texture;
            this.y = y;

            Viewport viewport = Trainer.getTrainer().getViewport();

            float newWidth = viewport.getWorldWidth() - 40;

            float widthRatio = newWidth / texture.getWidth();
            float heightRatio = viewport.getWorldHeight() / texture.getHeight();
            float ratio = Math.min(widthRatio, heightRatio);

            textureSize = new Vector2(ratio * texture.getWidth(), ratio * texture.getHeight());
        }

        private void draw(SpriteBatch spriteBatch) {
            Viewport viewport = Trainer.getTrainer().getViewport();

            spriteBatch.draw(texture, viewport.getWorldWidth() / 2 - textureSize.x / 2, y - textureSize.y, textureSize.x, textureSize.y);
        }

        private void adjustY(float y) {
            this.y += y;
        }
    }
}
