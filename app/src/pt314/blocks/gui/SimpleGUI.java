package pt314.blocks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pt314.blocks.game.Block;
import pt314.blocks.game.Direction;
import pt314.blocks.game.GameBoard;
import pt314.blocks.game.HorizontalBlock;
import pt314.blocks.game.TargetBlock;
import pt314.blocks.game.VerticalBlock;

/**
 * Simple GUI test...
 */
public class SimpleGUI extends JFrame implements ActionListener {

	private int rows = 5;
	private int cols = 5;
	private int targetRow = 2;

	private GameBoard board;
	
	// currently selected block
	private Block selectedBlock;
	private int selectedBlockRow;
	private int selectedBlockCol;

	private GridButton[][] buttonGrid;
	
	private JMenuBar menuBar;
	private JMenu gameMenu, helpMenu;
	private JMenuItem newGameMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem aboutMenuItem;
	
	public SimpleGUI() {
		super("Blocks");
		
		initMenus();
		
		initBoard();
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public SimpleGUI(String str) throws IllegalArgumentException, IOException
	{
		super("Blocks");
		
		initMenus();
		
		initBoard(str);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

	private void initMenus() {
		menuBar = new JMenuBar();
		
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		
		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		newGameMenuItem = new JMenuItem("New game");
		newGameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					new SimpleGUI("app\\res\\puzzles\\puzzle-101.txt");
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
			}
		});
		gameMenu.add(newGameMenuItem);
		
