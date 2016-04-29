package com.mygdx.game;

import java.io.*;

public class MyFileReader {
	private String path;
	public MyFileReader(String path) {
		this.path = path;
	}

	public String readFile(String readFile) {
		BufferedReader buffer = null;
		String output = "";
		StringBuilder stringBuilder = new StringBuilder();
		try {
			buffer = new BufferedReader( new FileReader( this.path + readFile ) );
			String line;
			char code;
			while ((line = buffer.readLine()) != null) {
				/*code = (char) c;
				output += String.valueOf(c);*/
				stringBuilder.append( line );
				stringBuilder.append( "\n" );
			}
			output = stringBuilder.toString();
		}
		catch (IOException ex) {

		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}
				catch (IOException ex) {}
			}
		}
		return output;
	}

	public void writeFile( String readFile ,String[] lines) {
		BufferedWriter buffer = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			buffer = new BufferedWriter( new FileWriter( this.path + readFile ) );
			for ( int i = 0; i < lines.length; i++ ) {
				buffer.write( lines[i] , 0, lines[i].length() );
				buffer.newLine();
			}
		}
		catch (IOException ex) { }
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}
				catch (IOException ex) {}
			}
		}
	}
}