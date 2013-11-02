package com.kevlanche.beaversmustdie;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cloud extends Actor{
	float posX;
	float posY;
	
	public Cloud(float angle, float length){
		//posY = 1500+200*randY;
		//posX = 10000*randX;
		posX = MathUtils.cosDeg(angle)*length;
		posY = MathUtils.sinDeg(angle)*length;
		float ang = MathUtils.atan2(posY, posX) + MathUtils.PI;
		setRotation(MathUtils.radiansToDegrees * ang + 90.0f);
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//float side = Mane.PTM_RATIO/4;
		//batch.draw(Assets.pole, -side/2, 0.0f, side/2, 0.0f, side, 1000*Mane.PTM_RATIO, 1.0f, 1.0f, getRotation());
		batch.draw(Assets.smiley, posX, posY, 0.0f, 0.0f, 200, 100, 1.0f, 1.0f, getRotation());
		//batch.draw(Assets.smiley, posX, posY, getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());		
	}

}




