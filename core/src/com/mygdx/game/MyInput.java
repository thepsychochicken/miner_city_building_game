package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;

public class MyInput implements InputProcessor {

	public String typedInput = "";
	public int deltaY = 0;

	public boolean mouseMoved(int mouseX, int mouseY) {
		return false;
	}

	public boolean touchDragged(int mouseX, int mouseY, int pleh) {
		return false;
	}

	public boolean touchUp(int mouseX, int mouseY, int pleh, int pleh2) {
		return false;
	}

	public boolean touchDown(int mouseX, int mouseY, int pleh, int pleh2) {
		return false;
	}

	public void clear() {
		typedInput = "";
		deltaY = 0;
	}

	public boolean keyTyped(char key) {
		//System.out.println("Key typed: " + key);
		typedInput = typedInput + key;
		return false;
	}

	public boolean keyUp(int key) {
		return false;
	}

	public boolean keyDown(int key) {
		return false;
	}

	public boolean scrolled(int amount) {
		this.deltaY += amount; 
		return false;
	}
}