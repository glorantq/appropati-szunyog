package hu.appropati.szunyog.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.gui.GuiScreen;

public class AssetLoaderScreen extends GuiScreen {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Trainer trainer = Trainer.getTrainer();
    private AssetManager assetManager = trainer.getAssetManager();

    private final Screen nextScreen;

    private final String[] textureList;
    private final String[] soundList;
    private final String[] musicList;

    public AssetLoaderScreen(Screen nextScreen, String[] textureList, String[] soundList, String[] musicList) {
        this.nextScreen = nextScreen;

        this.textureList = textureList;
        this.soundList = soundList;
        this.musicList = musicList;
    }

    @Override
    public void show() {
        Arrays.stream(textureList).forEach((it) -> assetManager.load(it, Texture.class));
        Arrays.stream(soundList).forEach((it) -> assetManager.load(it, Sound.class));
        Arrays.stream(musicList).forEach((it) -> assetManager.load(it, Music.class));

        logger.info("Loading {} assets! ({}, {}, {})", assetManager.getQueuedAssets(), textureList.length, soundList.length, musicList.length);
    }

    @Override
    protected void draw(SpriteBatch spriteBatch) {
        if(assetManager.update()) {
            trainer.setScreen(nextScreen);
            return;
        }

        float progress = assetManager.getProgress();
        logger.debug("Loading progress: {}", progress);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
