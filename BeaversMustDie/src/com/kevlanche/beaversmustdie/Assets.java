package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public static TextureAtlas atlas;
	
	public static TextureRegion alphabet, smiley, earthCore, island, pole, silo, cloud, beaver, blood, pool, tower, top_fin_rocket;
	
	public static TextureRegion shark ;
	public static TextureRegion bottom_fin_default, bottom_fin_wing, bottom_fin_dynamite;
	public static TextureRegion top_fin_default, top_fin_baloon;
	
	public static Music bg_music;
	public static SoundRef powerup, boom, water_break, beaver_death, dynamite;
	
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
		top_fin_rocket = find("rocket");
		
		top_fin_default = find("top_fin_default");
		top_fin_baloon = find("top_fin_baloon");
		
		bottom_fin_default = find("bottom_fin_default");
		bottom_fin_wing = find("bottom_fin_wing");
		bottom_fin_dynamite = find("bottom_fin_dynamite");

		water_break = load("water_break");
		boom = load("boom");
		powerup = load("powerup");
		beaver_death = new SoundRef( Gdx.audio.newSound(Gdx.files.internal("data/aaaah.ogg")) ) ;
		dynamite = load("dynamite");

		bg_music = Gdx.audio.newMusic(Gdx.files.internal("data/shark_concept.wav"));
	}
	private static TextureRegion find(String name) {
		return atlas.findRegion(name);
	}
	private static SoundRef load(String file) {
		Sound ret = Gdx.audio.newSound(Gdx.files.internal("data/"+file+".wav"));
		return new SoundRef(ret);
	}
	
	public static class SoundRef {
		public Sound sound;
		
		public SoundRef(Sound s) {
			this.sound = s;
		}
		
		public void play() {
			sound.play(0.5f);
		}
		public void dispose() {
			sound.dispose();
		}
	}

	public static void dispose() {
		atlas.dispose();
		bg_music.dispose();
		water_break.dispose();
		boom.dispose();
		powerup.dispose();
		
	}
}
