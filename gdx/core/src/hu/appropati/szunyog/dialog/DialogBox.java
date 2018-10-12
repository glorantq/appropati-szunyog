package hu.appropati.szunyog.dialog;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface DialogBox {
    void show();
    void render(SpriteBatch spriteBatch);
    void close();

    boolean isCloseable();
}
