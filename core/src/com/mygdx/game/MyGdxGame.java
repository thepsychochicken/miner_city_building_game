package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import java.util.Random;
import java.lang.*;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	int frameCount = 0;
	Texture stone, dirt, iron_ore, grass, selection;
	int blockSize;
	Pos mouse, mapMouse;
	boolean dirLeft,dirRight,dirDown,dirUp,mouseLeft, mouseRight;
	int screenW, screenH;
	MapGenerator[] mapgen;
	int[] mapgenLayers = {1,3};
	int seed = 3213213;
	int heightAmp = 30;
	boolean leftArrowLast, rightArrowLast, downArrowLast, upArrowLast;
	int[][][] world; // x = x pos, y = y pos, z1 = block type, z2 = link to object
	SlidePos box;
	int zoom;
	Camera camera;
	MyInput input;
	Person player = new Person();

	private int getBlockAt( int x, int y ) {
		int blockType = 0;
		int minInt = 1000;
		for (int j = 0; j < mapgen.length; j++) {
			if (y <= mapgen[j].generateHeight(x) && y <= minInt) {
				blockType = mapgenLayers[j];
				minInt = mapgen[j].generateHeight(x)-3;
			}
			else {
				break;
			}
		}
		return blockType;
	}

	private void reloadMap() {
		int blockType;
		for (int x = 0; x < screenW/blockSize; x++ ) {
			for (int y = 0; y < screenH/blockSize; y++ ) {
				blockType = getBlockAt( x, y );
				if (blockType == 1 && getBlockAt( x, y + 1 ) == 0)  {
					blockType = 2;
				}
				world[x][y][0] = blockType;
				world[x][y][1] = 0;
			}
		}
	}
	@Override
	public void create () {
		input = new MyInput();
		Gdx.input.setInputProcessor(input);
		mouse = new Pos( 0, 0 );
		mapMouse = new Pos( 0, 0 );
		blockSize = 50;
		mouseLeft = false;
		mouseRight = false;
		dirLeft = false;
		dirRight = false;
		dirDown = false;
		dirUp = false;
		screenW = 1600;
		screenH = 900;
		leftArrowLast = false;
		rightArrowLast = false;
		upArrowLast = false;
		downArrowLast = false;
		batch = new SpriteBatch();
		stone = new Texture("stone.png");
		dirt = new Texture("dirt.png");
		iron_ore = new Texture("iron-ore.png");
		grass = new Texture("grass.png");
		zoom = 80;
		selection = new Texture("selection.png");
		world = new int[ 16 * 5 ][ 16 * 5 ][2];
		mapgen = new MapGenerator[2];
		System.out.println("Test");
		for ( int i = 0; i < mapgen.length; i++ ) {
			mapgen[i] = new MapGenerator();
			mapgen[i].setSeed( seed + 512 );
			mapgen[i].setHeightAmp(heightAmp);
		}
		box = new SlidePos(0,0,300);
		resize(screenW, screenH);
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		camera = new Camera( screenW, screenH );
	}

	@Override
	public void render () {
		frameCount++;
		//System.out.println("Got to frame " + frameCount);
		// Events
		if (mouse.x != Gdx.input.getX()) {
			mouse.x = Gdx.input.getX();
			mapMouse.x = (int)(mouse.x / blockSize);
			box.setPos(mouse.x,mouse.y);
		}
		if (mouse.y != screenH - Gdx.input.getY()) {
			mouse.y = screenH - Gdx.input.getY();
			mapMouse.y = (int)(mouse.y / blockSize);
			box.setPos(mouse.x,mouse.y);
		}

		if (input.deltaY != 0) {
			//System.out.println("MOUSE WAS SCROLLED");
			zoom += input.deltaY*5;
			camera.setZoom(zoom);
			input.deltaY = 0;
		}

		// Map generator settings
		if ( Gdx.input.isKeyPressed(Input.Keys.LEFT) && !leftArrowLast ) {
			System.out.println("Changed seed to more");
			seed--;
			for (int i = 0; i < mapgen.length; i++) {
				mapgen[i].setSeed(seed+i);
			}
			reloadMap();
			leftArrowLast = true;
		}
		else if ( !Gdx.input.isKeyPressed(Input.Keys.LEFT) ){
			leftArrowLast = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !rightArrowLast ) {
			System.out.println("Changed seed to less");
			seed++;
			for (int i = 0; i < mapgen.length; i++) {
				mapgen[i].setSeed(seed+i);
			}
			reloadMap();
			rightArrowLast = true;
		}
		else if ( !Gdx.input.isKeyPressed(Input.Keys.RIGHT) ){
			rightArrowLast = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.UP)/* && !upArrowLast */) {
			System.out.println("Changed height amplifier to more");
			heightAmp++;
			for (int i = 0; i < mapgen.length; i++) {
				mapgen[i].setHeightAmp(heightAmp);
			}
			reloadMap();
			upArrowLast = true;
		}
		else if ( !Gdx.input.isKeyPressed(Input.Keys.UP) ){
			upArrowLast = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.DOWN)/* && !downArrowLast */) {
			System.out.println("Changed height amplifier to less");
			heightAmp--;
			for (int i = 0; i < mapgen.length; i++) {
				mapgen[i].setHeightAmp(heightAmp);
			}
			reloadMap();
			downArrowLast = true;
		}
		else if ( !Gdx.input.isKeyPressed(Input.Keys.DOWN) ){
			downArrowLast = false;
		}

		// Movement settings
		if ( Gdx.input.isKeyPressed(Input.Keys.A) ) {
			//System.out.print("Left was pressed\n");
			dirLeft = true;
		}
		else {
			dirLeft = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.D) ) {
			//System.out.println("Right was pressed");
			dirRight = true;
		}
		else {
			dirRight = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.W) ) {
			//System.out.println("Up was pressed");
			dirUp = true;
		}
		else {
			dirUp = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.S) ) {
			//System.out.println("Down was pressed");
			dirDown = true;
		}
		else {
			dirDown = false;
		}

		if ( Gdx.input.isButtonPressed(Input.Buttons.LEFT) ) {
			// Left mouse button
			mouseLeft = true;
		}
		else {
			mouseLeft = false;
		}

		if ( Gdx.input.isButtonPressed(Input.Buttons.RIGHT) ) {
			// Right mouse button
			mouseRight = true;
		}
		else {
			mouseRight = false;
		}

		// Movement

		if (dirLeft) {
			player.pos.x -= 1;
		}
		if (dirRight) {
			player.pos.x += 1;
		}
		if (dirUp) {
			player.pos.y += 1;
		}
		if (dirDown) {
			player.pos.y -= 1;
		}

		box.calc();
		if ( player.lastPos.x != player.pos.x || player.lastPos.y != player.pos.y ) {
			camera.setPos( 
				( int )( player.pos.x - (screenW/2/camera.blockSize) ),
			 	( int )( player.pos.y - (screenH/2/camera.blockSize) ) 
			);
		}
		camera.update();

		// Rendering

		Gdx.gl.glClearColor( ( float ) 0.2, ( float ) 0.5, ( float ) 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (int x = ( int ) camera.pos.x; x < camera.endPos.x; x+= camera.blockSize ) {
			for (int y = ( int ) camera.pos.y; y < camera.endPos.y; y++ ) {
				if (y > 0 && x > 0) {
					switch(world[x][y][0]) {
						case 1:
							draw( dirt, x, y );
							break;
						case 2:
							draw( grass, x, y );
							break;
						case 3:
							draw( stone, x, y );
							break;
					}
					if (x == mapMouse.x && y == mapMouse.y) {
						draw( selection, x, y );
					}
				}
			}
		}
		batch.draw( stone, box.x-10, box.y-10, 20, 20 );
		batch.end();

	}
	private void draw( Texture texture, int x, int y ) {
		batch.draw( texture, x*camera.blockSize, y*camera.blockSize, camera.blockSize, camera.blockSize );
	}
}
