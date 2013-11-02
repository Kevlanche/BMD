package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Upgrade extends PhysicsActor {

	private int type;


	public Upgrade(World world, Vector2 position, int type) {
		
		this.type = type; 
		float scale = 1.0f;
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.StaticBody;
		
		bd.position.set(position.x, position.y);
		
		Body body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		
		fd.density = 0.0f;
		fd.filter.categoryBits = Collision.UPGRADE;
		fd.filter.maskBits = Collision.SHARK;
		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		fd.isSensor = true;
		
		CircleShape cs = new CircleShape();
		cs.setRadius(1.0f);
		
		
		fd.shape = cs;
		
		body.createFixture(fd);
		cs.dispose();
		super.initPhysicsBody(body);
		
		setSize(scale*Mane.PTM_RATIO, scale*Mane.PTM_RATIO);
		setOrigin(0.0f, 0.0f);
	}
	public int getType(){
		return type;
		 
	 }
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha, Assets.silo);
	}
	
}
