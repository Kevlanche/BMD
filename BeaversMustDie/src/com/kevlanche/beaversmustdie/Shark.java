package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Shark extends PhysicsActor {

//	Vector2 speed;
//	float frictionTimer;
	
	boolean inWater;
	boolean movingClockwise;
	float jumpCooldown;
	
	boolean canJump;
	float airTime;
	
	boolean jumpUpgrade = false;
	
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

		fd.filter.maskBits = Collision.ISLAND | Collision.EARTH_CORE | Collision.SILO | Collision.UPGRADE | Collision.BEAVER | Collision.POOL;


		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		
		PolygonShape ps = new PolygonShape();
//		ps.setPosition(new Vector2(1.0f, 0.5f));
//		ps.setRadius(0.5f);
		ps.setAsBox(1.0f, 0.5f, new Vector2(1.0f, 0.5f), 0.0f);
		
		fd.shape = ps;
		
		body.createFixture(fd);
		
		ps.dispose();
		
		super.initPhysicsBody(body);
		
		setSize(2*Mane.PTM_RATIO, Mane.PTM_RATIO);
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
		
		
		
		if (jumpUpgrade && canJump && Gdx.input.isKeyPressed(Keys.SPACE) && !inWater) {
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
		
		trans.rotate(getRotation());
		
		
		Vector2 vel = physicsBody.getLinearVelocity();
		vel.add(trans);
		if (vel.len() > 40.0f)
			vel.nor().scl(40.0f);
		
		physicsBody.setLinearVelocity(vel);
		
		Vector2 pos = physicsBody.getPosition();
		float ang = MathUtils.atan2(pos.y, pos.x) + MathUtils.PI; // MathUtils.atan2(getY()+getHeight()/2, getX()+getWidth()/2)+ MathUtils.PI;
		setRotation(MathUtils.radiansToDegrees * ang + 90.0f);
		
		if(ang != 0 && mvx != 0) {
			movingClockwise = trans.rotate(-getRotation()).x > 0;
		} else if(ang != 0) {
			movingClockwise = vel.rotate(-getRotation()).x > 0;
		}
		
		
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		TextureRegion shark = Assets.shark;
		if(movingClockwise) {
			if (shark.isFlipX())
				shark.flip(true, false);			
		} else {
			if (!shark.isFlipX())
				shark.flip(true, false);
		}
		super.draw(batch, parentAlpha, Assets.shark);
		
		float ang = getRotation();
		Vector2 off = new Vector2(getWidth()*0.5f - getWidth()/8, -getHeight()/8);
		off.rotate(ang);
		batch.draw(Assets.bottom_fin_default, getX()+off.x, getY() + off.y, 0.0f, 0.0f, getWidth()/4, getHeight()/2, 1.0f, 1.0f, ang);
	}
	
	public void addJumpUpgrade() {
		
		jumpUpgrade = true;
	}
	
}
