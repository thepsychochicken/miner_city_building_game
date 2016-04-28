package com.mygdx.game;

public class Person {
	public double speed;
	public FloatPos pos;
	public FloatPos lastPos;
	public boolean dirUp, dirDown, dirLeft, dirRight;
	public Person() {
		pos = new FloatPos( (double) 64, (double) 20 );
		lastPos = new FloatPos( (double) 0, (double) 0 );
		speed = 5;
		dirLeft = false;
		dirRight = false;
		dirDown = false;
		dirUp = false;
	}

	public void calc( double timePassed ) {
		// First

		lastPos.x = pos.x;
		lastPos.y = pos.y;

		// Middle

		if (this.dirUp) { 
			this.pos.y += this.speed * timePassed; 
			System.out.println("Player goes up");
		}
		if (this.dirDown) {
			this.pos.y -= this.speed * timePassed;
		}
		if (this.dirLeft) {
			this.pos.x -= this.speed * timePassed; 
		}
		if (this.dirRight) {
			this.pos.x += this.speed * timePassed;
		}

		// Last

	}
}