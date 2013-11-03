package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.utils.ShaderLoader;

public class StoryScreen extends InputAdapter implements Screen {

	private Stage stage;
	private float totalTime = 0;
	private float[] mousePos;

	PostProcessor postProcessor;
	RippleEffect ripple;
	
	public StoryScreen() {
		mousePos = new float[]{0.0f, 0.0f};
		stage = new Stage();
		
		ShaderLoader.BasePath = "data/shaders/";
		postProcessor = new PostProcessor(false, false, true);
		
		ripple = new RippleEffect();
		stage.addActor(new BackgroundImage());
		
		LBL title = new LBL("Beavers must die!", 3.0f);
		title.position(Mane.WIDTH*0.75f, Mane.HEIGHT*0.85f, 0.5f, 0.75f);
		stage.addActor(title);
		
		title.addAction( Actions.forever( Actions.sequence( Actions.color(Color.RED, 1.0f), Actions.color(Color.BLUE, 1.0f), Actions.color(Color.MAGENTA, 1.0f))));

		
		LBL msg = new LBL("The beavers have broken the ocean agreement", 2.0f);
		msg.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.45f, 0.5f, 0.5f);
		stage.addActor(msg);
		
		LBL msg2 = new LBL("and stolen most of the water!", 2.0f);
		msg2.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.4f, 0.5f, 0.5f);
		stage.addActor(msg2);
		
		LBL msg3 = new LBL("Now the sharks are pissed and are", 2.0f);
		msg3.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.35f, 0.5f, 0.5f);
		stage.addActor(msg3);
		
		LBL msg4 = new LBL("planning to kill the beavers!", 2.0f);
		msg4.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.3f, 0.5f, 0.5f);
		stage.addActor(msg4);
		
		LBL msg5 = new LBL("and take back the water!", 2.0f);
		msg5.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.25f, 0.5f, 0.5f);
		stage.addActor(msg5);
		
		
		LBL startMsg = new LBL("Press enter to start tutorial!", 2.0f);
		startMsg.position(Mane.WIDTH*0.4f, Mane.HEIGHT*0.125f, 0.5f, 0.5f);
		stage.addActor(startMsg);
		
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
			
				Mane.startGame(555, true);
			
			
		} return true;
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
