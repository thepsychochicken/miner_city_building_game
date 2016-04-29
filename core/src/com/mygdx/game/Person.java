package com.mygdx.game;

public class Person {
	public double speed;
	public FloatPos pos, center;
	public double width, height;
	private FloatPos vel;
	private FloatPos lastPos;
	public boolean dirUp, dirDown, dirLeft, dirRight;
	public boolean inAir, isJumping;

	public Person() {
		this.pos = new FloatPos( (double) 64, (double) 20 );
		this.center = new FloatPos( (double) 64, (double) 20 );
		this.vel = new FloatPos( (double) 64, (double) 60 );
		this.lastPos = new FloatPos( (double) 0, (double) 0 );
		this.speed = 5;
		this.dirLeft = false;
		this.dirRight = false;
		this.dirDown = false;
		this.dirUp = false;
		this.inAir = false;
		this.height = 4.8d;
		this.width = 2.5d;
		this.isJumping = false;
	}

	private void blockCollide( double timePassed, World world) {

		// Down
		if ( world.getTypeAt( ( int ) this.pos.x, ( int ) ( this.pos.y - 0.00001d ) ) == 0 ) {
			this.vel.y -= 50 * timePassed;
		}
		else if (world.getTypeAt( ( int ) this.pos.x, ( int ) ( this.pos.y - 0.000005d ) ) != 0) {
			this.vel.y = 0;
			if (world.getTypeAt( ( int ) this.pos.x, ( int ) this.pos.y ) != 0) {
				this.pos.y = ( ( int ) this.pos.y + 1 );
			}
			else {
				this.pos.y = ( ( int ) this.pos.y );
			}
		}
	}

	public boolean isMoving() {
		return ( this.lastPos.x != this.pos.x || this.lastPos.y != this.pos.y );
	}

	public void calc( double timePassed, World world ) {
		// First

		this.lastPos.x = pos.x;
		this.lastPos.y = pos.y;

		// Middle

		if (this.dirUp) {
			//this.vel.y += this.speed * timePassed;
		}
		if (this.dirDown) {
			//this.vel.y -= this.speed * timePassed;
		}
		if (this.dirLeft) {
			if (this.vel.x > -this.speed) {	
				this.vel.x -= this.speed * timePassed; 
			}
		}
		if (this.dirRight) {
			if (this.vel.x < this.speed) {
				this.vel.x += this.speed * timePassed; 
			}
		}

		if ( !this.dirRight && !this.dirLeft && !this.dirDown && !this.dirUp ) {
			this.vel.x *= 0.9d;
			this.vel.y *= 0.9d;
			if (Math.abs( this.vel.x ) < 0.01d ) {
				this.vel.x = 0;
			}
			if (Math.abs(this.vel.y) < 0.01d ) {
				this.vel.y = 0;
			}

			if (this.isJumping) {

			}

			/*if (this.vel.y < -this.speed) {
				this.vel.y = -this.speed;
			}
			else if (this.vel.y > this.speed) {
				this.vel.y = this.speed;
			}*/
		}

		this.blockCollide( timePassed, world );

		this.pos.x += this.vel.x * timePassed;
		this.pos.y += this.vel.y * timePassed;


		// Last

		this.center.x = this.pos.x + this.width / 2; 
		this.center.y = this.pos.y + this.height / 2; 

	}
}