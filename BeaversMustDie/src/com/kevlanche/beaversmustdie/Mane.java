package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Mane extends Game {

	public final static int WIDTH = 1680/2;
	public final static int HEIGHT = 1050/2;
	
	public static final boolean PHYSICS_DEBUG = false;
	
	public static final float PTM_RATIO = 32.0f;
	
	private Screen gameScreen;

	@Override
	public void create() {
		Assets.load();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		Assets.dispose();
		gameScreen.dispose();
	}
}
