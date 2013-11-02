package com.kevlanche.beaversmustdie;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BeaversMustDie";
		cfg.useGL20 = true;
		cfg.width = (int)Mane.WIDTH;
		cfg.height = (int)Mane.HEIGHT;
		
		new LwjglApplication(new Mane(), cfg);
	}
}
