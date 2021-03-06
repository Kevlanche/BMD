package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
import com.kevlanche.beaversmustdie.particles.ParticleEffect;
import com.kevlanche.beaversmustdie.particles.ParticlePool;

public class Shark extends PhysicsActor {

//	Vector2 speed;
//	float frictionTimer;
	
	boolean inWater;
	boolean movingClockwise;
	float jumpCooldown;
	
	boolean canJump;
	float airTime;
	
	//top (passive) upgrades
	boolean ballonUpgrade;
	
	//bottom (active) upgrades
	boolean jumpUpgrade = false;
	boolean glideUpgrade = false;
	boolean speedUpgrade = false;
	
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

		fd.filter.maskBits = Collision.ISLAND | Collision.EARTH_CORE | Collision.SILO | Collision.UPGRADE | Collision.BEAVER | Collision.POOL| Collision.WATERTOWER;


		
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
			
			ParticleEffect pe = ParticlePool.get();
			pe.setColor(Color.RED);
			float ang = getRotation();
			Vector2 sides = new Vector2( getWidth(), getHeight());
			sides.rotate(ang);
			Vector2 mid = new Vector2(	getX() + sides.x/2,
										getY() + sides.y/2
										);
			pe.setPosition(mid.x - getWidth()/2, mid.y - getHeight()/2);
			pe.setSize(getWidth(), getHeight());
			pe.init(Assets.bottom_fin_dynamite, 50.0f, 5);
			getParent().addActor(pe);
			
			Assets.dynamite.play();
		}
		
		jumpCooldown -= delta;
		
		Vector2 trans = new Vector2(mvx, 0.0f);

		
		if (distFromCenter < Water.WATER_RADIUS) { //only y-axis control in water
			
			
			if (!inWater) {
				inWater = true;
				jumpCooldown = Math.min(jumpCooldown, 0.25f);
				listener.onSharkDidSweetJumpFor(airTime);
				
				ParticleEffect pe = ParticlePool.get();
				pe.setColor(Color.CYAN);
				float ang = getRotation();
				Vector2 sides = new Vector2( getWidth(), getHeight());
				sides.rotate(ang);
				Vector2 mid = new Vector2(	getX() + sides.x/2,
											getY() + sides.y/2
											);
				pe.setPosition(mid.x - getWidth()/2, mid.y - getHeight()/2);
				pe.setSize(getWidth(), getHeight());
				pe.init(Assets.bottom_fin_wing, 50.0f, 10, Mane.PTM_RATIO/3);
				getParent().addActor(pe);
				
				Assets.water_break.play();
				
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
				
				ParticleEffect pe = ParticlePool.get();
				pe.setColor(Color.CYAN);
				float ang = getRotation();
				Vector2 sides = new Vector2( getWidth(), getHeight());
				sides.rotate(ang);
				Vector2 mid = new Vector2(	getX() + sides.x/2,
											getY() + sides.y/2
											);
				pe.setPosition(mid.x - getWidth()/2, mid.y - getHeight()/2);
				pe.setSize(getWidth(), getHeight());
				pe.init(Assets.bottom_fin_wing, 50.0f, 10, Mane.PTM_RATIO/3);
				getParent().addActor(pe);
				
				Assets.water_break.play();
				
			} else {
				airTime += delta;
				listener.onSharkIsDoingSweetJumpFor(airTime);
			}
			
			mvy = ballonUpgrade ? -1.0f : -2.0f;
			trans.y = mvy; //don't nor()
		}
		
		
		trans.scl((speedUpgrade ? 40.0f : 20.0f) * delta);
		
		trans.rotate(getRotation());
		
		
		Vector2 vel = physicsBody.getLinearVelocity();
		vel.add(trans);
		if (glideUpgrade && Gdx.input.isKeyPressed(Keys.SPACE) && !inWater) {
			vel.rotate(-getRotation());
			vel.y = MathUtils.clamp(vel.y, -0.5f, 0.0f);
			vel.rotate(getRotation());
		}
		if(!speedUpgrade && vel.len() > 40.0f)
			vel.nor().scl(40.0f);
		else if (vel.len() > 50.0f)
			vel.nor().scl(50.0f);
		
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
		
		// BOTTOM FIN 
		
		TextureRegion bFin = glideUpgrade ? Assets.bottom_fin_wing : ( jumpUpgrade ? Assets.bottom_fin_dynamite : Assets.bottom_fin_default );

		 
		Vector2 off = new Vector2(getWidth()*0.5f - (bFin == Assets.bottom_fin_wing ? (shark.isFlipX() ? 0 : getWidth()/4) : getWidth()/8), (bFin == Assets.bottom_fin_wing ? getHeight()/3 : -getHeight()/8));
		off.rotate(ang);
		
		if (shark.isFlipX() != bFin.isFlipX())
			bFin.flip(true, false);
		batch.draw(bFin, getX()+off.x, getY() + off.y, 0.0f, 0.0f, getWidth()/4, getHeight()/(bFin == Assets.bottom_fin_wing ? 2 : 3), 1.0f, 1.0f, ang);
		
		
		// TOP FIN
		
		off.set(getWidth()*0.5f - getWidth()/8, getHeight()*0.85f);
		off.rotate(ang);
		
		TextureRegion tFin = ballonUpgrade ? Assets.top_fin_baloon : ( speedUpgrade ? Assets.top_fin_rocket: Assets.top_fin_default);
		if (shark.isFlipX() != tFin.isFlipX())
			tFin.flip(true, false);
		batch.draw(tFin, getX()+off.x, getY() + off.y, 0.0f, 0.0f, getWidth()/4, getHeight()/3, 1.0f, 1.0f, ang);
		
	}
	
	public void addJumpUpgrade() {
		glideUpgrade = false;
		jumpUpgrade = true;		
	}
	public void addGlideUpgrade() {
		glideUpgrade = true;
		jumpUpgrade = false;		
	}
	public void addSpeedUpgrade(){
		speedUpgrade = true;
		ballonUpgrade = false;
	}
		
	public void addBalloonUpgrade() {
		ballonUpgrade = true;
		speedUpgrade = false;
	}
	
}
