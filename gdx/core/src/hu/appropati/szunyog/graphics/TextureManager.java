package hu.appropati.szunyog.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * Textúrák betöltése
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
public class TextureManager {
    private static TextureManager INSTANCE;
    public static TextureManager create(AssetManager assetManager) {
        if(INSTANCE == null) {
            INSTANCE = new TextureManager(assetManager);
        }

        return INSTANCE;
    }

    private final AssetManager assetManager;
    private final Logger logger;

    @Getter
    private final Texture defaultTexture;

    @Getter
    private final TextureRegion white;

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

        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(1f, 1f, 1f, 1f);
        whitePixmap.fillRectangle(0, 0, 1, 1);

        this.white = new TextureRegion(new Texture(whitePixmap));
    }

    /**
     * Betölt egy textúrát
     *  Ha előre be van töltve, azt használja
     *  Ha nincs betöltve, betölti
     *  Ha érvénytelen a név, egy alap textúrát ad vissza
     *
     * @param name Fájl neve
     *
     * @return A textúra
     */
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
