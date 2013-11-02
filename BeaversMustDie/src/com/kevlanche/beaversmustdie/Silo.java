package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Silo extends PhysicsActor {

	public Silo(World world, Island placeOn, float placementX) {
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		
		Vector2 pos = placeOn.physicsBody.getPosition();
		float ang = placeOn.physicsBody.getAngle();
		

		float islandW = placeOn.getPhysicsWidth();
		float islandH = placeOn.getPhysicsHeight();
		
		float scale = 2.0f;
		Vector2 off = new Vector2(islandW * (0.2f + placementX * 0.6f), islandH);
		off.rotate(MathUtils.radiansToDegrees * ang);
		
		bd.position.set(new Vector2(
									pos.x + off.x, 
									pos.y + off.y ));
		bd.angle = ang;
		
		Body body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = Collision.SILO;
		fd.filter.maskBits = Collision.SHARK;
		fd.isSensor = true;
		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		
		
		float[] vertices = new float[]{
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f
		};
		for (int i=0; i<vertices.length; i += 2) {
			vertices[i] *= scale;
			vertices[i + 1] *= scale;
		}
		
		
		PolygonShape ps = new PolygonShape();
		ps.set(vertices);
		fd.shape = ps;
		
		body.createFixture(fd);
		
		ps.dispose();
		
		super.initPhysicsBody(body);
		
		setSize(scale*Mane.PTM_RATIO, scale*Mane.PTM_RATIO);
		setOrigin(0.0f, 0.0f);
		
	}
	

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha, Assets.silo);
	}
	
}
