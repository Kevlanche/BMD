package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class EarthCore extends PhysicsActor {

	public EarthCore(World world) {
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(-Water.EARTH_RADIUS, -Water.EARTH_RADIUS);
		
		
		Body body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = Collision.EARTH_CORE;
		fd.filter.maskBits = Collision.SHARK | Collision.ISLAND;
		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		
		CircleShape ps = new CircleShape();
		ps.setPosition(new Vector2( Water.EARTH_RADIUS, Water.EARTH_RADIUS ));
		ps.setRadius(Water.EARTH_RADIUS);
		fd.shape = ps;
		
		body.createFixture(fd);
		
		ps.dispose();
		
		super.initPhysicsBody(body);
		
		setSize(Mane.PTM_RATIO * Water.EARTH_RADIUS * 2, Mane.PTM_RATIO * Water.EARTH_RADIUS * 2);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha, Assets.earthCore);
	}
}
