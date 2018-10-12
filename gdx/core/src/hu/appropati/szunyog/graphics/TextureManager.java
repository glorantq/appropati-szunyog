package hu.appropati.szunyog.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class TextureManager {
    private static TextureManager INSTANCE;
    public static TextureManager create(AssetManager assetManager) {
        if(INSTANCE == null) {
            INSTANCE = new TextureManager(assetManager);
        }

        return INSTANCE;
    }

    public static TextureManager get() {
        if(INSTANCE == null) {
            throw new UnsupportedOperationException("Not initialised!");
        }

        return INSTANCE;
    }

    private final AssetManager assetManager;
    private final Logger logger;

    @Getter
    private final Texture defaultTexture;

    private TextureManager(AssetManager assetManager) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.assetManager = assetManager;

        Pixmap defaultTexturePixmap = new Pixmap(64, 64, Pixmap.Format.RGB888);

        defaultTexturePixmap.setColor(0, 0, 0, 1);
        defaultTexturePixmap.fillRectangle(0, 0, 64, 64);

        defaultTexturePixmap.setColor(1, 0, 0, 1);
        defaultTexturePixmap.fillRectangle(0, 0, 32, 32);
        defaultTexturePixmap.fillRectangle(32, 32, 32, 32);

        this.defaultTexture = new Texture(defaultTexturePixmap);
    }

    public Texture getTexture(String name) {
        Texture texture = getTexture0(name);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return texture;
    }

    private Texture getTexture0(String name) {
        if(assetManager.isLoaded(name)) {
            logger.debug("Loaded texture: {}", name);
            return assetManager.get(name, Texture.class);
        }

        if(Gdx.files.internal(name).exists()) {
            logger.warn("Loading texture {} on-demand!", name);
            return new Texture(name);
        }

        logger.error("Texture {} not found!", name);
        return defaultTexture;
    }
}