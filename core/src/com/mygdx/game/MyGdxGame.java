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
import java.util.Date;

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
	int zoom;
	Camera camera;
	MyInput input;
	Person player = new Person();

	Pos mapPos = new Pos( 0, 0 );
	Pos drawPos = new Pos( 0, 0 );

	long nowTime, lastTime;
	double timePassed;

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
		for (int x = 0; x < world.length; x++ ) {
			for (int y = 0; y < world[x].length; y++ ) {
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
		world = new int[ 16 * 10 ][ 16 * 10 ][2];
		mapgen = new MapGenerator[2];
		System.out.println("Test");
		for ( int i = 0; i < mapgen.length; i++ ) {
			mapgen[i] = new MapGenerator();
			mapgen[i].setSeed( seed + 512 );
			mapgen[i].setHeightAmp(heightAmp);
		}
		resize(screenW, screenH);
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		camera = new Camera( screenW, screenH );
		reloadMap();
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void render () {
		nowTime = System.currentTimeMillis();
		timePassed = (double)( lastTime - nowTime ) / 1000;
		lastTime = nowTime;
		System.out.println("Time passed: " + timePassed);

		// If possible, make render just call player.render(), camera.render(), person.render() and so on.
		// Just call render functions in different classes in the right order of course. Maybe add a "loop" function in all classes too or something


		//System.out.println("Got to frame " + frameCount);

		// Put events, movement and such into a function, to make it more clear what render is doing
		
		// Events
		if (mouse.x != Gdx.input.getX()) {
			mouse.x = Gdx.input.getX();
		}
		if (mouse.y != screenH - Gdx.input.getY()) {
			mouse.y = screenH - Gdx.input.getY();
		}


		if (input.deltaY != 0) {
			//System.out.println("MOUSE WAS SCROLLED");
			zoom -= input.deltaY*5;
			if (zoom < 15) {zoom = 15;}
			else if (zoom > 300) {zoom = 300;}
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
			player.pos.x += player.speed * timePassed;
		}
		if (dirRight) {
			player.pos.x -= player.speed * timePassed;
		}
		if (dirUp) {
			player.pos.y -= player.speed * timePassed;
		}
		if (dirDown) {
			player.pos.y += player.speed * timePassed;
		}



		if ( player.lastPos.x != player.pos.x || player.lastPos.y != player.pos.y ) { // Make a "player.isMoving() function"
			camera.setPos( 
				( int )( ( player.pos.x * camera.blockSize ) - ( screenW / 2 ) ) -  camera.blockSize,
			 	( int )( ( player.pos.y * camera.blockSize ) - ( screenH / 2 ) ) -  camera.blockSize
			);
		}

		player.calc(); // Calc what?
		camera.update();

		// Rendering
		//System.out.println("Camera size is: " + ( camera.endPos.x - camera.pos.x ) + ", " + ( camera.endPos.y - camera.pos.y ));
		//System.out.println("Camera is at: " + camera.pos.x + ", " + camera.pos.y );
		Gdx.gl.glClearColor( ( float ) 0.2, ( float ) 0.5, ( float ) 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int subtractDrawX = camera.pos.x - ( ( ( int )( camera.pos.x / camera.blockSize ) * camera.blockSize ) - camera.pos.x ); // I have no idea what this is
		int subtractDrawY = camera.pos.y - ( ( ( int )( camera.pos.y / camera.blockSize ) * camera.blockSize ) - camera.pos.y );

		mapMouse.x = (int)( ( camera.pos.x + mouse.x ) / camera.blockSize);
		mapMouse.y = (int)( ( camera.pos.y + mouse.y ) / camera.blockSize);

		batch.begin();
		
		for (int x = ( int ) camera.pos.x; x < camera.endPos.x; x += camera.blockSize ) {
			for (int y = ( int ) camera.pos.y; y < camera.endPos.y; y += camera.blockSize ) { // Loops through all blocks in sight by screenposition
				mapPos.x = ( int )( x / camera.blockSize ); // Calculates block position in grid (wutthafuck?)
				mapPos.y = ( int )( y / camera.blockSize );
				if ( mapPos.y > 0 && mapPos.x > 0 && mapPos.x < world.length ) { // Why not check for mappos here too?
					if ( mapPos.y < world[mapPos.x].length ) {
						drawPos.x = x - subtractDrawX;
						drawPos.y = y - subtractDrawY;
						
						//drawPos.x = x - camera.pos.x;
						//drawPos.y = y - camera.pos.y;
						//System.out.println("Rendering block at: " + ( x - camera.pos.x ) + ", " + ( y - camera.pos.y ));
						//System.out.println("Drawing tile at: " + drawPos.x + ", " + drawPos.y);
						

						// This switch could be a function like "DrawTile(int type, int x, int y)"
						switch( world[ mapPos.x ][ mapPos.y ][ 0 ] ) { // Checks which texture to draw without using a enum apparently :P
							case 1:
								draw( dirt, drawPos.x, drawPos.y );
								break;
							case 2:
								draw( grass, drawPos.x, drawPos.y );
								break;
							case 3:
								draw( stone, drawPos.x, drawPos.y );
								break;
						}
						if (mapPos.x == mapMouse.x && mapPos.y == mapMouse.y) { // And if the mouse is here, draw a "selected" box thingy around
							draw( selection, drawPos.x, drawPos.y );
						}
					}
				}
			}
		}
		batch.end();

	}
	private void draw( Texture texture, int x, int y ) {
		batch.draw( texture, x, y, camera.blockSize, camera.blockSize );
	}
}
