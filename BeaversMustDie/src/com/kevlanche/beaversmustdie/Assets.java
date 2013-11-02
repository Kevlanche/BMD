package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static TextureAtlas atlas;
	public static TextureRegion alphabet, smiley, earthCore, island, pole, silo;

	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("data/pack.atlas"));
		alphabet = find("alphabet");
		smiley = find("smiley");
		earthCore = find("earthcore");
		island = find("island");
		pole = find("pole");
		silo = find("silo");
	}
	private static TextureRegion find(String name) {
		return atlas.findRegion(name);
	}

	public static void dispose() {
		atlas.dispose();
	}
}
