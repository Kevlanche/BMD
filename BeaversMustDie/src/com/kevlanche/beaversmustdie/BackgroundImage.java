package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundImage extends Actor {

	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		batch.draw(Assets.beaver, 0, 0, 0.0f, 0.0f, Mane.WIDTH, Mane.HEIGHT, 1.0f, 1.0f, 0);
	}
	
}
