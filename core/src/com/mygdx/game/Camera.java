package com.mygdx.game;

public class Camera {
	public SlidePos posIdle, posMovement;
	public Pos endPos, pos;
	private Slide zoom;
	public int blockSize;
	private int screenW, screenH;
	private boolean playerMovedLastTick;
	public Camera( int screenW, int screenH ) {
		posIdle = new SlidePos( 0, 0 ,800);
		posMovement = new SlidePos( 0, 0 ,200);
		posIdle.setEasing("ease-out");
		pos = new Pos( 0, 0 );
		this.endPos = new Pos( 0, 0 );
		this.zoom = new Slide( 80, 80, 500 );
		this.screenW = screenW;
		this.screenH = screenH;
		this.blockSize = this.zoom.getPoint();
		playerMovedLastTick = false;
	}

	public void update() {
		this.posMovement.calc();
		this.posIdle.calc();
		if (playerMovedLastTick) {
			playerMovedLastTick = false;
			this.pos.x = this.posMovement.x;
			this.pos.y = this.posMovement.y;
			this.posIdle.setStart( this.pos.x, this.pos.y );
		}
		else {
			this.pos.x = this.posIdle.x;
			this.pos.y = this.posIdle.y;
		}
		this.blockSize = this.zoom.getPoint();
		this.endPos.x = this.pos.x + this.screenW + this.blockSize;
		this.endPos.y = this.pos.y + this.screenH + this.blockSize;
	}

	public void setZoom(int zoom) {
		this.zoom.setEnd(zoom);
	}

	public void setPos(int x, int y) {
		this.posIdle.setPos( x, y );
		this.posMovement.setPos( x, y );
		//this.posMovement.setStart( this.pos.x, this.pos.y );
		playerMovedLastTick = true;
	}
}