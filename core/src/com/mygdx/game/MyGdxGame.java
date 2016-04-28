/*

Naming convention:
Variables, functions and objects are camelCase
Class-definitions are PascalCase

*/

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
	Texture stone, dirt, iron_ore, grass, selection, personT;
	int blockSize;
	Pos mouse, mapMouse;
	boolean mouseLeft, mouseRight;
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
				if (blockType == TILE.DIRT && getBlockAt( x, y + 1 ) == TILE.AIR)  {
					blockType = TILE.GRASS;
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
		selection = new Texture("selection.png");
		personT = new Texture("person-placeholder.png");
		
		zoom = 80;
		world = new int[ 16 * 10 ][ 16 * 10 ][2];
		mapgen = new MapGenerator[2];

		System.out.println("Test");
		
		for ( int i = 0; i < mapgen.length; i++ ) {
			mapgen[i] = new MapGenerator();
			mapgen[i].setSeed( seed + 512 );
			mapgen[i].setHeightAmp(heightAmp);
		}
		
		//resize(screenW, screenH); // LibGDX will call this by itself after create()
		
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		camera = new Camera( screenW, screenH );
		reloadMap();
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void render () {
		nowTime = System.currentTimeMillis();
		timePassed = (double)( nowTime - lastTime ) / 1000;
		lastTime = nowTime;
		//System.out.println("Time passed: " + timePassed);

		// If possible, make render just call player.render(), camera.render(), person.render() and so on.
		// Just call render functions in different classes in the right order of course. Maybe add a "loop" function in all classes too or something
		// Put events, movement and such into a function, to make it more clear what render is doing
		
		HandleInput(input);
		player.calc(timePassed); // Movement and such, pretty much all actions
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
		
		System.out.println("Plater pos: " + player.pos.x + ", " + player.pos.y);

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
						drawTile(drawPos, world[ mapPos.x ][ mapPos.y ][ 0 ]);

						if (mapPos.x == mapMouse.x && mapPos.y == mapMouse.y) { // And if the mouse is here, draw a "selected" box thingy around
							draw( selection, drawPos.x, drawPos.y );
						}
					}
				}
			}
		}
		batch.draw( personT, ( int )( player.pos.x * camera.blockSize ) - camera.pos.x, ( int )( player.pos.y * camera.blockSize ) - camera.pos.y, ( int )( camera.blockSize * 2.5f ), ( int )( camera.blockSize * 5f ) );
		batch.end();

	}

	private void drawTile(Pos drawPos, int type) {
		switch( type ) {
			case TILE.DIRT: // 1
				draw( dirt, drawPos.x, drawPos.y );
				break;
			case TILE.GRASS: // 2
				draw( grass, drawPos.x, drawPos.y );
				break;
			case TILE.STONE: // 3
				draw( stone, drawPos.x, drawPos.y );
				break;
		}
	}

	private void draw( Texture texture, int x, int y ) {
		batch.draw( texture, x, y, camera.blockSize, camera.blockSize );
	}

	private void HandleInput(MyInput input) {
		// Adjust everything in here to take input from MyInput
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
			player.dirLeft = true;
		}
		else {
			player.dirLeft = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.D) ) {
			//System.out.println("Right was pressed");
			player.dirRight = true;
		}
		else {
			player.dirRight = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.W) ) {
			//System.out.println("Up was pressed");
			player.dirUp = true;
		}
		else {
			player.dirUp = false;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.S) ) {
			//System.out.println("Down was pressed");
			player.dirDown = true;
		}
		else {
			player.dirDown = false;
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

		if ( player.lastPos.x != player.pos.x || player.lastPos.y != player.pos.y ) { // Make a "player.isMoving() function"
			camera.setPos( 
				( int )( ( player.pos.x * camera.blockSize ) - ( screenW / 2 ) ) -  camera.blockSize,
			 	( int )( ( player.pos.y * camera.blockSize ) - ( screenH / 2 ) ) -  camera.blockSize
			);
		}

	}
}
