package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Mane extends Game {

	public final static int WIDTH = 1680/2;
	public final static int HEIGHT = 1050/2;
	
	public static final boolean PHYSICS_DEBUG = true;
	
	public static final float PTM_RATIO = 32.0f;
	
	
	private static Mane instance; 

	@Override
	public void create() {
		instance = this;
		Assets.load();
		setScreen(new TitleScreen());
	}

	@Override
	public void dispose() {
		Assets.dispose();
		getScreen().dispose();
	}
	
	public static void startGame(long seed) {
		instance.getScreen().dispose();
		instance.setScreen(new GameScreen(seed));
	}
	
	public static void startTitleScreen() {
		instance.getScreen().dispose();
		instance.setScreen(new TitleScreen());
	}
}
