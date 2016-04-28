package com.mygdx.game;

import java.util.Random;

public class MapGenerator {
	private int seed;
	private Random rand = new Random();
	private int heightAmp = 2;
	public MapGenerator() {
	}

	public void setSeed(int s) {
		seed = s;
	}

	public void setHeightAmp(int amp) {
		heightAmp = amp;
		if (heightAmp < 0) {
			heightAmp = 0;
		}
		else if (heightAmp > 500) {
			heightAmp = 500;
		}
	}

	public int generateOre(int input, int x , int y ) {
		int[] blocksAllowingOre = { TILE.STONE };
		boolean allowOre = false;
		for (int i = 0; i < blocksAllowingOre.length; i++) {
			if (input == blocksAllowingOre[i]) {
				allowOre = true;
				break;
			}
		}
		if (allowOre) {
			int clusterSizes = 8;
			float[][] percent = new float[3][3];
			// 0 for air, to make caves, input if not anything else
			int[] oreToGet = { input, TILE.IRON_ORE };
			int[] oreStartingLevel = { 0, 20 };
			int[] oreChance = { 300, 100 };
			rand.setSeed(this.seed);
			int oreSeed = rand.nextInt(100000);

			int combinedOreChance = 0;
			for ( int i = 0; i < oreChance.length; i++ ) {
				combinedOreChance += oreChance[ i ];
			}

			int spawnOre;

			int[] oreArray = new int[ oreToGet.length ];
			for ( int i = 0; i < oreArray.length; i++ ) {
				oreArray[ i ] = 0;
			}

			// List the ores in array (put ore in clusters)
			for ( int i = 0; i < oreToGet.length; i++ ) {
				for ( int j = clusterSizes; j > 0; j /= 2 ) {
					rand.setSeed( ( int )( x / j ) * 3241 + ( int )( y / j ) * 6578 + i * 21144 + oreSeed);
					spawnOre = rand.nextInt(combinedOreChance);
					if ( spawnOre <= oreChance[ i ] ) {
						oreArray[ i ]++;
					}
				}
			}

			// Find out what ore to use
			for ( int i = oreToGet.length-1; i >= 0 ; i-- ) {
				if ( oreArray[ i ] > 3 ) {
					input = oreToGet[ i ];
					break;
				}
			}
			
		}
		return input;
	}

	public int generateHeight(int x) {
		return smooth( x, 20);
	}

	private int smooth( int x, int amp ) {
		int multiplier = 10;
		int adder = getNoise(x) * 2 ;
		int divider = 2;
		for (int i = 1; i < amp; i++) {
			adder += (getNoise(x+i) + getNoise(x-i)) * ( ( amp * multiplier ) - ( i * ( int )( multiplier / 4 ) ) );
			divider += ( ( amp * multiplier ) * (2) ) - ( i * ( int )( multiplier / 4 ) );
		}
		return (int)( ( ( adder / divider ) ) ) + 30;


		// Work in progress 'Cosine interpolator'
		
		/*int curveSize = 16;
		int thisChunk = ( int )( ( float ) x / curveSize );
		//System.out.println(thisChunk);
		int thisChunkX = (int)( ( ( ( float )( ( float )getNoise( thisChunk ) / heightAmp ) + 1) / 2) * curveSize );
		int thisHeight = getNoise( x );
		if (x ==  thisChunkX + ( thisChunk * curveSize ) ) {
			return thisHeight + 40;
		}
		else if (x < thisChunkX + ( thisChunk * curveSize ) ) {
			int otherChunkX = (int)( ( ( ( float )( ( float )getNoise( thisChunk - 1 ) / heightAmp ) + 1) / 2) * curveSize );
			int dif = ( thisChunkX + ( thisChunk * curveSize ) ) - ( otherChunkX + ( thisChunk * curveSize ) );
			int otherHeight = getNoise( otherChunkX );
			float percent = ( (float)x - thisChunk * curveSize ) / dif;
			percent = 1f - ( ( float )( Math.cos( percent * Math.PI ) + 1 ) / 2 );
			//System.out.println(percent);
			dif = otherHeight - thisHeight;
			return otherHeight + ( int )( percent * dif ) + 40;
		}
		return 40;*/
	}

	private int getNoise(int x) {
		int y;
		rand.setSeed(x * 3442132 + seed);
		y = (int)((double)( ( ( (double)rand.nextInt(1000000) - 500000 ) / 500000 ) ) * heightAmp);
		if (x == 1) {
			//System.out.println( (double)( ( ( (double)rand.nextInt(1000000) - 500000 ) / 500000 ) + 1 ) );
		}
		return y;
	}
}