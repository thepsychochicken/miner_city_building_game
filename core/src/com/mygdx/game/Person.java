package com.mygdx.game;

public class Person {
	public FloatPos pos;
	public FloatPos lastPos;
	public Person() {
		pos = new FloatPos( (double) 64, (double) 20 );
		lastPos = new FloatPos( (double) 0, (double) 0 );
	}

	public void calc() {
		// First

		// Middle

		// Last
		lastPos.x = pos.x;
		lastPos.y = pos.y;
	}
}