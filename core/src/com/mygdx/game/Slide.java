package com.mygdx.game;

import java.util.Date;

public class Slide {
	private int start;
	private int end;
	private int time;
	private long lastTime;
	private int easing;

	public Slide(int start, int end, int time) {
		this.start = start;
		this.end = end;
		this.time = time;
		new java.util.Date();
		this.lastTime = System.currentTimeMillis();
		this.easing = 0;
	}

	public void setStart( int start) {
		this.start = start;
		new java.util.Date();
		this.lastTime = System.currentTimeMillis();
	}

	public void setEasing(String type) {
		if (type == "ease-in") {
			this.easing = 1;
		}
		else if (type == "ease-out") {
			this.easing = 2;
		}
		else if (type == "no-easing" ) {
			this.easing = 4;
		}
		else if (type == "ease-normal") {
			this.easing = 0;
		}
	}

	public void init(int start, int end, int time) {
		this.start = start;
		this.end = end;
		this.time = time;
		new java.util.Date();
		this.lastTime = System.currentTimeMillis();

	}

	public void setEnd(int end) {
		this.start = this.getPoint();
		this.end = end;
		new java.util.Date();
		this.lastTime = System.currentTimeMillis();
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getPoint() {
		new java.util.Date();
		long nowTime = System.currentTimeMillis();
		float percent = (float)( nowTime - this.lastTime ) / this.time;
		if (percent >= 1) {
			return this.end;
		}
		if ( 
			this.easing == 0 ||
			this.easing == 1 && percent < 0.5f ||
			this.easing == 1 && percent > 0.5f
		) {
			percent = 1f - ( ( float )( Math.cos( percent * Math.PI ) + 1 ) / 2 );
		}
		//System.out.println("Percent: " + percent);
		int absPoint = this.end - this.start;
		return (int)( percent * absPoint )+this.start;
	}
}