		gameMenu.addSeparator();
		
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		gameMenu.add(exitMenuItem);
		
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(SimpleGUI.this, "Sliding blocks!");
			}
		});
		helpMenu.add(aboutMenuItem);
		
		setJMenuBar(menuBar);
	}
	
	private void initBoard() {
		board = new GameBoard(rows, cols);
		buttonGrid = new GridButton[rows][cols];
		
		setLayout(new GridLayout(rows, cols));
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				GridButton cell = new GridButton(row, col);
				cell.setPreferredSize(new Dimension(64, 64));
				cell.addActionListener(this);
				cell.setOpaque(true);
				buttonGrid[row][col] = cell;
				add(cell);
			}
		}
		
		// add some blocks for testing...
		board.placeBlockAt(new HorizontalBlock(), 0, 0);
		board.placeBlockAt(new HorizontalBlock(), 4, 4);
		board.placeBlockAt(new VerticalBlock(), 1, 3);
		board.placeBlockAt(new VerticalBlock(), 3, 1);
		board.placeBlockAt(new TargetBlock(), 2, 2);
		
		updateUI();
	}
	
	private void initBoard(String str) throws IllegalArgumentException, IOException
	{
		Path filePath = Paths.get(str);
		Scanner scanner = new Scanner(filePath);
		rows = scanner.nextInt();
		cols = scanner.nextInt();
		int targetblocks = 0;
		
		if(rows < 1 || cols < 1)
		{
			throw new IllegalArgumentException();
		}
		String next;
		char[] fileLine;
		
		board = new GameBoard(rows, cols);
		buttonGrid = new GridButton[rows][cols];
		setLayout(new GridLayout(rows, cols));
		
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				GridButton cell = new GridButton(row, col);
				cell.setPreferredSize(new Dimension(64, 64));
				cell.addActionListener(this);
				cell.setOpaque(true);
				buttonGrid[row][col] = cell;
				add(cell);
			}
		}
		
		for(int i = 0; i< rows; i++)
		{
			next = scanner.next();
			fileLine = next.toCharArray();
			for(int j = 0; j< cols; j++)
			{
				if(fileLine[j] == 'H')
				{
					board.placeBlockAt(new HorizontalBlock(), i, j);
				}
				else if(fileLine[j] == 'V')
				{
					board.placeBlockAt(new VerticalBlock(), i, j);
				}
				else if(fileLine[j] == 'T')
				{
					if(targetblocks < 1)
					{
						targetblocks++;
						board.placeBlockAt(new TargetBlock(), i, j);
						targetRow = i;
						for(int k = j; k<cols;k++)
						{
							if(fileLine[k] == 'H')
							{
								throw new IllegalArgumentException("Horizontal blocking the Target block");
							}
						}
					}
					else throw new IllegalArgumentException("Multiple Target Blocks");
				}
				else if(fileLine[j] == '.')
				{
					
				}
				else
				{
					throw new IllegalArgumentException("Invalid Token");
				}
			}
		}
		
		scanner.close();
		updateUI();
		
	}

	// Update display based on the state of the board...
	// TODO: make this more efficient
	private void updateUI() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Block block = board.getBlockAt(row, col);
				JButton cell = buttonGrid[row][col];
				if (block == null)
					cell.setBackground(Color.LIGHT_GRAY);
				else if (block instanceof TargetBlock)
					cell.setBackground(Color.YELLOW);
				else if (block instanceof HorizontalBlock)
					cell.setBackground(Color.BLUE);
				else if (block instanceof VerticalBlock)
					cell.setBackground(Color.RED);
			}
		}
	}

	/**
	 * Handle board clicks.
	 * 
	 * Movement is done by first selecting a block, and then
	 * selecting the destination.
	 * 
	 * Whenever a block is clicked, it is selected, even if
	 * another block was selected before.
	 * 
	 * When an empty cell is clicked after a block is selected,
	 * the block is moved if the move is valid.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle grid button clicks...
		GridButton cell = (GridButton) e.getSource();
		int row = cell.getRow();
		int col = cell.getCol();
		System.out.println("Clicked (" + row + ", " + col + ")");
		
		if (selectedBlock == null || board.getBlockAt(row, col) != null) {
			selectBlockAt(row, col);
		}
		else {
			moveSelectedBlockTo(row, col);
		}
	}

	/**
	 * Select block at a specific location.
	 * 
	 * If there is no block at the specified location,
	 * the previously selected block remains selected.
	 * 
	 * If there is a block at the specified location,
	 * the previous selection is replaced.
	 */
	private void selectBlockAt(int row, int col) {
		Block block = board.getBlockAt(row, col);
		if (block != null) {
			selectedBlock = block;
			selectedBlockRow = row;
			selectedBlockCol = col;
		}
	}
	
	/**
	 * Try to move the currently selected block to a specific location.
	 * 
	 * If the move is not possible, nothing happens.
	 */
	private void moveSelectedBlockTo(int row, int col) {
		
		int vertDist = row - selectedBlockRow;
		int horzDist = col - selectedBlockCol;
		
		if (vertDist != 0 && horzDist != 0) {
			System.err.println("Invalid move!");
			return;
		}
		
		Direction dir = getMoveDirection(selectedBlockRow, selectedBlockCol, row, col);
		int dist = Math.abs(vertDist + horzDist);
		
		if (!board.moveBlock(selectedBlockRow, selectedBlockCol, dir, dist)) {
			System.err.println("Invalid move!");
		}
		else 
		{
			if(selectedBlock.getClass().getName() == "pt314.blocks.game.TargetBlock")
			{
				if(col == cols-1)
				{
					System.out.println("COOL");
				}
			}
			selectedBlock = null;
			updateUI();
		}
	}

	/**
	 * Determines the direction of a move based on
	 * the starting location and the destination.
	 *  
	 * @return <code>null</code> if both the horizontal distance
	 * 	       and the vertical distance are not zero. 
	 */
	private Direction getMoveDirection(int startRow, int startCol, int destRow, int destCol) {
		int vertDist = destRow - startRow;
		int horzDist = destCol - startCol;
		if (vertDist < 0)
			return Direction.UP;
		if (vertDist > 0)
			return Direction.DOWN;
		if (horzDist < 0)
			return Direction.LEFT;
		if (horzDist > 0)
			return Direction.RIGHT;
		return null;
	}
}

//if the block being moved is T
//check if t.col == col-1 and if so show message.

//last part is changing the look.
//jeepers mr. you sure are good at this game
