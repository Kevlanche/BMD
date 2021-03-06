package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class LBL extends Actor {

	public static final int 	CHAR_W = 6,
								CHAR_H = 9;

	private static TextureRegion[] A_Z;
	private static TextureRegion[] NUM;
	private static TextureRegion period, comma, colon, semicolon, questionmark, exclamation, plus, quote, percent, hashtag, at, div, minus, mul;
	
	static{
		A_Z = new TextureRegion[1 + (int)'z' - (int)'a'];
		
		TextureRegion alphabet = Assets.alphabet;
		
		int limit = (int)((int)'r'-(int)'a');
		for (int i=0; i<A_Z.length; ++i) {
			
			if (i <= limit) A_Z[i] = new TextureRegion(alphabet, 1 + i*(CHAR_W+1), 1, CHAR_W, CHAR_H);
			else A_Z[i] = new TextureRegion(alphabet, 1 + (i-limit-1)*(CHAR_W+1), CHAR_H+2, CHAR_W, CHAR_H);
		}
		
		NUM = new TextureRegion[10];
		
		for (int i=0; i<NUM.length; ++i) {
			NUM[i] = new TextureRegion(alphabet, 1 + i*(CHAR_W+1), 2*CHAR_H+3, CHAR_W, CHAR_H);
		}
		
		
		period = 		new TextureRegion(alphabet, 1, 4*CHAR_H+5, CHAR_W, CHAR_H);
		comma = 		new TextureRegion(alphabet, CHAR_W+2, 4*CHAR_H+5, CHAR_W, CHAR_H);
		colon = 		new TextureRegion(alphabet, 2*CHAR_W+3, 4*CHAR_H+5, CHAR_W, CHAR_H);
		semicolon = 	new TextureRegion(alphabet, 3*CHAR_W+4, 4*CHAR_H+5, CHAR_W, CHAR_H);
		questionmark = 	new TextureRegion(alphabet, 4*CHAR_W+5, 4*CHAR_H+5, CHAR_W, CHAR_H);
		exclamation = 	new TextureRegion(alphabet, 5*CHAR_W+6, 4*CHAR_H+5, CHAR_W, CHAR_H);
		plus = 			new TextureRegion(alphabet, 6*CHAR_W+7, 4*CHAR_H+5, CHAR_W, CHAR_H);
		quote = 		new TextureRegion(alphabet, 7*CHAR_W+8, 4*CHAR_H+5, CHAR_W, CHAR_H);
		percent = 		new TextureRegion(alphabet, 8*CHAR_W+9, 4*CHAR_H+5, CHAR_W, CHAR_H);
		hashtag =		new TextureRegion(alphabet, 9*CHAR_W+10, 4*CHAR_H+5, CHAR_W, CHAR_H);
		at =			new TextureRegion(alphabet, 10*CHAR_W+11, 4*CHAR_H+5, CHAR_W, CHAR_H);
		div =			new TextureRegion(alphabet, 11*CHAR_W+12, 4*CHAR_H+5, CHAR_W, CHAR_H);
		minus =			new TextureRegion(alphabet, 12*CHAR_W+13, 4*CHAR_H+5, CHAR_W, CHAR_H);
		mul =			new TextureRegion(alphabet, 13*CHAR_W+14, 4*CHAR_H+5, CHAR_W, CHAR_H);
		
	}

	public String text;
	private float x, y, ancx, ancy;
	private TextureRegion[][] letters;
	private int numLines;
	
	public LBL(String s) {
		this(s, 3.0f);
	}
	public LBL(String s, float scale) {
		x = y = ancx = ancy = 0f;
		setScale(scale);
		setText(s);

		setTouchable(Touchable.disabled);
	}
	
	public LBL invalidate() {
		String[] lines = text.split("\n");
		numLines = lines.length;
		int maxl = lines[0].length();
		for (int i=1; i<lines.length; ++i) {
			if (lines[i].length() > maxl)
				maxl = lines[i].length();
		}
		
		letters = new TextureRegion[lines.length][maxl];
		
		for (int i=0; i<lines.length; ++i) {
			
			for (int k=0; k<lines[i].length(); ++k) {
				char c = lines[i].charAt(k);
				
				if (c >= 'a' && c <= 'z') {
					letters[i][k] = A_Z[(int)c-(int)'a'];
				} else if (c >= '0' && c <= '9') {
					letters[i][k] = NUM[(int)c-(int)'0'];
				}
				else if (c == '.') letters[i][k] = period;
				else if (c == ',') letters[i][k] = comma;
				else if (c == ':') letters[i][k] = colon;
				else if (c == ';') letters[i][k] = semicolon;
				else if (c == '?') letters[i][k] = questionmark;
				else if (c == '!') letters[i][k] = exclamation;
				else if (c == '+') letters[i][k] = plus;
				else if (c == '"') letters[i][k] = quote;
				else if (c == '%') letters[i][k] = percent;
				else if (c == '#') letters[i][k] = hashtag;
				else if (c == '@') letters[i][k] = at;
				else if (c == '/') letters[i][k] = div;
				else if (c == '-') letters[i][k] = minus;
				else if (c == '*') letters[i][k] = mul;
				
				else {
					letters[i][k] = null;
				}
			}
		}

		setWidth(getScaleX()*CHAR_W*maxl);
		setHeight(getScaleY()*CHAR_H*lines.length);

		setOriginX(getWidth()/2);
		setOriginY(getHeight()/2);
		
		position( x, y, ancx, ancy );
		
		return this;
	}
	
	public int getNumberOfLines() {
		return numLines;
	}
	
	public LBL rescale(float maxw, float maxh, boolean keepAspectRatio) {
		float sx = maxw / (getWidth());
		float sy = maxh / (getHeight());
		
		if (keepAspectRatio) setScale( Math.min(sx, sy) );
		else setScale( sx, sy );
		
		return this;
	}
	public LBL rescaleWithMaxWidth(float maxw) {
			
			float sx = maxw / (getWidth());
			setScale(sx, sx);
			
			return this;
		}
	public LBL rescaleWithMaxHeight(float maxh) {
		
		float sy = maxh / (getHeight());
		setScale(sy, sy);
		
		return this;
	}

	public void setText(String text) {
		this.text = text.toLowerCase();
		invalidate();
	}
	public LBL position(float x, float y, float ancx, float ancy) {
		this.x = x;
		this.y = y;
		this.ancx = ancx;
		this.ancy = ancy;
		
		setPosition( x - getWidth()*ancx , y - getHeight()*ancy );
		
		return this;
	}


	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (letters == null) return;
		
		Color c = batch.getColor();
		Color tc = getColor();
		batch.setColor(tc.r, tc.g, tc.b, tc.a * parentAlpha);
		
		for (int i=0; i<letters.length; ++i) {
			for (int j=0; j<letters[0].length; ++j) {
				if (letters[i][j] == null) continue;
				
				batch.draw(letters[i][j], getX()+j*CHAR_W*getScaleX(), getY()+getHeight()-getScaleY()*(i+1)*CHAR_H, 0f, 0f, CHAR_W, CHAR_H, getScaleX(), getScaleY(), getRotation());
			}
		}
//		batch.draw(tr, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(tc);
	}
}
