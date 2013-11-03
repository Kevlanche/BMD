package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Water extends Actor implements Disposable {
	
	public static float WATER_RADIUS = 25.0f;
	public static final float EARTH_RADIUS = 5.0f;
	
	
	float time;
	ShaderProgram shader;
	Mesh mesh;
	
	public Water(boolean trans) {
		
		String alpha = trans ? "0.2" : "1.0";
		
		shader = new ShaderProgram("#define COLOR_INNER vec4(0.0, 0.0, 1.0, "+alpha+")\n#define COLOR_OUTER vec4(0.0,1.0,1.0, "+alpha+")\n" +getFile("vert.vs"), getFile("frag.fs"));
		
		if (!shader.isCompiled()) {
			System.err.println("SHADER NOT COMPILED");
			System.out.println(shader.getLog());
		}
		
		int i;
		final int triangles = 500;
	 

		
		float[] tris = new float[3*(triangles + 2)];
		
		tris[0] = 0.0f;
		tris[1] = 0.0f;
		tris[2] = 0.0f;
		
		for(i = 0; i <= triangles; i++) { 

			tris[3*(i+1) + 0] = 0.5f * MathUtils.cos(i *  MathUtils.PI2 / triangles);
			tris[3*(i+1) + 1] = 0.5f * MathUtils.sin(i *  MathUtils.PI2 / triangles);
			tris[3*(i+1) + 2] = 0.0f;
			
		}
		mesh = new Mesh(true, tris.length, 0, VertexAttribute.Position());
		mesh.setVertices(tris);

	}
	
	public void act(float dt) {
		super.act(dt);
		this.time += dt;
//		WATER_RADIUS = 30.0f + MathUtils.cos(time);
	}
	
	private String getFile(String file) {
		return Gdx.files.internal("data/"+file).readString();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
//
//		batch.end();
//		batch.begin();
		
		shader.begin();
		
		shader.setUniformMatrix("u_worldView", batch.getProjectionMatrix());
		shader.setUniformf("time", time);
		shader.setUniformf("size", 166.0f*WATER_RADIUS);
		
		mesh.render(shader, GL20.GL_TRIANGLE_FAN);
		
		shader.end();
		
		batch.end();
		batch.begin();
	}

	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
	}
}

