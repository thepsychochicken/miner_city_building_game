package com.mygdx.game;

import java.util.Date;

public class Slide {
	private int start;
	private int end;
	private int time;
	private long lastTime;

	public Slide(int start, int end, int time) {
		this.start = start;
		this.end = end;
		this.time = time;
		new java.util.Date();
		this.lastTime = System.currentTimeMillis();
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
		int absPoint = this.end - this.start;
		return (int)( percent * absPoint )+this.start;
	}
}