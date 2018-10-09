package hu.appropati.szunyog.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.appropati.szunyog.Trainer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int)(Trainer.WIDTH / 1.3f);
		config.height = (int)(Trainer.HEIGHT / 1.3f);
		config.title = "Appropati Szunyogfitnesz";
		config.allowSoftwareMode = true;
		config.resizable = true;
		config.vSyncEnabled = true;

		new LwjglApplication(Trainer.getTrainer(), config);
	}
}
