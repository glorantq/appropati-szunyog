package hu.appropati.szunyog;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Trainer extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;

	private Viewport fitViewport;
	private Viewport fillViewport;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		fitViewport = new FitViewport(720, 1280);
		fillViewport = new FillViewport(720, 1280);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		fillViewport.apply(true);
		batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
		batch.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		batch.draw(img, 0, 0, fillViewport.getWorldWidth(), fillViewport.getWorldHeight());
		batch.end();

		fitViewport.apply(true);
		batch.setProjectionMatrix(fitViewport.getCamera().combined);
        batch.begin();
		batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		batch.draw(img, fitViewport.getWorldWidth() / 2 - img.getWidth() / 2, fitViewport.getWorldHeight() / 2 - img.getHeight() / 2);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

    @Override
    public void resize(int width, int height) {
        fillViewport.update(width, height);
        fitViewport.update(width, height);
    }
}
