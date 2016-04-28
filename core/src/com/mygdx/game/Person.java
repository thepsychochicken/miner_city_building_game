package com.mygdx.game;

public class Person {
	public double speed;
	public FloatPos pos;
	private FloatPos vel;
	public FloatPos lastPos;
	public boolean dirUp, dirDown, dirLeft, dirRight;
	public boolean inAir;

	public Person() {
		pos = new FloatPos( (double) 64, (double) 20 );
		vel = new FloatPos( (double) 64, (double) 20 );
		lastPos = new FloatPos( (double) 0, (double) 0 );
		speed = 5;
		dirLeft = false;
		dirRight = false;
		dirDown = false;
		dirUp = false;
		inAir = false;
	}

	public void calc( double timePassed ) {
		// First

		lastPos.x = pos.x;
		lastPos.y = pos.y;

		// Middle

		if (this.dirUp) { 
			this.vel.y += this.speed * timePassed;
		}
		if (this.dirDown) {
			this.vel.y -= this.speed * timePassed;
		}
		if (this.dirLeft) {
			this.vel.x -= this.speed * timePassed; 
		}
		if (this.dirRight) {
			this.vel.x += this.speed * timePassed;
		}

		if ( !this.dirRight && !this.dirLeft && !this.dirDown && !this.dirUp ) {
			this.vel.x *= 0.9999d * timePassed;
			this.vel.y *= 0.9999d * timePassed;
			if (Math.abs( this.vel.x ) < 0.001d ) {
				this.vel.x = 0;
			}
			if (Math.abs(this.vel.y) < 0.001d ) {
				this.vel.y = 0;
			}

			if (this.vel.x < -this.speed) {
				this.vel.x = -this.speed;
			}
			else if (this.vel.x > this.speed) {
				this.vel.x = this.speed;
			}
			if (this.vel.y < -this.speed) {
				this.vel.y = -this.speed;
			}
			else if (this.vel.y > this.speed) {
				this.vel.y = this.speed;
			}
		}

		this.pos.x += this.vel.x * timePassed;
		this.pos.y += this.vel.y * timePassed;

		// Last

	}
}