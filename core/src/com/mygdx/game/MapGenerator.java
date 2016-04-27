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

	public int generateOre(int input, int x , int y) {
		Random rand = new Random();
		int[] blocksAllowingOre = {2};
		boolean allowOre = false;
		for (int i = 0; i < blocksAllowingOre.length; i++) {
			if (input == blocksAllowingOre[i]) {
				allowOre = true;
				break;
			}
		}
		if (allowOre) {
			float[][] percent = new float[3][3];
			// 0 for air, to make caves
			int[] oreToGet = { 0, 3 };
			int[] oreStartingLevel = { 30, 20 };
			int[] oreChance = { 300, 50 };
			int uX = 0;
			int uY = 0;
			for (int arrX = x-1; arrX <= x+1; arrX++ ) {
				uY = 0;
				for (int arrY = y-1; arrY <= y+1; arrY++ ) {
					percent[uX][uY] = (float)rand.nextInt(100000)/100000;
					uY++;
				}
				uX++;
			}
			rand.setSeed(x * 321321 + y *557444 + seed);
			
		}
		return input;
	}

	public int generateHeight(int x) {
		return smooth(x, 200);
	}

	private int smooth( int x, int amp ) {
		int adder = getNoise(x) * amp;
		int divider = amp;
		for (int i = 1; i < amp; i++) {
			adder += (getNoise(x+i) + getNoise(x-i)) * ( amp / i );
			divider += ( amp * 2 ) / i;
		}
		return (int)( ( ( adder / divider ) ) ) + 30;
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