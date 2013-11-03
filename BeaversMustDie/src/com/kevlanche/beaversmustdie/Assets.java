package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public static TextureAtlas atlas;
	
	public static TextureRegion alphabet, smiley, earthCore, island, pole, silo, cloud, beaver, blood, pool, tower;
	
	public static TextureRegion shark ;
	public static TextureRegion bottom_fin_default, bottom_fin_wing, bottom_fin_dynamite;
	public static TextureRegion top_fin_default, top_fin_baloon;
	
	public static Music bg_music;

	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		alphabet = find("alphabet");
		smiley = find("smiley");
		earthCore = find("earthcore");
		island = find("island");
		pole = find("pole");
		silo = find("silo");
		cloud = find("cloud");
		shark = find("shark");
		beaver = find("beaver");
		blood = find("blood");
		pool = find("pool");
		tower = find("tower");
		
		top_fin_default = find("top_fin_default");
		top_fin_baloon = find("top_fin_baloon");
		
		bottom_fin_default = find("bottom_fin_default");
		bottom_fin_wing = find("bottom_fin_wing");
		bottom_fin_dynamite = find("bottom_fin_dynamite");
		
		bg_music = Gdx.audio.newMusic(Gdx.files.internal("data/shark_concept.wav"));
	}
	private static TextureRegion find(String name) {
		return atlas.findRegion(name);
	}

	public static void dispose() {
		atlas.dispose();
		bg_music.dispose();
	}
}
