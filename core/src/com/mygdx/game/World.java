package com.mygdx.game;

public class World {

	private String worldName;

	private int seed, heightAmp;


	private int[] mapgenLayers = {1,3};

	private int[][][] world; // x = x pos, y = y pos, z1 = block type, z2 = link to object
	public MapGenerator[] mapgen = new MapGenerator[2];

	public World(String worldName, int seed) {
		this.seed = seed;
		this.worldName = worldName;
		world = new int[ 64 * 5 ][ 64 * 5 ][2];

		for ( int i = 0; i < mapgen.length; i++ ) {
			mapgen[i] = new MapGenerator();
			mapgen[i].setSeed( seed + 512 );
			mapgen[i].setHeightAmp(heightAmp);
		}
	}

	public void setSeed(int seed) {
		for (int i = 0; i < this.mapgen.length; i++) {
			mapgen[i].setSeed(seed+i);
		}
	}

	public void setHeightAmp(int heightAmp) {
		for (int i = 0; i < this.mapgen.length; i++) {
			mapgen[i].setHeightAmp(heightAmp);
		}
	}

	public int getTypeAt( int x, int y ) {
		if ( y > 0 && x > 0 && x < this.world.length ) {
			if ( y < this.world[x].length ) {
				return this.world[x][y][0];	
			}
		}
		return -1;
	}

	private int getBlockAt( int x, int y, int[][] mapgenHeights ) {
		int blockType = 0;
		int minInt = 1000;
		for (int j = 0; j < mapgen.length; j++) {
			if (y <= mapgenHeights[j][x] && y <= minInt) {
				blockType = mapgenLayers[j];
				minInt = mapgenHeights[j][x]-3;
			}
			else {
				break;
			}
		}
		return blockType;
	}

	public void reloadMap() {
		long start = System.currentTimeMillis();
		int blockType;
		int[][] mapgenHeights = new int[ mapgen.length ][ world.length ];
		for (int i = 0; i < mapgen.length; i++) {
			for (int j = 0; j < world.length; j++) {
				mapgenHeights[i][j] = mapgen[i].generateHeight(j);
			}
		}
		for (int x = 0; x < world.length; x++ ) {
			for (int y = 0; y < world[x].length; y++ ) {
				blockType = getBlockAt( x, y, mapgenHeights );
				if (blockType == TILE.DIRT && getBlockAt( x, y + 1, mapgenHeights ) == TILE.AIR)  {
					blockType = TILE.GRASS;
				}
				blockType = mapgen[ 0 ].generateOre( blockType, x, y );
				world[x][y][0] = blockType;
				world[x][y][1] = 0;
			}
		}
		long finish = System.currentTimeMillis();
		int totalBlocks = world.length * world[0].length;
		System.out.println("Took " + (finish - start) + " ms to recalculate a map of ( width: " +
		 world.length + ", height: " + world[0].length + " ) " + "(" + 
		 ((double)( finish - start ) / totalBlocks) + " ms pr tile)" );
	}
}