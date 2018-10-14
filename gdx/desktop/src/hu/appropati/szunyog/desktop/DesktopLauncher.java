package hu.appropati.szunyog.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.appropati.szunyog.Trainer;
import hu.appropati.szunyog.input.text.TextInputListener;
import hu.appropati.szunyog.input.text.TextInputProvider;

public class DesktopLauncher implements TextInputProvider {
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

    }

    @Override
    public void removeListener(TextInputListener textInputListener) {

    }

    @Override
    public void openTextInput(String placeholder, InputType type, int maxChars) {

    }

    @Override
    public void closeTextInput() {

    }
}
