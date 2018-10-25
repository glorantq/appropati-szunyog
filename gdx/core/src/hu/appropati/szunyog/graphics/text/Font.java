package hu.appropati.szunyog.graphics.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import lombok.Getter;

/**
 * Egy játék által használható TrueType betűtípus
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
@Getter
public class Font {
    private static final String CHARSET = "0123456789qwertzuiopőúasdfghjkléáűíyxcvbnmöüóÖÜÓQWERTZUIOPŐÚASDFGHJKLÉÁŰÍYXCVBNM,.-?:_;>*\\|$ß@&#><{}'\"+!%/=()©";

    private final String name;

    private final FileHandle normalHandle;
    private final FileHandle italicHandle;
    private final FileHandle boldHandle;

    public Font(String name, String normalPath, String italicPath, String boldPath) {
        this.normalHandle = Gdx.files.internal(normalPath);
        this.italicHandle = Gdx.files.internal(italicPath);
        this.boldHandle = Gdx.files.internal(boldPath);

        this.name = name;
    }

    /**
     * LibGDX által használható betűtípust generál egy megadott stílusra és méretre
     * @param style Stílus
     * @param size Méret
     * @return Rajzolásra kész betűtípus
     */
    BitmapFont getFont(FontStyle style, int size) {
        FileHandle handle = getHandle(style);
        if(handle == null || !handle.exists()) {
            throw new UnsupportedOperationException("This font doesn't support the " + style.name() + " style!");
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);

        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = size;
        freeTypeFontParameter.characters = CHARSET;
        freeTypeFontParameter.color = Color.WHITE;
        freeTypeFontParameter.minFilter = Texture.TextureFilter.Linear;
        freeTypeFontParameter.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = generator.generateFont(freeTypeFontParameter);

        generator.dispose();
        return font;
    }

    /**
     * A fájl egy adott stílushoz
     * @param style Stílus
     * @return .ttf fájl
     */
    private FileHandle getHandle(FontStyle style) {
        switch (style) {
            case NORMAL:
                return normalHandle;

            case BOLD:
                return boldHandle;

            case ITALIC:
                return italicHandle;

            default:
                return null;
        }
    }
}
