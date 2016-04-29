package com.mygdx.game;

import java.io.*;

public class MyFileReader {
	private FileInputStream file = null;
	private String path;
	public MyFileReader(String path) {
		this.path = path;
	}

	public String readFile() {
		String output = "";
		try {
			file = new FileInputStream( this.path );
			int c;
			char code;
			while ((c = file.read()) != -1) {
				code = (char) c;
				output += String.valueOf(c);
			}
		}
		catch (IOException ex) {

		}
		finally {
			if (file != null) {
				try {
					file.close();
				}
				catch (IOException ex) {}
			}
		}
		return output;
	}
}