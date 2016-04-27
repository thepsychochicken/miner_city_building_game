package com.mygdx.game;

public class SlidePos {
	public int x, y;
	private Slide sX, sY;
	public SlidePos( int x, int y , int time) {
		 this.x = x;
		 this.y = y;
		 this.sX = new Slide( x ,x, time );
		 this.sY = new Slide( x ,x, time );
	}
	public void calc() {
		this.x = this.sX.getPoint();
		this.y = this.sY.getPoint();
	}

	public void setPos( int x, int y ) {
		this.sX.setEnd(x);
		this.sY.setEnd(y);
	}
}