package com.kevlanche.beaversmustdie;

import com.bitfire.postprocessing.filters.Filter;
import com.bitfire.utils.ShaderLoader;

public class FirstRippleFilter extends Filter<FirstRippleFilter> {

	public enum Param implements Parameter {
		// @formatter:off
		Time( "time", 0 ),
		Resolution( "resolution", 0 ),
		Texture( "tex", 0 ),
		MousePositionX("mousePosX", 0),
		MousePositionY("mousePosY", 0);
		// @formatter:on

		private String mnemonic;
		private int elementSize;

		private Param( String mnemonic, int elementSize ) {
			this.mnemonic = mnemonic;
			this.elementSize = elementSize;
		}

		@Override
		public String mnemonic() {
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize() {
			return this.elementSize;
		}
	}
	
	private float time;
	private float[] mousePos;
	
	public FirstRippleFilter() {
		super(ShaderLoader.fromFile( "screenspace", "ripple" ));
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public void setMousePos(float[] mousePos) {
		this.mousePos = mousePos;
		
	}

	@Override
	public void rebind() {
		setParams( Param.Texture, u_texture0 );
		setParams( Param.Time, time);
		mousePos[0] = -1.0f + 2*mousePos[0];
		mousePos[1] = 1.0f - 2*mousePos[1];
		setParam(Param.MousePositionX, mousePos[0]);
		setParam(Param.MousePositionY, mousePos[1]);
		endParams();
	}

	@Override
	protected void onBeforeRender() {
		inputTexture.bind( u_texture0 );
		rebind();
	}

}
