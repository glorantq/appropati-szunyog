package hu.appropati.szunyog.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.graphics.text.FontStyle;
import hu.appropati.szunyog.gui.GuiScreen;
import hu.appropati.szunyog.gui.elements.GuiButton;

public class TestScreen extends GuiScreen {
    private Trainer trainer = Trainer.getTrainer();

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        trainer.getTextRenderer().drawText("Testing", 0, 0, 36, "Roboto", FontStyle.ITALIC, Color.RED, true);
        trainer.getTextRenderer().drawCenteredText("Haha yes", Trainer.getTrainer().getViewport().getWorldWidth() / 2, Trainer.getTrainer().getViewport().getWorldHeight() / 2, 72, "Roboto", FontStyle.BOLD, Color.WHITE);
        trainer.getTextRenderer().drawRightText("Indeed", Trainer.getTrainer().getViewport().getWorldWidth(), 0, 36, "Roboto", FontStyle.NORMAL, Color.BLUE, true);
        trainer.getTextRenderer().drawText("Hmmst", 0, Trainer.getTrainer().getViewport().getWorldHeight(), 36, "Roboto", FontStyle.BOLD, Color.GREEN, false);
        trainer.getTextRenderer().drawRightText("Ausgesuccent mein fren", Trainer.getTrainer().getViewport().getWorldWidth(), Trainer.getTrainer().getViewport().getWorldHeight(), 36, "Roboto", FontStyle.ITALIC, Color.ORANGE, false);

        drawElements(spriteBatch);
    }

    @Override
    public void show() {

        GuiButton button = new GuiButton(100, 100, 250, 100, "Testing", GuiButton.Style.builder().fontSize(36).fontStyle(FontStyle.ITALIC).build(), new Texture("badlogic.jpg"));
        createElement(button);

        button.onClick((longPress) -> {
            System.err.println("Kalesz " + longPress);
        });

        createElement(new GuiButton(300, 300, 120, 120, "X", GuiButton.Style.builder().fontSize(36).fontStyle(FontStyle.BOLD).fontColor(Color.RED).build()));
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
