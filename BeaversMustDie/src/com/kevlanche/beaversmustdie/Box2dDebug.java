package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Box2dDebug extends Actor {

	private World world;
	private Box2DDebugRenderer bd;
	private OrthographicCamera camera;
	
	public Box2dDebug(World world) {
		this.world = world;
		bd = new Box2DDebugRenderer();
		
		camera = new OrthographicCamera(Mane.WIDTH/Mane.PTM_RATIO, Mane.HEIGHT/Mane.PTM_RATIO);
		camera.update();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		
//		camera.update();
//		bd.render(world, camera.projection);
		batch.end();
		Matrix4 m = batch.getProjectionMatrix().cpy();
		m.scl(Mane.PTM_RATIO);
		bd.render(world, m);
		batch.begin();
		
	}
	
	
}
