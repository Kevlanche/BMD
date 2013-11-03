package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Island extends PhysicsActor {

	private float scale;
	
	public Island(World world, float angleDegrees, float scale, float length) {

		this.scale = scale;
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set( MathUtils.cosDeg(angleDegrees)*length, MathUtils.sinDeg(angleDegrees)*length);
		angleDegrees -= scale*2.5f;
		bd.angle = MathUtils.degreesToRadians * (angleDegrees - 90.0f);
		
		Body body = world.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = Collision.ISLAND;
		fd.filter.maskBits = Collision.SHARK | Collision.ISLAND;
		
		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		
		
		float[] vertices = new float[]{
				0.0f, 0.5f,
				0.8f, 0.0f,
				3.2f, 0.0f,
				4.0f, 0.5f,
				3.2f, 1.0f,
				0.8f, 1.0f
		};
		for (int i=0; i<vertices.length; i+=2) {
			vertices[i] *= scale;
			vertices[i+1] *= scale;
		}
		
		PolygonShape ps = new PolygonShape();
		ps.set(vertices);
		fd.shape = ps;
		
		body.createFixture(fd);
		
		ps.dispose();
		
		super.initPhysicsBody(body);
		
		setSize(4*scale*Mane.PTM_RATIO, scale*Mane.PTM_RATIO);
		setOrigin(0.0f, 0.0f);
		setScaleY(1.2f);
//		setOrigin(getWidth()/2, getHeight()/2);
//		super.drawingOffsetX = -Mane.PTM_RATIO * scale / 2;
	}
	
	public float getPhysicsWidth() {
		return getWidth() / (Mane.PTM_RATIO);
	}
	public float getPhysicsHeight() {
		return getHeight() / (Mane.PTM_RATIO);
	}
	
	

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float side = Mane.PTM_RATIO/4;
		batch.draw(Assets.pole, -side/2, 0.0f, side/2, 0.0f, side, physicsBody.getPosition().len()*Mane.PTM_RATIO, 1.0f, 1.0f, getRotation());
		
		super.draw(batch, parentAlpha, Assets.island);
	}
}
