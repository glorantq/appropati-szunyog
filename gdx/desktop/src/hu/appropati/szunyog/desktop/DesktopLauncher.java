package hu.appropati.szunyog.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.input.text.TextInputListener;
import hu.appropati.szunyog.input.text.TextInputProvider;

public class DesktopLauncher implements TextInputProvider {
    private List<TextInputListener> textInputListeners = new CopyOnWriteArrayList<>();

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int)(Trainer.WIDTH / 1.3f);
		config.height = (int)(Trainer.HEIGHT / 1.3f);
		config.title = "Appropati Szunyogfitnesz";
		config.allowSoftwareMode = true;
		config.resizable = true;
		config.vSyncEnabled = true;

		new LwjglApplication(Trainer.createTrainer(new DesktopLauncher()), config);
	}

    @Override
    public void registerListener(TextInputListener textInputListener) {
        textInputListeners.add(textInputListener);
    }

    @Override
    public void removeListener(TextInputListener textInputListener) {
        textInputListeners.remove(textInputListener);
    }

    @Override
    public void openTextInput(String placeholder, String text, InputType type, int maxChars) {
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String s) {
                textInputListeners.forEach(it -> it.textUpdated(s));
            }

            @Override
            public void canceled() {

            }
        }, placeholder, text, placeholder);
    }

    @Override
    public void closeTextInput() {

    }
}
