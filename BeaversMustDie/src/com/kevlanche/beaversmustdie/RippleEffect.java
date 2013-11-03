package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;

public class RippleEffect extends PostProcessorEffect{

	
	private FirstRippleFilter ripple;
	
	private float time;
	private float[] mousePos;
	
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

	public void setMousePos(float[] mousePos) {
		this.mousePos = mousePos;
	}
	
	@Override
	public void render(FrameBuffer src, FrameBuffer dest) {
		ripple.setTime(time);
		ripple.setMousePos(mousePos);
		restoreViewport( dest );
		ripple.setInput( src ).setOutput( dest ).render();
		
	}

}
