package com.mygdx.game;

public class Camera {
	public SlidePos pos;
	public Pos endPos;
	private Slide zoom;
	public int blockSize;
	private int screenW, screenH;
	public Camera( int screenW, int screenH ) {
		pos = new SlidePos( 0, 0 ,300);
		this.endPos = new Pos( 0, 0 );
		this.zoom = new Slide( 80, 80, 500 );
		this.screenW = screenW;
		this.screenH = screenH;
		this.blockSize = this.zoom.getPoint();
	}

	public void update() {
		this.pos.calc();
		this.blockSize = this.zoom.getPoint();
		this.endPos.x = this.pos.x+( int )( this.screenW / this.blockSize ) + 1;
		this.endPos.y = this.pos.y+( int )( this.screenH / this.blockSize ) + 1;
	}

	public void setZoom(int zoom) {
		this.zoom.setEnd(zoom);
	}

	public void setPos(int x, int y) {
		this.pos.setPos( x, y );
	}
}