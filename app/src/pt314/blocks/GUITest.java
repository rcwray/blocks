package pt314.blocks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import pt314.blocks.gui.SimpleGUI;

/**
 * Simple GUI test...
 */
public class GUITest {

	public static void main(String[] args) throws IllegalArgumentException, IOException 
	{
		new SimpleGUI("app\\res\\puzzles\\puzzle-101.txt");
	}
}
