package com.mygdx.game;

public class FloatPos {
	public double x, y;
	private double lastX, lastY;
	public FloatPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void normalize() {
		if (lastX != x || lastY != y) {
			double len = Math.sqrt( this.x * this.x + this.y * this.y );
			this.x /= len;
			this.y /= len;
			lastX = x;
			lastY = y;
		}
	}
}