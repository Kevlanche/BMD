package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class CollisionManager implements ContactListener  {
	
	private GameScreen gs;
	
	public CollisionManager(World world, GameScreen gs) {
		world.setContactListener(this);
		this.gs = gs;
	}

	@Override
	public void beginContact(Contact contact) {
		short ca = contact.getFixtureA().getFilterData().categoryBits;
		short cb = contact.getFixtureB().getFilterData().categoryBits;
		
		if ( (ca == Collision.SHARK && cb == Collision.SILO) ||
			 (cb == Collision.SHARK && ca == Collision.SILO) ) {
			
			gs.onSiloBoom( (Silo) (ca == Collision.SILO ? contact.getFixtureA().getUserData() : contact.getFixtureB().getUserData()) );
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}