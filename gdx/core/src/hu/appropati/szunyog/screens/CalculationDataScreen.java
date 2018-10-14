package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.calculation.CalculationParameter;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.elements.GuiTextInput;

public class CalculationDataScreen extends MenuScreen {
    private final CalculationParameter.Type calculationTarget;

    private Viewport viewport;
    private TextRenderer textRenderer;

    public CalculationDataScreen(CalculationParameter.Type calculationTarget) {
        if(calculationTarget != CalculationParameter.Type.TARGET_DISTANCE && calculationTarget != CalculationParameter.Type.START_TIME) {
            throw new ExceptionInInitializerError("Invalid target!");
        }

        this.calculationTarget = calculationTarget;
    }

    @Override
    public void show() {
        super.show();

        Trainer trainer = Trainer.getTrainer();
        textRenderer = trainer.getTextRenderer();
        viewport = trainer.getViewport();

        createElement(new GuiTextInput(100, 100, 200, 75, "Testing", this));
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        drawElements(spriteBatch);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
