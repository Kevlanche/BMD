package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Beaver extends PhysicsActor {
	
	private Island island;
	private float ang;

	public Beaver(World world, Island island) {
		this.island = island;
		float scale = 2.0f;
		float radius = 1.0f;
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.DynamicBody;

		Vector2 pos = island.physicsBody.getPosition();
		ang = island.physicsBody.getAngle();
		

		float islandW = island.getPhysicsWidth();
		float islandH = island.getPhysicsHeight();
		
		Vector2 off = new Vector2(islandW * MathUtils.random(0.3f, 0.6f), islandH);
		off.rotate(MathUtils.radiansToDegrees * ang);
		
		bd.position.set(new Vector2(
									pos.x + off.x, 
									pos.y + off.y ));
		bd.angle = ang;

		off.set(islandW*0.075f, islandH);
		off.rotate(MathUtils.radiansToDegrees * ang);
		Vector2 rightEdge = new Vector2(pos.x + off.x, pos.y + off.y).scl(Mane.PTM_RATIO);
		off.set(islandW*0.85f, islandH);
		off.rotate(MathUtils.radiansToDegrees * ang);
		Vector2 leftEdge = new Vector2(pos.x + off.x, pos.y + off.y).scl(Mane.PTM_RATIO);
		
		
		float speed = MathUtils.random(1.0f, 3.0f);
		addAction( Actions.delay(MathUtils.random(0.0f, 1.0f),
									Actions.forever( Actions.sequence(
											Actions.moveTo(rightEdge.x, rightEdge.y, speed), 
											Actions.moveTo(leftEdge.x, leftEdge.y, speed) ) )));
		
		bd.linearDamping = 0.2f;

		Body body = world.createBody(bd);

		FixtureDef fd = new FixtureDef();

		fd.density = 1.0f;
		fd.filter.categoryBits = Collision.BEAVER;
		fd.filter.maskBits = Collision.SHARK;

		fd.restitution = 0.0f;
		fd.friction = 0.0f;
		fd.isSensor = true;

		CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		cs.setPosition(new Vector2(radius, radius));


		fd.shape = cs;

		body.createFixture(fd);
		cs.dispose();
		super.initPhysicsBody(body);

		setSize(scale*Mane.PTM_RATIO, scale*Mane.PTM_RATIO);
		setOrigin(0.0f, 0.0f);
	}

	
	@Override
	public void act(float delta) {
		super.act(delta);
	
//		Vector2 move = new Vector2(0,0);
//		
//		if(physicsBody.getPosition().x < (island.getX() - island.getWidth()/2)) {
//			
//			move.x = delta * 15;
//			move = move.rotate(MathUtils.radiansToDegrees * ang);
//			
//			this.addAction(Actions.moveBy(move.x, move.y));
//			
//			
//		} else if(physicsBody.getPosition().x > (island.getX() + island.getWidth()/2)) {
//			move.x = delta * -15;
//			move = move.rotate(MathUtils.degreesToRadians * ang);
//			this.addAction(Actions.moveBy(move.x, move.y));
//		}
		
		
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha, Assets.beaver);
	}


}
