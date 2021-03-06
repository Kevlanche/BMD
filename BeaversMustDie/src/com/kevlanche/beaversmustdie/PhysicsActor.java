package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PhysicsActor extends Actor {

	protected Body physicsBody;
	
	
	protected PhysicsActor() {
	}
	
	protected void initPhysicsBody(Body b) {
		this.physicsBody = b;
		b.setUserData(this);
		for (Fixture f : b.getFixtureList()) {
			f.setUserData(this);
		}
	}
	
	public void act(float delta) {
		super.act(delta);
		
	}
	
	
	@Override
	public boolean remove() {
		if (super.remove()) {
			physicsBody.getWorld().destroyBody(physicsBody);
			
			return true;
		} 
		return false;
	}

	@Override
	public float getX() {
		return physicsBody.getPosition().x * Mane.PTM_RATIO;
	}
	@Override
	public float getY() {
		return physicsBody.getPosition().y * Mane.PTM_RATIO;
	}
	
	@Override
	public void setX(float x) {
		physicsBody.setTransform(x / Mane.PTM_RATIO, getY(), physicsBody.getAngle());
	}
	@Override
	public void setY(float y) {
		physicsBody.setTransform(getX(), y / Mane.PTM_RATIO, physicsBody.getAngle());
	}
	@Override
	public void setPosition(float x, float y) {
		physicsBody.setTransform(x / Mane.PTM_RATIO, y / Mane.PTM_RATIO, physicsBody.getAngle());
	}
	@Override
	public void translate (float x, float y) {
		setPosition(getX()+x, getY()+y);
	}
	@Override
	public float getRotation() {
		return physicsBody.getAngle() * MathUtils.radiansToDegrees;
	}
	@Override
	public void setRotation(float rot) {
		physicsBody.setTransform(physicsBody.getPosition(), rot * MathUtils.degreesToRadians);
	}
	

	public void draw(SpriteBatch batch, float parentAlpha, TextureRegion tr) {
		batch.draw(tr, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
