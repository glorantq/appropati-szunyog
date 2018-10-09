package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.PrintWriter;
import java.io.StringWriter;

import hu.appropati.szunyog.Trainer;

public class CrashScreen implements Screen {
    private final Throwable exception;
    private final String stackTrace;

    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    public CrashScreen(Throwable exception) {
        this.exception = exception;

        Trainer trainer = Trainer.getTrainer();
        this.viewport = trainer.getViewport();
        this.spriteBatch = trainer.getSpriteBatch();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);

        this.stackTrace = stringWriter.toString();
    }

    @Override
    public void show() {
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        bitmapFont.getData().setScale(2f);

        String header = String.format("%s: %s\n=================================", exception.getClass().getCanonicalName(), exception.getMessage());
        float headerHeight = getTextSize(header).y;
        bitmapFont.draw(spriteBatch, header, 15, viewport.getWorldHeight() - 15, viewport.getWorldWidth() - 15, Align.left, true);

        bitmapFont.getData().setScale(1.2f);
        bitmapFont.draw(spriteBatch, stackTrace, 15, viewport.getWorldHeight() - 25 - headerHeight, viewport.getWorldWidth() - 15, Align.left, true);
    }

    private Vector2 getTextSize(String text) {
        glyphLayout.setText(bitmapFont, text);
        return new Vector2(glyphLayout.width, glyphLayout.height);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
    }
}
