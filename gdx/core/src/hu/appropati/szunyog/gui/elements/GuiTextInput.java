package hu.appropati.szunyog.gui.elements;

import com.badlogic.gdx.Gdx;
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

public class GuiTextInput implements GuiElement, InputHandler {
    private static boolean registeredListener = false;
    private static GuiTextInput focusedElement = null;

    private static class StaticTextInputHandler implements TextInputListener {

        @Override
        public void textUpdated(String text) {
            if(focusedElement != null) {
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

    private String text = "";
    private final String placeholder;

    private Rectangle bounds;

    private NinePatch normalTexture;

    public GuiTextInput(float x, float y, float width, float height, String placeholder, GuiScreen parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
        this.parent = parent;
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

        if(!registeredListener) {
            registeredListener = true;
            trainer.getTextInputProvider().registerListener(new StaticTextInputHandler());
        }
    }

    @Override
    public void destroy() {
        inputHandler.removeInputHandler(this);
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        bounds = new Rectangle(x, y, width, height);

        normalTexture.draw(spriteBatch, x, y, width, height);

        if(text.isEmpty() && !placeholder.isEmpty() && focusedElement != this) {
            textRenderer.drawCenteredText(placeholder, x + width / 2, y + height / 2 + 3, 26, "Maiandra", FontStyle.NORMAL, Color.LIGHT_GRAY);
        } else {
            textRenderer.drawCenteredText(text, x + width / 2, y + height / 2 + 3, 26, "Maiandra", FontStyle.NORMAL, Color.WHITE);
        }
    }

    private void updateText(String text) {
        this.text = text;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("I", x + " " + y);

        if(bounds.contains(x, y)) {
            focusedElement = this;
            textInputProvider.openTextInput(placeholder, TextInputProvider.InputType.TEXT, 64);

            Gdx.app.log("T", "Open");

            return true;
        }

        if(parent.guiElements.stream().filter(it -> it instanceof GuiTextInput).noneMatch(it -> ((GuiTextInput) it).bounds.contains(x, y))) {
            textInputProvider.closeTextInput();
            focusedElement = null;

            Gdx.app.log("T", "Other");

            return true;
        }

        return false;
    }
}
