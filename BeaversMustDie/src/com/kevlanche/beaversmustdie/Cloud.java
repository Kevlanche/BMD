package com.kevlanche.beaversmustdie;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cloud extends Actor{
	float posX;
	float posY;
	
	float angle;
	float length;
	
	float rotSpeed;
	float time;
	
	public Cloud(float angle, float length){
		//posY = 1500+200*randY;
		//posX = 10000*randX;
		this.angle = angle;
		this.length = length;
		
		this.rotSpeed = MathUtils.random(1.5f, 2.5f) * MathUtils.random(0, 1) > 0 ? 1 : -1;
		this.time = 0.0f;
//		posX = MathUtils.cosDeg(angle)*length;
//		posY = MathUtils.sinDeg(angle)*length;
//		float ang = MathUtils.atan2(posY, posX) + MathUtils.PI;
//		setRotation(MathUtils.radiansToDegrees * ang + 90.0f);
	}
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		time += delta;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		//float side = Mane.PTM_RATIO/4;
		//batch.draw(Assets.pole, -side/2, 0.0f, side/2, 0.0f, side, 1000*Mane.PTM_RATIO, 1.0f, 1.0f, getRotation());
		
		float modifiedAngle = angle + time*rotSpeed;
		
		posX = MathUtils.cosDeg( modifiedAngle )*(length + Mane.PTM_RATIO*Water.WATER_RADIUS);
		posY = MathUtils.sinDeg( modifiedAngle )*(length + Mane.PTM_RATIO*Water.WATER_RADIUS);
		
		float ang = MathUtils.atan2(posY, posX) + MathUtils.PI;
		setRotation(MathUtils.radiansToDegrees * ang + 90.0f);
		
		batch.draw(Assets.cloud, posX, posY, 0.0f, 0.0f, 200, 100, 1.0f, 1.0f, getRotation());
		//batch.draw(Assets.smiley, posX, posY, getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());		
	}

}
