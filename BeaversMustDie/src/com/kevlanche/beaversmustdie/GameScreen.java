package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kevlanche.beaversmustdie.Shark.SharkSweetAirJumpTimeReportReceiver;
import com.kevlanche.beaversmustdie.particles.ParticleEffect;
import com.kevlanche.beaversmustdie.particles.ParticlePool;

public class GameScreen extends InputAdapter implements Screen{

	private Array<Disposable> disposables;

	private float zoom;
	private Stage gameStage, guiStage;

	public Shark shark;
	
	private World physicsWorld;
	float physicsTimeBuffer;
	
	private Array<Beaver> beaversToRemove;
	private Array<Silo> silosToBoom;
	private Array<Pool> poolsToBoom;
	private Array<Upgrade> upgradesToRemove;
	private Array<Integer> islandAngles;
	
	private float waterRaiseBuffer;
	
	public GameScreen() {
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_COLOR, GL20.GL_DST_ALPHA);
	
		disposables = new Array<Disposable>();
		silosToBoom = new Array<Silo>();
		poolsToBoom = new Array<Pool>();
		upgradesToRemove = new Array<Upgrade>();
		beaversToRemove = new Array<Beaver>();
		islandAngles = new Array<Integer>();
		
		gameStage = new Stage();
		guiStage = new Stage();
		disposables.add(gameStage);
		disposables.add(guiStage);
		
		physicsWorld = new World(Vector2.Zero, false);
		new CollisionManager(physicsWorld, this);
		
		Music music = Assets.bg_music;
		music.setLooping(true);
		music.play();
		disposables.add(music);
		
		Sky sky = new Sky();
		gameStage.addActor(sky);
		disposables.add(sky);
		
		Water water = new Water();
		gameStage.addActor(water);
		disposables.add(water);
		
		for(int i =0; i<20; i++){
			Cloud cloud = new Cloud(MathUtils.random(0.0f, 360.0f) , Mane.PTM_RATIO * MathUtils.random(Water.WATER_RADIUS * 0.25f, Water.WATER_RADIUS * 1.5f));
			gameStage.addActor(cloud);
		}
		

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
		
		for (int i=0; i<20; ++i) {
			boolean notDone = true;
			int angle;
			float size= MathUtils.random(2.5f, 5.0f);
			do {

				angle = MathUtils.random(0, 360);
				if (islandAngles.size == 0) {
					islandAngles.add(MathUtils.random(0, 360));
				}
				for (int t = 0; t <= islandAngles.size; ++t) {

					if (((islandAngles.get(t) - angle) > -size/0.1f
							&& (islandAngles.get(t) - angle) < size/0.1f) && (Math.abs(t-i)<size/1.1f) ) //|| (((islandAngles.get(t) - angle) > -3
							//&& (islandAngles.get(t) - angle) < 3) && size<3 )) { //&& (Math.abs(t-i)>10) insert later to accept small angle when big height diff.
					{
						break;
					}
					if ((t + 1) == islandAngles.size) {

						notDone = false;
						islandAngles.add(angle);

					}

				}

			} while (notDone);


			Island island = addIsland((float)angle, size, 1.0f + i/10.0f, MathUtils.random(1, 5),MathUtils.random(1, 2));

			
			gameStage.addActor(new Beaver(physicsWorld, island));

		}
		
		gameStage.addActor(new EarthCore(physicsWorld));
		
		gameStage.addActor(shark);
		
		gameStage.addActor(new Upgrade(physicsWorld, new Vector2(5.0f, 5.0f),1));
		
		if (Mane.PHYSICS_DEBUG)
			gameStage.addActor(new Box2dDebug(physicsWorld));
		
		zoom = 1.0f;
	}

	private Island addIsland(float ang, float size, float len, int numSilos,int numPools) {
		Island island = new Island(physicsWorld, ang, size, len * Water.WATER_RADIUS);
		gameStage.addActor(island);
		for (int i=0; i<numSilos; ++i) {
			gameStage.addActor(new Silo(physicsWorld, island, MathUtils.random()));
		}
		for (int i=0; i<numPools; ++i) {
			gameStage.addActor(new Pool(physicsWorld, island, MathUtils.random()));
		}
		return island;

	}
	
	public void onSiloBoom(Silo s) {
		if (!silosToBoom.contains(s, true)) silosToBoom.add(s);
	}
	
	public void onPoolBoom(Pool p){
		if(!poolsToBoom.contains(p, true)) poolsToBoom.add(p);
	}
	
	public void upgrade(Upgrade u) {
	
		if(!upgradesToRemove.contains(u, true)) upgradesToRemove.add(u);
		
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
			ParticleEffect pe = ParticlePool.get();
			pe.setColor(Color.BLUE);
			float ang = s.getRotation();
			Vector2 sides = new Vector2( s.getWidth(), s.getHeight());
			sides.rotate(ang);
			Vector2 mid = new Vector2(	s.getX() + sides.x/2,
										s.getY() + sides.y/2
										);
			pe.setPosition(mid.x - s.getWidth()/2, mid.y - s.getHeight()/2);
			pe.setSize(s.getWidth(), s.getHeight());
			pe.init(Assets.smiley, 50.0f, 10);
			gameStage.addActor(pe);
			
			s.physicsBody.setActive(false);
			s.addAction(Actions.sequence( Actions.fadeOut(0.25f),
											Actions.removeActor() ));
			waterRaiseBuffer += 0.75f;
		}
		
		if (waterRaiseBuffer > 0.0f) {
			float amntRise = Math.min(waterRaiseBuffer, 2*delta);
			waterRaiseBuffer -= amntRise;
			Water.WATER_RADIUS += amntRise;
		}
				
		silosToBoom.clear();
		
		for (Pool p : poolsToBoom) {
			p.remove();
			Water.WATER_RADIUS += 0.95f;
		}
				
		poolsToBoom.clear();
		
		for (Upgrade u : upgradesToRemove) {
			u.remove();
			switch(u.getType()){
			case 1:
				shark.addJumpUpgrade();
				break;
			}
		}
		
		upgradesToRemove.clear();
		
		gameStage.act(delta);
		
		gameStage.setViewport(Mane.WIDTH/zoom, Mane.HEIGHT/zoom, true);
		gameStage.getCamera().translate(-gameStage.getGutterWidth(),
			    						-gameStage.getGutterHeight(), 0);
		gameStage.getCamera().translate(-Mane.WIDTH/(2*zoom) + shark.getX() + shark.getWidth()/2, 
										-Mane.HEIGHT/(2*zoom) + shark.getY() + shark.getHeight()/2, 0.0f);
		
		if (!Mane.PHYSICS_DEBUG) {
			float sharkAng = MathUtils.radiansToDegrees * MathUtils.atan2(shark.getY()+shark.getHeight()/2, shark.getX()+shark.getWidth()/2);// shark.getRotation() + 90.0f; //TODO räkna vinkel på sharks position
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
