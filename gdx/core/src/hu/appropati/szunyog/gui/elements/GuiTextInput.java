package hu.appropati.szunyog.gui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.GuiElement;
import hu.appropati.szunyog.gui.GuiScreen;
import hu.appropati.szunyog.input.GdxInputHandler;
import hu.appropati.szunyog.input.InputHandler;
import hu.appropati.szunyog.input.text.TextInputListener;
import hu.appropati.szunyog.input.text.TextInputProvider;
import lombok.Getter;
import lombok.Setter;

/**
 * Szöveg vagy számbevitelre alkalmas mező
 *
 * @see hu.appropati.szunyog.gui.GuiScreen
 * @see GuiElement
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
public class GuiTextInput implements GuiElement, InputHandler {
    private static boolean registeredListener = false;
    private static GuiTextInput focusedElement = null;

    private static class StaticTextInputHandler implements TextInputListener {

        @Override
        public void textUpdated(String text) {
            if (focusedElement != null) {
                focusedElement.updateText(text);
            }
        }
    }

    private GdxInputHandler inputHandler;
    private TextRenderer textRenderer;
    private TextInputProvider textInputProvider;

    @Setter @Getter
    private float x;

    @Setter @Getter
    private float y;

    @Getter
    private final float width;

    @Getter
    private final float height;

    private final GuiScreen parent;

    @Getter @Setter
    private String text = "";

    private final String placeholder;

    @Getter @Setter
    private String suffix = "";

    private Rectangle bounds;

    private NinePatch normalTexture;

    private final TextInputProvider.InputType type;
    private final int maxChars;

    @Getter @Setter
    private boolean visible = true;

    public GuiTextInput(float x, float y, float width, float height, String placeholder, TextInputProvider.InputType type, int maxChars, GuiScreen parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
        this.type = type;
        this.parent = parent;
        this.maxChars = maxChars;
    }

    public GuiTextInput(float x, float y, float width, float height, String placeholder, String suffix, TextInputProvider.InputType type, int maxChars, GuiScreen parent) {
        this(x, y, width, height, placeholder, type, maxChars, parent);

        this.suffix = suffix;
    }

    @Override
    public void create() {
        Trainer trainer = Trainer.getTrainer();

        inputHandler = trainer.getInputHandler();
        textRenderer = trainer.getTextRenderer();
        textInputProvider = trainer.getTextInputProvider();

        inputHandler.addInputHandler(this);

        TextureManager textureManager = trainer.getTextureManager();
        normalTexture = new NinePatch(textureManager.getTexture("gui/text_input.png"), 40, 40, 40, 40);

        if (!registeredListener) {
            registeredListener = true;
            trainer.getTextInputProvider().registerListener(new StaticTextInputHandler());
        }

        bounds = new Rectangle(x, y, width, height);
    }

    @Override
    public void destroy() {
        inputHandler.removeInputHandler(this);
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        if (!visible) {
            return;
        }

        bounds = new Rectangle(x, y, width, height);

        normalTexture.draw(spriteBatch, x, y, width, height);

        if (text.isEmpty() && !placeholder.isEmpty() && focusedElement != this) {
            textRenderer.drawCenteredText(placeholder, x + width / 2, y + height / 2 + 3, 26, "Maiandra", FontStyle.NORMAL, Color.LIGHT_GRAY);
        } else {
            textRenderer.drawCenteredText(text + " " + suffix, x + width / 2, y + height / 2 + 3, 26, "Maiandra", FontStyle.NORMAL, Color.WHITE);
        }
    }

    private void updateText(String text) {
        if (!visible) {
            return;
        }

        this.text = text;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (visible && bounds.contains(x, y)) {
            focusedElement = this;
            textInputProvider.openTextInput(placeholder, text, type, maxChars);

            return true;
        }

        if (parent.guiElements.stream().filter(it -> it instanceof GuiTextInput).noneMatch(it -> ((GuiTextInput) it).bounds.contains(x, y))) {
            textInputProvider.closeTextInput();
            focusedElement = null;

            return true;
        }

        return false;
    }
}
