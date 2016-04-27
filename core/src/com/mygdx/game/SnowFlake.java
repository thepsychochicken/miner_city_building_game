package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;
import java.lang.*;

public class SnowFlake {
	int startX;
	int startY;
	int lifeTime;
	int screenH,screenW;
	boolean active;
	int scale;
	public SnowFlake(int w, int h) {
		screenH = h;
		screenW = w;
		Random rand = new Random();
		startX = rand.nextInt(screenW-10) + 5;
		startY = rand.nextInt(screenH) + 50;
		scale = rand.nextInt(5)+5;
		lifeTime = 0;
		active = true;
	}

	public void render(SpriteBatch batch, Texture snowFlake) {
		if (active) {
			int y = startY-lifeTime;
			lifeTime++;
			int x = (int)( startX + ( Math.cos( lifeTime * 0.02 ) * 10 ) );
			batch.draw( snowFlake, x, y, scale, scale );
			if (y < -50) {
				Random rand = new Random();
				startX = rand.nextInt(screenW-10) + 5;
				startY = rand.nextInt(200) + screenH;
				scale = rand.nextInt(5)+5;
				lifeTime = 0;
				//active = false;
			}
		}
	}
}