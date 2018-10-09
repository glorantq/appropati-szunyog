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

    public void registerFont(Font font) {
        loadedFonts.add(font);

        logger.info("Loaded font: {}", font.getName());
    }

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

    public void drawRightText(String text, float x, float y, int size, String fontName, FontStyle fontStyle, Color color, boolean yBottom) {
        BitmapFont font = fontCache.get(new CacheLookup(fontName, fontStyle, size));
        if(font == null) {
            return;
        }

        glyphLayout.setText(font, text);

        x -= glyphLayout.width;

        if(yBottom) {
            y += glyphLayout.height;
        }

        font.setColor(color);
        font.draw(spriteBatch, text, x, y);
    }

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

    @Data
    private class CacheLookup {
        private final String name;
        private final FontStyle style;
        private final int size;
    }
}
