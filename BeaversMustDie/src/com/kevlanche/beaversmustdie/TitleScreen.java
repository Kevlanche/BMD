package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TitleScreen extends InputAdapter implements Screen {

	private Stage stage;
	LBL seed;
	
	public TitleScreen() {
		stage = new Stage();
		
		LBL title = new LBL("Beavers must die!", 3.0f);
		title.position(Mane.WIDTH/2, Mane.HEIGHT*0.75f, 0.5f, 0.75f);
		stage.addActor(title);
		
		LBL seedMsg = new LBL("Enter a seed (or leave empty for random) and press enter to start!", 2.0f);
		seedMsg.position(Mane.WIDTH/2, Mane.HEIGHT*0.5f, 0.5f, 0.75f);
		stage.addActor(seedMsg);
		
		seed = new LBL("**", 2.0f);
		seed.position(Mane.WIDTH/2, Mane.HEIGHT*0.25f, 0.5f, 0.5f);
		stage.addActor(seed);
		
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClearColor(0, 1.0f, 1.0f, 1);
		  Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		  stage.act(delta);
		  stage.draw();		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
