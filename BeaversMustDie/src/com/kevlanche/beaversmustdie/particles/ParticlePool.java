package com.kevlanche.beaversmustdie.particles;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class ParticlePool {
	
	public static ParticleEffect get() {
		Pool<ParticleEffect> pool = Pools.get(ParticleEffect.class);
		ParticleEffect pe = pool.obtain();
		pe.pool = pool;
		return pe;
	}

}
