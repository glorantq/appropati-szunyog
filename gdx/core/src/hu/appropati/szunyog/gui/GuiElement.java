package hu.appropati.szunyog.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GuiElement {
    void create();
    void destroy();
    void render(SpriteBatch spriteBatch, float delta);
}
