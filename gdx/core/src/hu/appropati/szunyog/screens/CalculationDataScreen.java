package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.calculation.CalculationParameter;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.FontStyle;
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

    private GuiButton nextButton;

    private int windDirection = 1;
    private int precision;

    private String dataErrorMessage = "";

    private NinePatch errorBackground;

    CalculationDataScreen(CalculationParameter.Type calculationTarget) {
        if (calculationTarget != CalculationParameter.Type.TARGET_DISTANCE && calculationTarget != CalculationParameter.Type.START_TIME) {
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

        flySpeedInput = new GuiTextInput(viewport.getWorldWidth() / 2 - inputWidth / 2, viewport.getWorldHeight() / 2 - inputHeight / 2, inputWidth, inputHeight, "Szúnyog Sebessége", TextInputProvider.InputType.NUMBER, 3, this);

        if (calculationTarget == CalculationParameter.Type.START_TIME) {
            flyTravelDistanceInput = new GuiTextInput(flySpeedInput.getX(), flySpeedInput.getY() + inputHeight + 5, inputWidth, inputHeight, "Céltávolság", TextInputProvider.InputType.NUMBER, 3, this);
        }

        humansDistanceInput = new GuiTextInput(flySpeedInput.getX(), (flyTravelDistanceInput == null ? flySpeedInput.getY() : flyTravelDistanceInput.getY()) + inputHeight + 5, inputWidth, inputHeight, "Emberek Távolsága", TextInputProvider.InputType.NUMBER, 3, this);
        speedHumanA = new GuiTextInput(humansDistanceInput.getX(), humansDistanceInput.getY() + inputHeight + 5, inputWidth / 2 - 2, inputHeight, "A Sebessége", TextInputProvider.InputType.NUMBER, 3, this);
        speedHumanB = new GuiTextInput(speedHumanA.getX() + speedHumanA.getWidth() + 5, speedHumanA.getY(), speedHumanA.getWidth(), speedHumanA.getHeight(), "B Sebessége", TextInputProvider.InputType.NUMBER, 3, this);

        GuiButton.Style checkBoxStyle = GuiButton.Style.builder().fontColor(Color.WHITE).fontSize(26).fontName("Maiandra").build();
        windCheckBox = new GuiCheckBox(flySpeedInput.getX(), flySpeedInput.getY() - 5 - inputHeight / 1.3f, inputHeight / 1.3f, inputHeight / 1.3f, "Szél Engedélyezése", checkBoxStyle);
        windCheckBox.setX(viewport.getWorldWidth() / 2 - windCheckBox.getWidth() / 2);

        windSpeedInput = new GuiTextInput(flySpeedInput.getX(), windCheckBox.getY() - inputHeight - 5, inputWidth / 2 - 2, inputHeight, "Szélsebesség", TextInputProvider.InputType.NUMBER, 3, this);
        windSpeedInput.setVisible(windCheckBox.isChecked());

        animationCheckBox = new GuiCheckBox(windSpeedInput.getX(), windCheckBox.getY() - 5 - inputHeight / 1.3f, inputHeight / 1.3f, inputHeight / 1.3f, "Út Animálása", checkBoxStyle);
        animationCheckBox.setX(viewport.getWorldWidth() / 2 - animationCheckBox.getWidth() / 2);

        GuiButton windDirectionToggle = new GuiButton(windSpeedInput.getX() + windSpeedInput.getWidth() + 5, windSpeedInput.getY(), inputWidth / 2 - 2, inputHeight, "Jobbra", GuiButton.Style.builder().build());
        windDirectionToggle.onClick((longPress) -> {
            windDirection *= -1;

            if (windDirection == 1) {
                windDirectionToggle.setText("Jobbra");
            } else {
                windDirectionToggle.setText("Balra");
            }
        });
        windDirectionToggle.setVisible(windCheckBox.isChecked());

        createElement(flySpeedInput);
        createElement(humansDistanceInput);

        if (flyTravelDistanceInput != null) {
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
        backButton.setCatchBackKey(true);

        createElement(backButton);

        nextButton = new GuiButton(viewport.getWorldWidth() - backButton.getWidth() - 5, 5, backButton.getWidth(), backButton.getHeight(), "Tovább", GuiButton.Style.builder().build());

        createElement(nextButton);

        windCheckBox.onStateChange((checked) -> {
            windSpeedInput.setVisible(checked);
            windDirectionToggle.setVisible(checked);

            if (checked) {
                animationCheckBox.setY(windSpeedInput.getY() - 5 - inputHeight / 1.3f);
            } else {
                animationCheckBox.setY(windCheckBox.getY() - 5 - inputHeight / 1.3f);
            }
        });

        TextureManager textureManager = trainer.getTextureManager();
        errorBackground = new NinePatch(textureManager.getTexture("gui/text_input.png"), 40, 40, 40, 40);

        nextButton.onClick((longPress) -> {
            boolean isAnimated = animationCheckBox.isChecked();
            float humansDistance = Float.parseFloat(humansDistanceInput.getText());
            int flySpeed = Integer.parseInt(flySpeedInput.getText());
            int windSpeed = (windCheckBox.isChecked() ? Integer.parseInt(windSpeedInput.getText()) : 0) * windDirection;

            int humanASpeed = Integer.parseInt(speedHumanA.getText());
            int humanBSpeed = Integer.parseInt(speedHumanB.getText());

            if (isAnimated) {
                if (calculationTarget == CalculationParameter.Type.TARGET_DISTANCE) {
                    Trainer.getTrainer().setScreen(new AnimationScreen(humanASpeed, humanBSpeed, humansDistance, flySpeed, windSpeed, 0f));
                } else {
                    Trainer.getTrainer().setScreen(new AnimationScreen(humanASpeed, humanBSpeed, humansDistance, flySpeed, windSpeed, calculateStartTime(false)));
                }
            } else {
                if (calculationTarget == CalculationParameter.Type.TARGET_DISTANCE) {
                    calculateTravelDistance();
                } else {
                    calculateStartTime(true);
                }
            }
        });

        precision = trainer.getPreferences().getInteger("precision");
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        drawElements(spriteBatch);

        if (!dataErrorMessage.isEmpty()) {
            Vector2 textSize = textRenderer.getTextSize(dataErrorMessage, "Maiandra", FontStyle.BOLD, 26);
            errorBackground.draw(spriteBatch, viewport.getWorldWidth() / 2 - textSize.x / 2 - 20, nextButton.getY() + nextButton.getHeight() + 150 - 72 / 2, textSize.x + 40, 72);
            textRenderer.drawCenteredText(dataErrorMessage, viewport.getWorldWidth() / 2, nextButton.getY() + nextButton.getHeight() + 150, 26, "Maiandra", FontStyle.BOLD, Color.valueOf("DD1111"));
        }

        if (calculationTarget == CalculationParameter.Type.TARGET_DISTANCE) {
            nextButton.setVisible(validateDataForDistanceCalculation());
        } else {
            nextButton.setVisible(validateDataForTimeCalculation());
        }
    }

    private boolean validateDataForTimeCalculation() {
        if (flyTravelDistanceInput.getText().isEmpty()) {
            dataErrorMessage = "Nincs megadva a céltávolság!";
            return false;
        }

        int targetDistance = Integer.parseInt(flyTravelDistanceInput.getText());

        if (targetDistance < 0) {
            dataErrorMessage = "A céltávolság nem lehet kisebb mint 0!";
            return false;
        }

        if (targetDistance == 0) {
            dataErrorMessage = "Ha a szúnyog 0 km-t akar megtenni, nem kell elindulnia";
            return false;
        }

        return validateDataForDistanceCalculation();
    }

    private boolean validateDataForDistanceCalculation() {
        if (flySpeedInput.getText().isEmpty()) {
            dataErrorMessage = "Nincs megadva a szúnyog sebessége!";
            return false;
        } else if (Integer.parseInt(flySpeedInput.getText()) <= 0) {
            dataErrorMessage = "Nem adhatsz meg negatív vagy nulla értéket!";
            return false;
        }

        if (humansDistanceInput.getText().isEmpty()) {
            dataErrorMessage = "Nincs megadva az emberek távolsága";
            return false;
        } else if (Integer.parseInt(humansDistanceInput.getText()) <= 0) {
            dataErrorMessage = "Nem adhatsz meg negatív vagy nulla értéket!";
            return false;
        }

        if (speedHumanA.getText().isEmpty() || speedHumanB.getText().isEmpty()) {
            dataErrorMessage = "Nincs megadva az emberek sebessége!";
            return false;
        } else if (Integer.parseInt(speedHumanA.getText()) <= 0 || Integer.parseInt(speedHumanB.getText()) <= 0) {
            dataErrorMessage = "Nem adhatsz meg negatív vagy nulla értéket!";
            return false;
        }

        if (windCheckBox.isChecked() && windSpeedInput.getText().isEmpty()) {
            dataErrorMessage = "Nincs megadva a szélsebesség";
            return false;
        } else if (windCheckBox.isChecked() && Integer.parseInt(windSpeedInput.getText()) <= 0) {
            dataErrorMessage = "Nem adhatsz meg negatív vagy nulla értéket!";
            return false;
        }

        int flySpeed = Integer.parseInt(flySpeedInput.getText());
        int windSpeed = (windCheckBox.isChecked() ? Integer.parseInt(windSpeedInput.getText()) : 0) * windDirection;

        if (Math.abs(flySpeed) < Math.abs(windSpeed)) {
            dataErrorMessage = "A szél el fogja fújni a szúnyogot!";
            return false;
        }

        if (Math.abs(flySpeed) == Math.abs(windSpeed)) {
            dataErrorMessage = "A szél nem lehet olyan gyors mint a szúnyog";
            return false;
        }

        dataErrorMessage = "";

        return true;
    }

    private void calculateTravelDistance() {
        float humansDistance = Float.parseFloat(humansDistanceInput.getText());
        int flySpeed = Integer.parseInt(flySpeedInput.getText());
        int windSpeed = (windCheckBox.isChecked() ? Integer.parseInt(windSpeedInput.getText()) : 0) * windDirection;

        int humanASpeed = Integer.parseInt(speedHumanA.getText());
        int humanBSpeed = Integer.parseInt(speedHumanB.getText());

        int flyDirection = 1;
        float humanAX = 0;
        float humanBX = humansDistance;
        float flyX = 0;

        float humanAMovement;
        float humanBMovement;
        float relativeWindSpeed;
        float flyMovement;

        float totalDistance = 0;
        float totalTime = 0;

        float delta = 1 / (float) precision;

        while (humansDistance > 0) {
            humanAMovement = humanASpeed * delta;
            humanBMovement = -humanBSpeed * delta;

            if (flyX <= humanAX) {
                flyDirection = 1;
            }

            if (flyX >= humanBX) {
                flyDirection = -1;
            }

            relativeWindSpeed = windSpeed * flyDirection;

            flyMovement = (flySpeed + relativeWindSpeed) * flyDirection * delta;

            humanAX += humanAMovement;
            humanBX += humanBMovement;

            flyX += flyMovement;

            humansDistance = Math.abs(humanBX) - Math.abs(humanAX);

            totalDistance += Math.abs(flyMovement);
            totalTime += delta;
        }

        Trainer.getTrainer().setScreen(new CalculationResultScreen(0f, totalTime, totalDistance));
    }

    private float calculateStartTime(boolean showResult) {
        float humansDistance = Float.parseFloat(humansDistanceInput.getText());
        int targetDistance = Integer.parseInt(flyTravelDistanceInput.getText());
        int flySpeed = Integer.parseInt(flySpeedInput.getText());
        int windSpeed = (windCheckBox.isChecked() ? Integer.parseInt(windSpeedInput.getText()) : 0) * windDirection;

        int humanASpeed = Integer.parseInt(speedHumanA.getText());
        int humanBSpeed = Integer.parseInt(speedHumanB.getText());

        int flyDirection = 1;
        float humanAX = 0;
        float humanBX = humansDistance;
        float flyX = 0;

        float humanAMovement;
        float humanBMovement;
        float relativeWindSpeed;
        float flyMovement;

        float totalDistance = 0;
        float totalTime = 0;

        float delta = 1 / (float) precision;

        while (humansDistance > 0) {
            humanAMovement = humanASpeed * delta;
            humanBMovement = -humanBSpeed * delta;

            if (flyX <= humanAX) {
                flyDirection = 1;
            }

            if (flyX >= humanBX) {
                flyDirection = -1;
            }

            relativeWindSpeed = windSpeed * flyDirection;

            flyMovement = (flySpeed + relativeWindSpeed) * flyDirection * delta;

            humanAX += humanAMovement;
            humanBX += humanBMovement;

            flyX += flyMovement;

            humansDistance = Math.abs(humanBX) - Math.abs(humanAX);

            totalDistance += Math.abs(flyMovement);
            totalTime += delta;
        }

        float x = totalTime / totalDistance;
        float startDistance = Math.max(0, totalDistance - targetDistance);

        float startTime = x * startDistance;

        if (showResult) {
            Trainer.getTrainer().setScreen(new CalculationResultScreen(startTime, totalTime - startTime, Math.min(targetDistance, totalDistance)));
        }

        return startTime;
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
