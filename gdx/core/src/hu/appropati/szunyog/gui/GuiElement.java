package hu.appropati.szunyog.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Egy GUI építőeleme
 *
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
public interface GuiElement {
    void create();
    void destroy();
    void render(SpriteBatch spriteBatch, float delta);

    float getWidth();
    float getHeight();
    float getX();
    float getY();
}
