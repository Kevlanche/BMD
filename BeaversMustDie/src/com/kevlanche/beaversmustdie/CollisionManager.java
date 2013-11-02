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
		else if ( (ca == Collision.SHARK && cb == Collision.POOL) ||
				 (cb == Collision.SHARK && ca == Collision.POOL) ) {
			
				gs.onPoolBoom( (Pool) (ca == Collision.POOL ? contact.getFixtureA().getUserData() : contact.getFixtureB().getUserData()) );
		}
		
		else if( (ca == Collision.SHARK && cb == Collision.UPGRADE) ||
				 (cb == Collision.SHARK && ca == Collision.UPGRADE)) {
			gs.upgrade((Upgrade) (ca == Collision.UPGRADE ? contact.getFixtureA().getUserData() : contact.getFixtureB().getUserData()) );
		} else if ( (ca == Collision.SHARK && cb == Collision.ISLAND) ||
					(cb == Collision.SHARK && ca == Collision.ISLAND)) {
			
			gs.shark.canJump = true;
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
