package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.utils.ShaderLoader;

public class TitleScreen extends InputAdapter implements Screen {

	private Stage stage;
	private float totalTime = 0;
	private float[] mousePos;
	
	LBL seed;
	PostProcessor postProcessor;
	RippleEffect ripple;
	
	public TitleScreen() {
		mousePos = new float[]{0.0f, 0.0f};
		
		stage = new Stage();
		
		ShaderLoader.BasePath = "data/shaders/";
		postProcessor = new PostProcessor(false, false, true);
		
		ripple = new RippleEffect();
		stage.addActor(new BackgroundImage());
		
		LBL title = new LBL("Beavers must die!", 3.0f);
		title.position(Mane.WIDTH/2, Mane.HEIGHT*0.75f, 0.5f, 0.75f);
		stage.addActor(title);
		
		LBL seedMsg = new LBL("Enter a seed (or leave empty for random) and press enter to start!", 2.0f);
		seedMsg.position(Mane.WIDTH/2, Mane.HEIGHT*0.5f, 0.5f, 0.75f);
		stage.addActor(seedMsg);
		
		seed = new LBL("**", 2.0f);
		seed.position(Mane.WIDTH/2, Mane.HEIGHT*0.25f, 0.5f, 0.5f);
		stage.addActor(seed);
		
		
		postProcessor.addEffect( ripple );
		
	}
	@Override
	public void render(float delta) {
		
		totalTime += delta;
		
		ripple.setTime(totalTime);

		mousePos[0] = Gdx.input.getX() / (float) Mane.WIDTH;
		mousePos[1] = Gdx.input.getY() / (float) Mane.HEIGHT;
		
		ripple.setMousePos(mousePos);
		
		stage.act(delta);
		
		postProcessor.capture();
		  Gdx.gl.glClearColor(0, 1.0f, 1.0f, 1);
		  Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		  
		  stage.draw();		
		  postProcessor.render();
	}

	@Override
	public void resize(int width, int height) {
		  stage.setViewport(Mane.WIDTH, Mane.HEIGHT, true);
		  stage.getCamera().translate(-stage.getGutterWidth(),
		    -stage.getGutterHeight(), 0);		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() == this)
			Gdx.input.setInputProcessor(null);
	}

	

	@Override
	public boolean keyDown(int kc) {
		
		if (kc == Keys.ENTER) {
			
			if (seed.text.length() == 2) {
				MathUtils.random.setSeed( System.currentTimeMillis() );
				Mane.startGame(MathUtils.random.nextLong());
			} else
				Mane.startGame(seed.text.hashCode());
			
		} else if (kc == Keys.BACKSPACE && seed.text.length() > 2) {
			String newText = seed.text.substring(0, seed.text.length()-2) +"*";
			seed.setText(newText);
		} else if (kc >= Keys.A && kc <= Keys.Z) {
			char c = (char)('A' + (kc - Keys.A));
			String newText = seed.text.substring(0, seed.text.length()-1) + c + "*";
			seed.setText(newText);
		}
		return true;
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		postProcessor.rebind();
		
	}

	@Override
	public void dispose() {
		postProcessor.dispose();
		
	}
	
}
