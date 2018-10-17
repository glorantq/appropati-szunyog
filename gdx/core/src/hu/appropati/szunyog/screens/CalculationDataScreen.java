package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.calculation.CalculationParameter;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.gui.elements.GuiButton;
import hu.appropati.szunyog.gui.elements.GuiCheckBox;
import hu.appropati.szunyog.gui.elements.GuiTextInput;
import hu.appropati.szunyog.input.text.TextInputProvider;

public class CalculationDataScreen extends MenuScreen {
    private final CalculationParameter.Type calculationTarget;

    private Viewport viewport;
    private TextRenderer textRenderer;

    private GuiTextInput flySpeedInput;
    private GuiTextInput humansDistanceInput;
    private GuiTextInput flyTravelDistanceInput;
    private GuiTextInput speedHumanA;
    private GuiTextInput speedHumanB;
    private GuiCheckBox windCheckBox;
    private GuiTextInput windSpeedInput;
    private GuiCheckBox animationCheckBox;

    private int windDirection = 1;

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

        float inputWidth = 350;
        float inputHeight = 95;

        flySpeedInput = new GuiTextInput(viewport.getWorldWidth() / 2 - inputWidth / 2, viewport.getWorldHeight() / 2 - inputHeight/2, inputWidth, inputHeight, "Szúnyog Sebessége", TextInputProvider.InputType.NUMBER, 3, this);

        if(calculationTarget == CalculationParameter.Type.START_TIME) {
            flyTravelDistanceInput = new GuiTextInput(flySpeedInput.getX(), flySpeedInput.getY() + inputHeight + 5, inputWidth, inputHeight, "Céltávolság", TextInputProvider.InputType.NUMBER, 3, this);
        }

        humansDistanceInput = new GuiTextInput(flySpeedInput.getX(), (flyTravelDistanceInput == null ? flySpeedInput.getY() : flyTravelDistanceInput.getY()) + inputHeight + 5, inputWidth, inputHeight, "Emberek Távolsága", TextInputProvider.InputType.NUMBER, 3, this);
        speedHumanA = new GuiTextInput(humansDistanceInput.getX(), humansDistanceInput.getY() + inputHeight + 5, inputWidth / 2 - 2, inputHeight, "A Sebessége", TextInputProvider.InputType.NUMBER, 3, this);
        speedHumanB = new GuiTextInput(speedHumanA.getX() + speedHumanA.getWidth() + 5, speedHumanA.getY(), speedHumanA.getWidth(), speedHumanA.getHeight(), "B Sebessége", TextInputProvider.InputType.NUMBER, 3, this);

        GuiButton.Style checkBoxStyle = GuiButton.Style.builder().fontColor(Color.WHITE).fontSize(26).build();
        windCheckBox = new GuiCheckBox(flySpeedInput.getX(), flySpeedInput.getY() - 5 - inputHeight / 1.3f, inputHeight / 1.3f, inputHeight / 1.3f, "Szél Engedélyezése", checkBoxStyle);
        windCheckBox.setX(viewport.getWorldWidth() / 2 - windCheckBox.getWidth() / 2);

        windSpeedInput = new GuiTextInput(flySpeedInput.getX(), windCheckBox.getY() - inputHeight - 5, inputWidth / 2 - 2, inputHeight, "Szélsebesség", TextInputProvider.InputType.NUMBER, 3, this);
        windSpeedInput.setVisible(windCheckBox.isChecked());

        animationCheckBox = new GuiCheckBox(windSpeedInput.getX(), windCheckBox.getY() - 5 - inputHeight / 1.3f, inputHeight / 1.3f, inputHeight / 1.3f, "Út Animálása", checkBoxStyle);
        animationCheckBox.setX(viewport.getWorldWidth() / 2 - animationCheckBox.getWidth() / 2);

        GuiButton windDirectionToggle = new GuiButton(windSpeedInput.getX() + windSpeedInput.getWidth() + 5, windSpeedInput.getY(), inputWidth / 2 - 2, inputHeight, "Jobbra", GuiButton.Style.builder().build());
        windDirectionToggle.onClick((longPress) -> {
            windDirection *= -1;

            if(windDirection == 1) {
                windDirectionToggle.setText("Jobbra");
            } else {
                windDirectionToggle.setText("Balra");
            }
        });
        windDirectionToggle.setVisible(windCheckBox.isChecked());

        createElement(flySpeedInput);
        createElement(humansDistanceInput);

        if(flyTravelDistanceInput != null) {
            createElement(flyTravelDistanceInput);
        }

        createElement(speedHumanA);
        createElement(speedHumanB);
        createElement(windCheckBox);
        createElement(windSpeedInput);
        createElement(animationCheckBox);
        createElement(windDirectionToggle);

        GuiButton backButton = new GuiButton(5, 5, 130, 75, "Vissza", GuiButton.Style.builder().build());
        backButton.onClick((longPress) -> trainer.setScreen(new MainScreen()));

        createElement(backButton);

        GuiButton nextButton = new GuiButton(viewport.getWorldWidth() - backButton.getWidth() - 5, 5, backButton.getWidth(), backButton.getHeight(), "Tovább", GuiButton.Style.builder().build());

        createElement(nextButton);

        windCheckBox.onStateChange((checked) -> {
            windSpeedInput.setVisible(checked);
            windDirectionToggle.setVisible(checked);

            if(checked) {
                animationCheckBox.setY(windSpeedInput.getY() - 5 - inputHeight / 1.3f);
            } else {
                animationCheckBox.setY(windCheckBox.getY() - 5 - inputHeight / 1.3f);
            }
        });
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
