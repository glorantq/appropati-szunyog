package hu.appropati.szunyog.graphics.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import lombok.Data;

/**
 * Szövegrajzolásra használható osztály
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
public class TextRenderer {
    private final List<Font> loadedFonts = new CopyOnWriteArrayList<>();

    private final LoadingCache<CacheLookup, BitmapFont> fontCache = Caffeine.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build(key -> loadedFonts.stream().filter((it) -> it.getName().equalsIgnoreCase(key.getName())).findFirst().orElseThrow(() -> new RuntimeException("Invalid font")).getFont(key.getStyle(), key.getSize()));

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SpriteBatch spriteBatch;

    private final GlyphLayout glyphLayout;

    public TextRenderer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.glyphLayout = new GlyphLayout();
    }

    /**
     * Hozzáad egy betűtípust a használhatóak közé
     *
     * @param font Betűtípus
     */
    public void registerFont(Font font) {
        loadedFonts.add(font);

        logger.info("Loaded font: {}", font.getName());
    }

    /**
     * Egyszerű szöveg rajzolása
     *
     * @param text A szöveg
     * @param x x koordináta
     * @param y y koodináta
     * @param size Betűk mérete
     * @param fontName Betűtípus neve
     * @param fontStyle Szöveg stílusa
     * @param color Szöveg színe
     * @param yBottom y origó helyzete
     */
    public void drawText(String text, float x, float y, int size, String fontName, FontStyle fontStyle, Color color, boolean yBottom) {
        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return;
        }

        if(yBottom) {
            y += size;
        }

        font.setColor(color);
        font.draw(spriteBatch, text, x, y);
    }

    /**
     * Középre igazított szöveg rajzolása
     *
     * @param text A szöveg
     * @param x x koordináta
     * @param y í koordináta
     * @param size Betűk mérete
     * @param fontName Betűtípus neve
     * @param fontStyle Szöveg stílusa
     * @param color Szöveg színe
     */
    public void drawCenteredText(String text, float x, float y, int size, String fontName, FontStyle fontStyle, Color color) {
        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return;
        }

        glyphLayout.setText(font, text);

        x -= glyphLayout.width / 2;
        y += glyphLayout.height / 2;

        font.setColor(color);
        font.draw(spriteBatch, text, x, y);
    }

    /**
     * Jobbra igazított szöveg rajzolása
     *
     * @param text A szöveg
     * @param x x koordináta
     * @param y y koodináta
     * @param size Betűk mérete
     * @param fontName Betűtípus neve
     * @param fontStyle Szöveg stílusa
     * @param color Szöveg színe
     * @param yBottom y origó helyzete
     */
    public void drawRightText(String text, float x, float y, int size, String fontName, FontStyle fontStyle, Color color, boolean yBottom) {
        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return;
        }

        glyphLayout.setText(font, text);

        x -= glyphLayout.width;

        if(yBottom) {
            y += size;
        }

        font.setColor(color);
        font.draw(spriteBatch, text, x, y);
    }

    /**
     * Sortöréseket támogató szöveg rajzolása
     *
     * @see com.badlogic.gdx.utils.Align
     *
     * @param text A szöveg
     * @param x x koordináta
     * @param y y koordináta
     * @param size Betűk mérete
     * @param fontName Betűtípus neve
     * @param fontStyle Szöveg stílusa
     * @param color Szöveg színe
     * @param width Maximális szélesség
     * @param align Igazítás
     */
    public void drawWrappedText(String text, float x, float y, int size, String fontName, FontStyle fontStyle, Color color, float width, int align) {
        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return;
        }

        font.setColor(color);
        font.draw(spriteBatch, text, x, y, width, align, true);
    }

    /**
     * Megadja egy szöveg méretét
     *
     * @param text A szöveg
     * @param fontName Betűtípus
     * @param fontStyle Szöveg stílusa
     * @param size Betűk mérete
     *
     * @return Szöveg méretei pixelben
     */
    public Vector2 getTextSize(String text, String fontName, FontStyle fontStyle, int size) {
        if(text.isEmpty()) {
            return Vector2.Zero;
        }

        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return Vector2.Zero;
        }

        glyphLayout.setText(font, text);

        return new Vector2(glyphLayout.width, glyphLayout.height);
    }

    /**
     * Megadja egy szöveg méretét sortörésekkel együtt
     *
     * @see com.badlogic.gdx.utils.Align
     *
     * @param text A szöveg
     * @param fontName Betűtypus
     * @param fontStyle Szöveg stílusa
     * @param size Betűk mérete
     * @param width Maximális szélesség
     * @param align Igazítás
     *
     * @return Szöveg méretei pixelben
     */
    public Vector2 getWrappedTextSize(String text, String fontName, FontStyle fontStyle, int size, float width, int align) {
        if(text.isEmpty()) {
            return Vector2.Zero;
        }

        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return Vector2.Zero;
        }

        glyphLayout.setText(font, text, Color.WHITE, width, align, true);

        return new Vector2(glyphLayout.width, glyphLayout.height);
    }

    @Data
    private class CacheLookup {
        private final String name;
        private final FontStyle style;
        private final int size;
    }
}
