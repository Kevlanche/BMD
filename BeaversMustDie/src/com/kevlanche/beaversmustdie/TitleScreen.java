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
	LBL mapSize;

	public static int MAP_SIZE = 13;
	private static int MAP_SIZE_MIN = 6;
	private static int MAP_SIZE_MAX = 20;
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
		
		LBL seedMsg = new LBL("Enter a seed (or leave empty for random)", 2.0f);
		seedMsg.position(Mane.WIDTH/2, Mane.HEIGHT*0.6f, 0.5f, 0.5f);
		stage.addActor(seedMsg);
		
		seed = new LBL("**", 2.0f);
		seed.position(Mane.WIDTH/2, Mane.HEIGHT*0.55f, 0.5f, 0.5f);
		stage.addActor(seed);
		

		LBL mapSizeMsg = new LBL("Use arrow keys (left & right) to change map size", 2.0f);
		mapSizeMsg.position(Mane.WIDTH/2, Mane.HEIGHT*0.4f, 0.5f, 0.5f);
		stage.addActor(mapSizeMsg);
		
		mapSize = new LBL("*"+ mapSizeMsg() +"*", 2.0f);
		mapSize.position(Mane.WIDTH/2, Mane.HEIGHT*0.35f, 0.5f, 0.5f);
		stage.addActor(mapSize);
		
		LBL startMsg = new LBL("Press enter to start!", 2.0f);
		startMsg.position(Mane.WIDTH/2, Mane.HEIGHT*0.2f, 0.5f, 0.5f);
		stage.addActor(startMsg);


		
		postProcessor.addEffect( ripple );
		
	}
	private String mapSizeMsg() {
		if (MAP_SIZE < 7) return "small--";
		if (MAP_SIZE > MAP_SIZE_MAX) return "large++";

		switch (MAP_SIZE) {
		case 7: return "small-";
		case 8: return "small";
		case 9: return "small+";
		case 10: return "small++";
		case 11: return "medium--";
		case 12: return "medium-";
		case 13: return "medium";
		case 14: return "medium+";
		case 15: return "medium++";
		case 16: return "large--";
		case 17: return "large-";
		case 18: return "large";
		case 19: return "large+";
		case 20: return "large++";
		
		
		}
		
		return "medium";
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
		} else if (kc == Keys.LEFT) {
			MAP_SIZE = MathUtils.clamp(MAP_SIZE-1, MAP_SIZE_MIN, MAP_SIZE_MAX);
			mapSize.setText("*"+mapSizeMsg()+"*");
		} else if (kc == Keys.RIGHT) {
			MAP_SIZE = MathUtils.clamp(MAP_SIZE+1, MAP_SIZE_MIN, MAP_SIZE_MAX);
			mapSize.setText("*"+mapSizeMsg()+"*");
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
