package hu.appropati.szunyog.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.appropati.szunyog.Trainer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)(720 / 1.3f);
		config.height = (int)(1280 / 1.3f);
		new LwjglApplication(new Trainer(), config);
	}
}
