package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	private Array<WaterTower> towerToBoom;
	private Array<Upgrade> upgradesToRemove;
	private Array<Integer> islandAngles;
	private int beaversKilled;

	private float waterRaiseBuffer;


	private LBL fpsLabel;

	private float totalTime;
	private int waterSources;
	private LBL waterSourceLbl;


	boolean isDisposed = false;
	long seed;


	GameShaker shaker;
	
	private LBL TutLbl;

	private boolean tut;

	public GameScreen(long seed, boolean tut) {

		Water.WATER_RADIUS = 25.0f;

		this.tut = tut; 
		this.seed = seed;
		//		Gdx.gl.glEnable(GL20.GL_BLEND);
		//		Gdx.gl.glBlendFunc(GL20.GL_SRC_COLOR, GL20.GL_DST_ALPHA);

		disposables = new Array<Disposable>();
		silosToBoom = new Array<Silo>();
		poolsToBoom = new Array<Pool>();
		towerToBoom = new Array<WaterTower>();
		upgradesToRemove = new Array<Upgrade>();
		beaversToRemove = new Array<Beaver>();
		islandAngles = new Array<Integer>();
		beaversKilled=0;

		gameStage = new Stage();
		guiStage = new Stage();
		disposables.add(gameStage);
		disposables.add(guiStage);

		physicsWorld = new World(Vector2.Zero, false);
		new CollisionManager(physicsWorld, this);


		Sky sky = new Sky();
		gameStage.addActor(sky);
		disposables.add(sky);

		Water water = new Water();
		gameStage.addActor(water);
		disposables.add(water);
		
		shaker = new GameShaker();
		gameStage.addActor(shaker);

		for(int i =0; i<20; i++){
			Cloud cloud = new Cloud(MathUtils.random(0.0f, 360.0f) , Mane.PTM_RATIO * MathUtils.random(Water.WATER_RADIUS * 0.25f, Water.WATER_RADIUS * 1.5f),MathUtils.random(1.0f,4.0f));
			gameStage.addActor(cloud);
		}


		//		final LBL sharkTimeLbl = new LBL("No jump yet!", 2.0f);
		//		sharkTimeLbl.position(Mane.WIDTH*0.95f, Mane.HEIGHT - Mane.WIDTH*0.05f, 1.0f, 1.0f);
		//		guiStage.addActor(sharkTimeLbl);


		if(tut){
			TutLbl = new LBL("Try to get all the water silos around the map.", 2.0f);

			TutLbl.position(Mane.WIDTH*0.95f, Mane.HEIGHT - Mane.WIDTH*0.20f, 1.0f, 1.0f);

			guiStage.addActor(TutLbl);
		}

		fpsLabel = new LBL("1338 FPS", 2.0f);
		fpsLabel.position(Mane.WIDTH * 0.05f, Mane.HEIGHT - Mane.WIDTH*0.1f, 0.0f, 1.0f);

		guiStage.addActor(fpsLabel);

		shark = new Shark(physicsWorld, new SharkSweetAirJumpTimeReportReceiver() {

			@Override
			public void onSharkIsDoingSweetJumpFor(float duration) {
				//				String format = Float.toString(duration);
				//				if (format.length() > 5) format = format.substring(0, 4);
				//				sharkTimeLbl.setText( ""+format );
			}

			@Override
			public void onSharkDidSweetJumpFor(float duration) {
				//				String format = Float.toString(duration);
				//				if (format.length() > 5) format = format.substring(0, 4);
				//				sharkTimeLbl.setText( format +"!");
				if (duration > 5) guiStage.addActor( constructUpgradeLabel(randomLowJumpMessage(), Color.YELLOW));

			}
		});

		MathUtils.random.setSeed(seed);

		if(!tut){
			for (int i=0; i<TitleScreen.MAP_SIZE/2; ++i) {
				boolean notDone = true;
				int angle;
				float size= MathUtils.random(2.5f, 5.0f);

				do {
					//
					angle = MathUtils.random(0, 360);
					boolean angleOK = true;
					for(int t = 0; t < islandAngles.size; t++) {
						int a = islandAngles.get(t);
						if(Math.abs(a - angle) < size/0.1f && Math.abs((a + 180)%360 - (angle + 180)%360) < size/0.1f && (Math.abs(t-i)<size/1.1f)) {
							angleOK = false;
							break;
						}
					}

					if(angleOK) {
						islandAngles.add(angle);
						notDone = false;
					}	

				} while (notDone);



				Island island = addIsland((float)angle, size, 1.0f + i/10.0f, MathUtils.random(2, 3));


				gameStage.addActor(new Beaver(physicsWorld, island));
			}
		}
		else{
			for (int i=1; i<6; ++i) {
				Island island = addIsland(40.0f*i, 4.0f, 0.9f + i/15.0f, 1);
				gameStage.addActor(new Beaver(physicsWorld, island));
			}


		}

		waterSourceLbl = new LBL(waterSources+" water sources remaining", 2.0f);
		waterSourceLbl.position(Mane.WIDTH*0.05f, Mane.HEIGHT - Mane.WIDTH*0.05f, 0.0f, 1.0f);
		guiStage.addActor(waterSourceLbl);

		gameStage.addActor(new EarthCore(physicsWorld));

		gameStage.addActor(shark);


		if(!tut){
			Array<Float> pos = new Array<Float>(8);
			for(int r=0; r<8;++r){
				while(true){
					float p = MathUtils.random(-24.0f, 24.0f);
					if(p<-4.5f||p>4.5f){
						pos.add(p);
						break;
					}
				}

			}
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(pos.get(0), pos.get(1)),1));
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(pos.get(2), pos.get(3)),2));
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(pos.get(4), pos.get(5)),3));

			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(pos.get(6), pos.get(7)),4));

		}
		else{
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(9.0f, 27.0f),1));
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(26.0f, 10.0f),2));
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(-7.0f, 32.0f),3));
			gameStage.addActor(new Upgrade(physicsWorld, new Vector2(7.0f, 7.0f),4));
		}

		if (Mane.PHYSICS_DEBUG)
			gameStage.addActor(new Box2dDebug(physicsWorld));

		zoom = 1.0f;
	}

	private Island addIsland(float ang, float size, float len, int numWaterSources) {
		Island island = new Island(physicsWorld, ang, size, len * Water.WATER_RADIUS);
		gameStage.addActor(island);
		for (int i=0; i<numWaterSources; ++i) {
			switch (MathUtils.random(0, 2)) {
			case 0:
				gameStage.addActor(new Silo(physicsWorld, island, MathUtils.random()));
				break;
			case 1:
				gameStage.addActor(new Pool(physicsWorld, island, MathUtils.random()));
				break;
			case 2:
				gameStage.addActor(new WaterTower(physicsWorld, island, MathUtils.random()));
				break;
			}
		}
		this.waterSources += numWaterSources;

		return island;

	}

	private static String randomLowJumpMessage() {

		switch (MathUtils.random(0, 10)) {
		case 0: return "weeee";
		case 1: return "180 sharkspin";
		case 2: return "flying shark? madness!";
		case 3: return "sweet jump bro";
		case 4: return "cool";
		case 5: return "*-*";
		case 6: return "A toaster would be proud of that jump";
		case 7: return "it's raining sharks!";
		case 8: return "beep boop";
		case 9: return "no hands!";
		case 10: return "nobody expects the spanish flying shark";
		case 11: return "360 noshark";

		}

		return null;
	}

	public void onSiloBoom(Silo s) {
		if (!silosToBoom.contains(s, true)) silosToBoom.add(s);
	}

	public void onPoolBoom(Pool p){
		if(!poolsToBoom.contains(p, true)) poolsToBoom.add(p);
	}

	public void onTowerBoom(WaterTower t){
		if(!towerToBoom.contains(t, true)) towerToBoom.add(t);
	}

	public void upgrade(Upgrade u) {

		if(!upgradesToRemove.contains(u, true)) upgradesToRemove.add(u);

	}

	public void killBeaver(Beaver beaver) {

		if(!beaversToRemove.contains(beaver, true)) beaversToRemove.add(beaver);

	}

	private static final float TIME_STEP = 1.0f / 60.0f;

	private void rmPool(PhysicsActor s, float waterAmount) {
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
		pe.init(Assets.shark, 50.0f, 10);
		gameStage.addActor(pe);

		s.physicsBody.setActive(false);
		s.addAction(Actions.sequence( Actions.fadeOut(0.25f),
				Actions.removeActor() ));
		waterRaiseBuffer += waterAmount;

		--waterSources;
		if (waterSources == 0) {
			String format = Float.toString(totalTime);
			if (format.length() > 5) format = format.substring(0, 4);
			waterSourceLbl.setText( "Finished in "+format+" seconds!\nPress Q to return to title screen or R to go again" );
			TutLbl.setText("Congrats you beat the tutorial! time to play the real game!");
		} else {
			waterSourceLbl.setText(waterSources+" water sources remaining");
		}


		shaker.shake();
		Assets.splash.play();
	}

	private void rmBeaver(PhysicsActor s) {
		ParticleEffect pe = ParticlePool.get();
		pe.setColor(Color.RED);
		float ang = s.getRotation();
		Vector2 sides = new Vector2( s.getWidth(), s.getHeight());
		sides.rotate(ang);
		Vector2 mid = new Vector2(	s.getX() + sides.x/2,
				s.getY() + sides.y/2
				);
		pe.setPosition(mid.x - s.getWidth()/2, mid.y - s.getHeight()/2);
		pe.setSize(s.getWidth(), s.getHeight());
		pe.init(Assets.blood, 250.0f, 50, Mane.PTM_RATIO/4);
		gameStage.addActor(pe);

		s.physicsBody.setActive(false);
		s.addAction(Actions.sequence( Actions.fadeOut(0.25f),
				Actions.removeActor() ));
		s.physicsBody.setActive(false);
		s.addAction(Actions.sequence( Actions.fadeOut(0.25f),
				Actions.removeActor() ));

	}

	private LBL constructUpgradeLabel(String msg, Color c) {
		LBL ret = new LBL(msg, 2.5f);
		ret.setColor(c);
		ret.position(Mane.WIDTH/2, Mane.HEIGHT/2, 0.5f, 0.5f);
		ret.addAction(Actions.sequence(
				Actions.parallel( 	Actions.moveBy(0.0f, Mane.HEIGHT/4, 2.0f, Interpolation.exp5Out),
						Actions.fadeOut(2.0f) ),
						Actions.removeActor()));
		return ret;
	}

	@Override
	public void render(float delta) {

		fpsLabel.setText(1 / delta + "");

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		totalTime += delta;
		physicsTimeBuffer += delta;

		while (physicsTimeBuffer >= TIME_STEP ) {
			physicsTimeBuffer -= TIME_STEP;
			physicsWorld.step(TIME_STEP, 4, 4);
		}

		for (Silo s : silosToBoom) {
			rmPool(s, 0.75f);
		}
		silosToBoom.clear();

		for (Pool p : poolsToBoom) {
			rmPool(p, 0.95f);
		}
		poolsToBoom.clear();


		for (WaterTower t : towerToBoom) {
			rmPool(t, 1.15f);
		}
		towerToBoom.clear();


		if (waterRaiseBuffer > 0.0f) {
			float amntRise = Math.min(waterRaiseBuffer, 2*delta);
			waterRaiseBuffer -= amntRise;
			Water.WATER_RADIUS += amntRise;
		}

		for (Upgrade u : upgradesToRemove) {



			if(tut){
				TutLbl.setText( "");
			}

			Assets.powerup.play();


			switch(u.getType()){

			case 1:
				guiStage.addActor(constructUpgradeLabel("Wing flipper unlocked", Color.GREEN));
				shark.addGlideUpgrade();
				if(tut){
					TutLbl.setText("Wing flipper lets you glide across the world like a beautiful swan");
				}
				break;
			case 2:
				guiStage.addActor(constructUpgradeLabel("Dynamite flipper unlocked", Color.GREEN));
				shark.addJumpUpgrade();
				if(tut){
					TutLbl.setText("Want to jump? Use the Dynamite flipper with SPACE to reach higher");
				}
				break;
			case 3:
				guiStage.addActor(constructUpgradeLabel("Baloon fin unlocked", Color.GREEN));
				shark.addBalloonUpgrade();
				if(tut){
					TutLbl.setText("With this umbrella fin can you fight the gravity like never before");
				}
				break;
			case 4:

				guiStage.addActor(constructUpgradeLabel("Rocket fin unlocked", Color.GREEN));
				shark.addSpeedUpgrade();
				if(tut){
					TutLbl.setText("With the rocket fin are you very fast!");
				}
			}
			u.remove();
		}
		upgradesToRemove.clear();

		for(Beaver b : beaversToRemove) {

			Assets.beaver_death.play();

			switch(beaversKilled){
			case 0:
				guiStage.addActor(constructUpgradeLabel("MURDERER!!!!!", Color.RED));
				break;

			case 1:
				guiStage.addActor(constructUpgradeLabel("BLOODY MESS!!!!!", Color.RED));
				break;

			case 2:
				guiStage.addActor(constructUpgradeLabel("MULTI KILL!!!!!", Color.RED));
				break;

			case 3:
				guiStage.addActor(constructUpgradeLabel("GENOCIDE!!!!!", Color.RED));
				break;

			case 4:
				guiStage.addActor(constructUpgradeLabel("YOU Are A MONSTER!!!!!", Color.RED));
				break;
			default:
				guiStage.addActor(constructUpgradeLabel("WILL THIS EVER STOP??!!!!!", Color.RED));
				break;

			}


			++beaversKilled;


			rmBeaver(b);
			//TODO SPLASH BLOOD EFFECT
		}
		beaversToRemove.clear();

		gameStage.act(delta);

		if (isDisposed) return;

		float r = (float) Math.sqrt(Math.pow(shark.getX()/Mane.PTM_RATIO, 2) + Math.pow(shark.getY()/Mane.PTM_RATIO, 2));

		zoom = MathUtils.clamp((0.5f + ((1 - (r / Water.WATER_RADIUS)) / 2)), 0.4f, 1f);


		gameStage.setViewport(Mane.WIDTH/zoom, Mane.HEIGHT/zoom, true);
		gameStage.getCamera().translate(-gameStage.getGutterWidth(),
				-gameStage.getGutterHeight(), 0);
		gameStage.getCamera().translate(	-Mane.WIDTH/(2*zoom) + shark.getX() + shark.getWidth()/2 + shaker.getX(), 
											-Mane.HEIGHT/(2*zoom) + shark.getY() + shark.getHeight()/2 + shaker.getY(), 0.0f);

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
		isDisposed = true;

	}



	@Override
	public boolean keyDown(int kc) {

		if (kc == Keys.Q) Mane.startTitleScreen();
		else if (kc == Keys.R) Mane.startGame(seed);

		return true;
	}

	private static class GameShaker extends Actor {
		
		public GameShaker() {
			setVisible(false);
		}
		public void shake() {
			float mvx = (MathUtils.randomBoolean() ? 1 : -1) *  MathUtils.random(0.5f, 1.25f) * Mane.PTM_RATIO;
			float mvy = (MathUtils.randomBoolean() ? 1 : -1) *  MathUtils.random(0.5f, 1.25f) * Mane.PTM_RATIO;
			addAction(Actions.sequence( 
						Actions.moveBy(-mvx/2, mvy/2, 0.1f), 
						Actions.moveBy(mvx, -mvy, 0.1f),
						Actions.moveBy(-mvx/2, mvy/2, 0.1f)
					));
		}
	}
}
