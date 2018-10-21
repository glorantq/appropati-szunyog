package hu.appropati.szunyog;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.appropati.szunyog.graphics.TextureManager;
import hu.appropati.szunyog.graphics.text.Font;
import hu.appropati.szunyog.graphics.text.TextRenderer;
import hu.appropati.szunyog.input.GdxInputHandler;
import hu.appropati.szunyog.input.text.TextInputProvider;
import hu.appropati.szunyog.screens.AssetLoaderScreen;
import hu.appropati.szunyog.screens.CrashScreen;
import hu.appropati.szunyog.screens.MainScreen;
import lombok.Getter;

public class Trainer extends Game {
    public static int WIDTH = 720;
    public static int HEIGHT = 1280;

    private static Trainer INSTANCE;

    public static Trainer getTrainer() {
        return INSTANCE;
    }

    public static Trainer createTrainer(TextInputProvider textInputProvider) {
        if (INSTANCE == null) {
            INSTANCE = new Trainer(textInputProvider);
        }

        return INSTANCE;
    }

    private Trainer(TextInputProvider provider) {
        this.textInputProvider = provider;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    private Screen currentScreen;

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

    @Getter
    private final TextInputProvider textInputProvider;

    @Override
    public void create() {
        logger.info("Starting up!");

        spriteBatch = new SpriteBatch();
        viewport = new ExtendViewport(WIDTH, HEIGHT);

        assetManager = new AssetManager();
        textureManager = TextureManager.create(assetManager);

        textRenderer = new TextRenderer(spriteBatch);
        textRenderer.registerFont(new Font("Roboto", "fonts/roboto/Roboto-Regular.ttf", "fonts/roboto/Roboto-Italic.ttf", "fonts/roboto/Roboto-Bold.ttf"));
        textRenderer.registerFont(new Font("Maiandra", "fonts/maiandra/Maiandra GD Regular.ttf", "fonts/maiandra/Maiandra GD Italic.ttf", "fonts/maiandra/Maiandra GD Demi Bold.ttf"));
        textRenderer.registerFont(new Font("Niramit", "fonts/niramit/Niramit-Regular.ttf", "fonts/niramit/Niramit-Italic.ttf", "fonts/niramit/Niramit-Bold.ttf"));

        inputHandler = new GdxInputHandler();
        Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector(inputHandler), inputHandler));

        try {
            logger.info("Starting asset loading!");
            FileHandle assetsFile = Gdx.files.internal("assets.json");
            String assetsFileContent = assetsFile.readString("UTF-8");

            setScreen(new AssetLoaderScreen(new MainScreen(), new Gson().fromJson(assetsFileContent, AssetLoaderScreen.TextureFileData.class)));
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
}
