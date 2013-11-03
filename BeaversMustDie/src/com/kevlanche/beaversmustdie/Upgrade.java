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
		float radius = 1f;
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
		cs.setPosition(new Vector2(radius, radius));
		cs.setRadius(radius);
		
		
		fd.shape = cs;
		
		body.createFixture(fd);
		cs.dispose();
		super.initPhysicsBody(body);
		
		setSize(2*scale*Mane.PTM_RATIO, 2*scale*Mane.PTM_RATIO);
		setOrigin(0.0f, 0.0f);
	}
	public int getType(){
		return type;
		 
	 }
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		switch(type){
		case 1:
			super.draw(batch, parentAlpha, Assets.bottom_fin_wing);
			break;
		case 2:
			super.draw(batch, parentAlpha, Assets.bottom_fin_dynamite);
			break;
		case 3:
			super.draw(batch, parentAlpha, Assets.top_fin_baloon);
			break;
		}
	}
	
}
