package hu.appropati.szunyog.gui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.GuiElement;
import hu.appropati.szunyog.input.GdxInputHandler;
import hu.appropati.szunyog.input.InputHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GuiButton implements GuiElement, InputHandler {
    private GdxInputHandler inputHandler;
    private TextRenderer textRenderer;

    @Setter @Getter
    private float x;

    @Setter @Getter
    private float y;

    @Getter
    private float width;

    @Getter
    private final float height;

    @Setter
    private String text;

    @Setter
    private Texture icon = null;

    private final Style style;

    private Rectangle bounds;
    private Vector2 textSize;
    private Vector2 imageSize;

    private NinePatch normalTexture;
    private NinePatch hoverTexture;

    private Consumer<Boolean> onClick;
    private boolean touching = false;

    @Getter @Setter
    private boolean visible = true;

    @Getter @Setter
    private boolean catchBackKey = false;

    public GuiButton(float x, float y, float width, float height, String text, Style style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.style = style;

        this.bounds = new Rectangle(x, y, width, height);
    }

    public GuiButton(float x, float y, float width, float height, String text, Style style, Texture icon) {
        this(x, y, width, height, text, style);

        this.icon = icon;
    }

    @Override
    public void create() {
        Trainer trainer = Trainer.getTrainer();

        inputHandler = trainer.getInputHandler();
        inputHandler.addInputHandler(this);

        textRenderer = trainer.getTextRenderer();
        textSize = textRenderer.getTextSize(text, style.fontName, style.fontStyle, style.fontSize);

        TextureManager textureManager = trainer.getTextureManager();
        normalTexture = new NinePatch(textureManager.getTexture("gui/button_normal.png"), 40, 40, 40, 40);
        hoverTexture = new NinePatch(textureManager.getTexture("gui/button_hover.png"), 40, 40, 40, 40);

        updateIcon();
    }

    private void updateIcon() {
        if(icon != null) {
            float scale;
            imageSize = new Vector2();

            if(width > height) {
                scale = icon.getWidth() / icon.getHeight();

                imageSize.y = height / 2 + 5;
                imageSize.x = imageSize.y * scale;
            } else {
                scale = icon.getHeight() / icon.getWidth();

                imageSize.x = width / 2 + 5;
                imageSize.y = imageSize.x * scale;
            }
        }
    }

    @Override
    public void destroy() {
        inputHandler.removeInputHandler(this);
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        if(!visible) {
            return;
        }

        bounds = new Rectangle(x, y, width, height);

        if(touching) {
            hoverTexture.draw(spriteBatch, x, y, width, height);
        } else {
            normalTexture.draw(spriteBatch, x, y, width, height);
        }

        textRenderer.drawCenteredText(text, x + width / 2, y + height / 2, style.fontSize, style.fontName, style.fontStyle, style.fontColor);

        if(icon != null) {
            Vector2 iconPosition = new Vector2();
            if(textSize.x > 0) {
                iconPosition.set(x + 20, y + height / 2 - imageSize.y / 2);
            } else {
                iconPosition.set(x + width / 2 - imageSize.x / 2, y + height / 2 - imageSize.y / 2);
            }

            spriteBatch.draw(icon, iconPosition.x, iconPosition.y, imageSize.x, imageSize.y);
        }

        if(catchBackKey && Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            onClick.accept(false);
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(!bounds.contains(x, y) || onClick == null || !visible) {
            return false;
        }

        onClick.accept(false);

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        if(!bounds.contains(x, y) || onClick == null || !visible) {
            return false;
        }

        onClick.accept(true);

        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if(!bounds.contains(x, y) || !visible) {
            return false;
        }

        touching = true;

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        touching = false;

        return true;
    }

    public void onClick(Consumer<Boolean> clickHandler) {
        onClick = clickHandler;
    }

    @Builder
    @Getter
    public static class Style {
        @Builder.Default private String fontName = "Niramit";
        @Builder.Default private int fontSize = 26;
        @Builder.Default private FontStyle fontStyle = FontStyle.NORMAL;

        @Builder.Default private Color fontColor = Color.BLACK;
    }
}
