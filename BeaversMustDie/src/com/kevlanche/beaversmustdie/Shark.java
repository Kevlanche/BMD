package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Shark extends PhysicsActor {

//	Vector2 speed;
//	float frictionTimer;
	
	boolean inWater;
	float jumpCooldown;
	
	boolean canJump;
	float airTime;
	SharkSweetAirJumpTimeReportReceiver listener;
	
	public interface SharkSweetAirJumpTimeReportReceiver {
		public void onSharkDidSweetJumpFor(float duration);
		public void onSharkIsDoingSweetJumpFor(float duration);
	}
	
	public Shark(World world, SharkSweetAirJumpTimeReportReceiver listener) {
		

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(0.0f, 10.0f);
		bd.linearDamping = 0.2f;
		
		Body body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = Collision.SHARK;
		fd.filter.maskBits = Collision.ISLAND | Collision.EARTH_CORE | Collision.SILO;
		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		
		CircleShape ps = new CircleShape();
		ps.setPosition(new Vector2(0.5f, 0.5f));
		ps.setRadius(0.5f);
		fd.shape = ps;
		
		body.createFixture(fd);
		
		ps.dispose();
		
		super.initPhysicsBody(body);
		
		setSize(Mane.PTM_RATIO, Mane.PTM_RATIO);
//		setOrigin(getWidth()/2, getHeight()/2);
		inWater = true;
		canJump = true;
		this.listener = listener;
//		speed = new Vector2();
//		frictionTimer = 0.0f;
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		
		airTime += delta;
		
		float distFromCenter = physicsBody.getPosition().len();//(float)Math.hypot(getX()+getWidth()/2, getY()+getHeight()/2) / Mane.PTM_RATIO;
		
		float mvx = 0.0f;
		float mvy = 0.0f; //-MathUtils.clamp(distFromCenter/Water.WATER_RADIUS, 0.5f, 1.5f); //gravity, bitch!
		
		if (Gdx.input.isKeyPressed(Keys.A))
			mvx--;
		if (Gdx.input.isKeyPressed(Keys.D))
			mvx++;
		
		if (canJump && Gdx.input.isKeyPressed(Keys.SPACE)) {
			Vector2 speed = physicsBody.getLinearVelocity();
			Vector2 jumpAdd = new Vector2(0.0f, 20.0f);
			jumpAdd.rotate(getRotation());
			speed.add(jumpAdd);
			physicsBody.setLinearVelocity(speed);
			canJump = false;
			jumpCooldown = 0.5f;
		}
		
		jumpCooldown -= delta;
		
		Vector2 trans = new Vector2(mvx, 0.0f);
		
		if (distFromCenter < Water.WATER_RADIUS) { //only y-axis control in water
			
			
			if (!inWater) {
				inWater = true;
				jumpCooldown = Math.min(jumpCooldown, 0.25f);
				listener.onSharkDidSweetJumpFor(airTime);
			} else {
				if (jumpCooldown <= 0.0f) {
					canJump = true;
				}
			}
			
			if (Gdx.input.isKeyPressed(Keys.S))
				mvy--;
			if (Gdx.input.isKeyPressed(Keys.W))
				mvy++;
			
			trans.y = mvy;
			trans.nor();
			
		} else {
			
			if (inWater) {
				inWater = false;
				airTime = delta;
			} else {
				airTime += delta;
				listener.onSharkIsDoingSweetJumpFor(airTime);
			}
			
			mvy = -2.0f;
			trans.y = mvy; //don't nor()
		}
		
		
		
		trans.scl(20.0f * delta);
		
//		frictionTimer += delta;
//		if (frictionTimer > 0.05f) {
//			
//			speed.scl( (float)Math.pow(0.99f, (int)(frictionTimer/0.05f)) );
//			frictionTimer %= 0.05f;
//		}
//		
//		speed.x += trans.x;
//		speed.y += trans.y;
//		
//		if (speed.len() > 10.0f)
//			speed.nor().scl(10.0f);
//		
//		trans.set(speed);
		
		trans.rotate(getRotation());
		
//		translate(trans.x, trans.y);
		Vector2 vel = physicsBody.getLinearVelocity();
		vel.add(trans);
		if (vel.len() > 20.0f)
			vel.nor().scl(20.0f);
		physicsBody.setLinearVelocity(vel);
		
//		distFromCenter = (float)Math.hypot(getX()+getWidth()/2, getY()+getHeight()/2) / Mane.PTM_RATIO;
//		
//		if (distFromCenter < Water.EARTH_RADIUS) { //move out of the earth's core, dummy!
//			trans.set(0.0f, Mane.PTM_RATIO * (Water.EARTH_RADIUS-distFromCenter));
//			trans.rotate(getRotation());
//			translate(trans.x, trans.y);
//			speed.y = Math.max(0.0f, speed.y);
//		}
		
		Vector2 pos = physicsBody.getPosition();
		float ang = MathUtils.atan2(pos.y, pos.x) + MathUtils.PI; // MathUtils.atan2(getY()+getHeight()/2, getX()+getWidth()/2)+ MathUtils.PI;
		setRotation(MathUtils.radiansToDegrees * ang + 90.0f);
	
		
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha, Assets.smiley);
	}
	
}