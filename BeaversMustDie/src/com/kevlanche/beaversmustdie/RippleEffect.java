package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;

public class RippleEffect extends PostProcessorEffect{

	
	private FirstRippleFilter ripple;
	
	private float time;
	
	public RippleEffect() {
		ripple = new FirstRippleFilter();
	}

	@Override
	public void dispose() {
		ripple.dispose();
		
	}

	@Override
	public void rebind() {
		ripple.rebind();
		
	}
	
	public void setTime(float time) {
		this.time = time;
	}

	@Override
	public void render(FrameBuffer src, FrameBuffer dest) {
		ripple.setTime(time);
		restoreViewport( dest );
		ripple.setInput( src ).setOutput( dest ).render();
		
	}

}
