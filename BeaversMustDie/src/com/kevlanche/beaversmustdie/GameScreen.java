package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kevlanche.beaversmustdie.Shark.SharkSweetAirJumpTimeReportReceiver;

public class GameScreen extends InputAdapter implements Screen{

	private Array<Disposable> disposables;

	private float zoom;
	private Stage gameStage, guiStage;

	private Shark shark;
	
	private World physicsWorld;
	float physicsTimeBuffer;
	
	private Array<Silo> silosToBoom;
	
	public GameScreen() {
	
		disposables = new Array<Disposable>();
		silosToBoom = new Array<Silo>();
		
		gameStage = new Stage();
		guiStage = new Stage();
		disposables.add(gameStage);
		disposables.add(guiStage);
		
		physicsWorld = new World(Vector2.Zero, false);
		new CollisionManager(physicsWorld, this);
		
		Water water = new Water();
		gameStage.addActor(water);
		disposables.add(water);
		

		final LBL sharkTimeLbl = new LBL("Score:1337", 2.0f);
		sharkTimeLbl.position(Mane.WIDTH*0.05f, Mane.HEIGHT - Mane.WIDTH*0.05f, 0.0f, 1.0f);
		
		guiStage.addActor(sharkTimeLbl);
		
		shark = new Shark(physicsWorld, new SharkSweetAirJumpTimeReportReceiver() {
			
			@Override
			public void onSharkIsDoingSweetJumpFor(float duration) {
				String format = Float.toString(duration);
				if (format.length() > 5) format = format.substring(0, 4);
				sharkTimeLbl.setText( ""+format );
			}
			
			@Override
			public void onSharkDidSweetJumpFor(float duration) {
				String format = Float.toString(duration);
				if (format.length() > 5) format = format.substring(0, 4);
				sharkTimeLbl.setText( format +"!");
			}
		});
		
		for (int i=0; i<10; ++i) {
			addIsland(MathUtils.random(0.0f, 360.0f), MathUtils.random(2.5f, 5.0f), 1.0f + i/15.0f, MathUtils.random(1, 5));
		}
		
		gameStage.addActor(new EarthCore(physicsWorld));
		
		gameStage.addActor(shark);
		
		gameStage.addActor(new Upgrade(physicsWorld));
		
		if (Mane.PHYSICS_DEBUG)
			gameStage.addActor(new Box2dDebug(physicsWorld));
		
		zoom = 1.0f;
	}
	private void addIsland(float ang, float size, float len, int numSilos) {
		Island island = new Island(physicsWorld, ang, size, len * Water.WATER_RADIUS);
		gameStage.addActor(island);
		for (int i=0; i<numSilos; ++i) {
			gameStage.addActor(new Silo(physicsWorld, island, MathUtils.random()));
		}
	}
	
	public void onSiloBoom(Silo s) {
		if (!silosToBoom.contains(s, true)) silosToBoom.add(s);
	}
	
	private static final float TIME_STEP = 1.0f / 60.0f;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		physicsTimeBuffer += delta;
		
		while (physicsTimeBuffer >= TIME_STEP ) {
			physicsTimeBuffer -= TIME_STEP;
			physicsWorld.step(TIME_STEP, 4, 4);
		}
		
		for (Silo s : silosToBoom) {
			s.remove();
			Water.WATER_RADIUS += 0.75f;
		}
		
		silosToBoom.clear();
		
		gameStage.act(delta);
		
		gameStage.setViewport(Mane.WIDTH/zoom, Mane.HEIGHT/zoom, true);
		gameStage.getCamera().translate(-gameStage.getGutterWidth(),
			    						-gameStage.getGutterHeight(), 0);
		gameStage.getCamera().translate(-Mane.WIDTH/(2*zoom) + shark.getX() + shark.getWidth()/2, 
										-Mane.HEIGHT/(2*zoom) + shark.getY() + shark.getHeight()/2, 0.0f);
		
		if (!Mane.PHYSICS_DEBUG) {
			float sharkAng = shark.getRotation() + 90.0f; //TODO räkna vinkel på sharks position
			gameStage.getCamera().up.set( MathUtils.cosDeg(sharkAng), MathUtils.sinDeg(sharkAng), 0.0f);
		}
		gameStage.draw();
		
		guiStage.act(delta);
		guiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gameStage.setViewport(Mane.WIDTH, Mane.HEIGHT, true);
		guiStage.setViewport(Mane.WIDTH, Mane.HEIGHT, true);
		guiStage.getCamera().translate(	-guiStage.getGutterWidth(),
			    					-guiStage.getGutterHeight(), 0);
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
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		for (Disposable d : disposables) {
			d.dispose();
		}
		disposables.clear();
		
	}

	@Override
	public boolean scrolled(int amount) {
		
		zoom = MathUtils.clamp(zoom - amount*0.1f, 0.25f, 2.5f);
		return true;
	}
	

}
