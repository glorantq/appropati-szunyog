package hu.appropati.szunyog.gui.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.GuiElement;
import hu.appropati.szunyog.input.GdxInputHandler;
import hu.appropati.szunyog.input.InputHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * Szimpla jelölőnégyzet
 *
 * @see hu.appropati.szunyog.gui.GuiScreen
 * @see GuiElement
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
public class GuiCheckBox implements GuiElement, InputHandler {
    private GdxInputHandler inputHandler;
    private TextRenderer textRenderer;
    private Trainer trainer = Trainer.getTrainer();

    @Setter @Getter
    private float x;

    @Setter @Getter
    private float y;

    @Getter
    private final float width;

    @Getter
    private final float height;
    private final String text;

    private Rectangle checkBoxBounds;

    private NinePatch normalTexture;
    private Texture checkmarkTexture;
    private final GuiButton.Style style;

    @Getter @Setter
    private boolean checked = false;

    private Consumer<Boolean> onStateChange;

    public GuiCheckBox(float x, float y, float width, float height, String text, GuiButton.Style style) {
        this.inputHandler = trainer.getInputHandler();
        this.textRenderer = trainer.getTextRenderer();

        this.x = x;
        this.y = y;
        this.text = text;
        this.style = style;

        checkBoxBounds = new Rectangle((int) x, (int) y, (int) width, (int) height);

        Vector2 textSize = textRenderer.getTextSize(text, style.getFontName(), style.getFontStyle(), style.getFontSize());
        Rectangle totalBounds = new Rectangle((int) x, (int) y, (int) (width + 5 + textSize.x), (int) height);

        this.width = totalBounds.getWidth();
        this.height = totalBounds.getHeight();
    }

    @Override
    public void create() {
        TextureManager textureManager = trainer.getTextureManager();
        normalTexture = new NinePatch(textureManager.getTexture("gui/button_normal.png"), 40, 40, 40, 40);
        checkmarkTexture = textureManager.getTexture("gui/check.png");

        inputHandler.addInputHandler(this);
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        checkBoxBounds.setX(x);
        checkBoxBounds.setY(y);

        normalTexture.draw(spriteBatch, x, y, checkBoxBounds.width, checkBoxBounds.height);
        if(checked) {
            float checkMarkWidth = checkBoxBounds.getWidth() / 1.5f;
            float checkMarkHeight = checkBoxBounds.getHeight() / 1.5f;

            spriteBatch.draw(checkmarkTexture, x + checkBoxBounds.getWidth() / 2 - checkMarkWidth / 2, y + checkBoxBounds.getHeight() / 2 - checkMarkHeight / 2, checkMarkWidth, checkMarkHeight);
        }

        textRenderer.drawCenteredText(text, x + 10 + (int) (width + checkBoxBounds.getWidth()) / 2f, y + height / 2, style.getFontSize(), style.getFontName(), style.getFontStyle(), style.getFontColor());
    }

    @Override
    public void destroy() {
        inputHandler.removeInputHandler(this);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(checkBoxBounds.contains(x, y)) {
            checked = !checked;

            if(onStateChange != null) {
                onStateChange.accept(checked);
            }

            return true;
        }

        return false;
    }

    public void onStateChange(Consumer<Boolean> consumer) {
        onStateChange = consumer;
    }
}
