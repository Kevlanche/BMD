package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;

public class Mane extends Game {

	public final static int WIDTH = 1680/2;
	public final static int HEIGHT = 1050/2;
	
	public static final boolean PHYSICS_DEBUG = false;
	
	public static final float PTM_RATIO = 32.0f;
	
	
	private static Mane instance; 

	@Override
	public void create() {
		instance = this;
		Assets.load();
		setScreen(new TitleScreen());
		
		Music music = Assets.bg_music;
		music.setLooping(true);
		music.play();
	}

	@Override
	public void dispose() {
		Assets.dispose();
		getScreen().dispose();
	}
	
	public static void startGame(long seed) {
		System.out.println("Starting new game with seed " + seed);
		instance.getScreen().dispose();
		instance.setScreen(new GameScreen(seed));
	}
	
	public static void startTitleScreen() {
		instance.getScreen().dispose();
		instance.setScreen(new TitleScreen());
	}
}
