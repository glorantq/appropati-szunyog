package hu.appropati.szunyog.input;

public interface InputHandler {
    default boolean tap(float x, float y, int count, int button) {
        return false;
    }

    default boolean longPress(float x, float y) {
        return false;
    }

    default boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    default boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }
}
