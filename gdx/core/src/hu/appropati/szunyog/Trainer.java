package hu.appropati.szunyog;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.appropati.szunyog.dialog.DialogBox;
import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.Font;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.input.GdxInputHandler;
import hu.appropati.szunyog.screens.AssetLoaderScreen;
import hu.appropati.szunyog.screens.CrashScreen;
import hu.appropati.szunyog.screens.TestScreen;
import lombok.Getter;

public class Trainer extends Game {
    public static int WIDTH = 720;
    public static int HEIGHT = 1280;

    private static Trainer INSTANCE;

    public static Trainer getTrainer() {
        if (INSTANCE == null) {
            INSTANCE = new Trainer();
        }

        return INSTANCE;
    }

    private Color gradientTop = new Color(0f, 0f, 0f, 0f);
    private Color gradientBottom = new Color(0f, 0f, 0f, 1f);

    private Trainer() { }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    private Screen currentScreen;

    @Getter
    private DialogBox currentDialogBox;

    @Getter
    private SpriteBatch spriteBatch;

    @Getter
    private Viewport viewport;

    @Getter
    private TextureManager textureManager;

    @Getter
    private AssetManager assetManager;

    @Getter
    private TextRenderer textRenderer;

    @Getter
    private GdxInputHandler inputHandler;

    @Override
    public void create() {
        logger.info("Starting up!");

        spriteBatch = new SpriteBatch();
        viewport = new ExtendViewport(WIDTH, HEIGHT);

        assetManager = new AssetManager();
        textureManager = TextureManager.create(assetManager);

        textRenderer = new TextRenderer(spriteBatch);
        textRenderer.registerFont(new Font("Roboto", "fonts/roboto/Roboto-Regular.ttf", "fonts/roboto/Roboto-Italic.ttf", "fonts/roboto/Roboto-Bold.ttf"));

        inputHandler = new GdxInputHandler();
        Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector(inputHandler), inputHandler));

        try {
            logger.info("Starting asset loading!");
            FileHandle assetsFile = Gdx.files.internal("assets.json");
            String assetsFileContent = assetsFile.readString("UTF-8");

            setScreen(new AssetLoaderScreen(new TestScreen(), new Gson().fromJson(assetsFileContent, AssetLoaderScreen.TextureFileData.class)));
        } catch (Exception e) {
            openCrashScreen(e);
        }

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();

            openCrashScreen(throwable);
        });
    }

    @Override
    public void render() {
        try {
            safeRender();
        } catch (Exception e) {
            openCrashScreen(e);
        }

        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.graphics.setTitle("Appropati Szunyogfitnesz -- " + Gdx.graphics.getFramesPerSecond() + " FPS");
        }
    }

    private void safeRender() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        if (currentScreen != null) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }

        if(currentDialogBox != null) {
            drawGradient(spriteBatch, textureManager.getWhite(), 0, 0,
                    viewport.getWorldWidth(), viewport.getWorldHeight(), gradientTop, gradientBottom, false);

            currentDialogBox.render(spriteBatch);
        }

        spriteBatch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        super.resize(width, height);

        if(currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        logger.info("Paused!");

        if(currentScreen != null) {
            currentScreen.pause();
        }
    }

    @Override
    public void resume() {
        logger.info("Resumed!");

        if(currentScreen != null) {
            currentScreen.resume();
        }
    }

    @Override
    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }

        currentScreen = screen;

        if (currentScreen != null) {
            currentScreen.show();
            currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            logger.debug("Changing to: {}", currentScreen.getClass().getCanonicalName());
        } else {
            logger.debug("Removed current screen!");
        }
    }

    public void openDialogBox(DialogBox dialogBox) {
        if(currentDialogBox != null) {
            logger.warn("Already has a dialog open, trying to close it...");
            if(currentDialogBox.isCloseable()) {
                currentDialogBox.close();
                currentDialogBox = null;
            } else {
                throw new RuntimeException("Can't close current dialog box");
            }
        }

        inputHandler.touchUp(0, 0, 0, 0);

        currentDialogBox = dialogBox;
        currentDialogBox.show();
    }

    public void closeDialogBox() {
        if(currentDialogBox == null) {
            return;
        }

        if(!currentDialogBox.isCloseable()) {
            return;
        }

        currentDialogBox.close();
        currentDialogBox = null;

        inputHandler.touchUp(0, 0, 0, 0);
    }

    private void openCrashScreen(Throwable throwable) {
        if(spriteBatch.isDrawing()) {
            spriteBatch.end();
        }

        if(currentScreen instanceof CrashScreen) {
            throwable.printStackTrace();
            Gdx.app.exit();
            return;
        }

        setScreen(new CrashScreen(throwable));

        Gdx.input.setInputProcessor(null);
    }

    private final float[] vertices = new float[20];

    private void drawGradient(SpriteBatch batch,
                              TextureRegion white,
                              float x, float y,
                              float width, float height,
                              Color colorA, Color colorB,
                              boolean horizontal) {

        float ca = colorA.toFloatBits();
        float cb = colorB.toFloatBits();
        int idx = 0;
        float u = white.getU();
        float v = white.getV2();
        float u2 = white.getU2();
        float v2 = white.getV();

        //bottom left
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = horizontal ? ca : cb;
        vertices[idx++] = u;
        vertices[idx++] = v;

        //top left
        vertices[idx++] = x;
        vertices[idx++] = y + height;
        vertices[idx++] = ca;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        //top right
        vertices[idx++] = x + width;
        vertices[idx++] = y + height;
        vertices[idx++] = horizontal ? cb : ca;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        //bottom right
        vertices[idx++] = x + width;
        vertices[idx++] = y;
        vertices[idx++] = cb;
        vertices[idx++] = u2;
        vertices[idx++] = v;

        batch.draw(white.getTexture(), vertices, 0, vertices.length);
    }
}
