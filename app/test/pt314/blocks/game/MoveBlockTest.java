package pt314.blocks.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoveBlockTest 
{
	GameBoard gb;
	Block Hblock;
	Block Vblock;
	Block NullBlock;
	Direction dir;
	
	@Before
	public void setUp() 
	{
		gb = new GameBoard(5,5);
		Hblock = new HorizontalBlock();
		Vblock = new VerticalBlock();
		NullBlock = null;
		gb.placeBlockAt(Vblock, 1, 1);
		gb.placeBlockAt(Hblock, 3, 3);
		gb.placeBlockAt(NullBlock,0,0);
		dir = Direction.UP;
	}
	
	@Test
	public void testValidVerticalMove() 
	{
		assertTrue(gb.moveBlock(1, 1, dir, 1));
		dir = Direction.DOWN;
		assertTrue(gb.moveBlock(0, 1, dir, 1));
	}
	
	@Test
	public void testValidHorizontalMove() 
	{
		dir = Direction.RIGHT;
		assertTrue(gb.moveBlock(3, 3, dir, 1));
		dir = Direction.LEFT;
		assertTrue(gb.moveBlock(3, 4, dir, 1));
	}
	
	@Test
	public void testOutofBounds() 
	{
		dir = Direction.RIGHT;
		assertTrue(gb.moveBlock(3, 3, dir, 1)); //move to wall is valid
		assertFalse(gb.moveBlock(3, 4, dir, 1)); //tries to go out of bounds and fails.
		
		dir = Direction.UP;
		assertTrue(gb.moveBlock(1, 1, dir, 1));  //move to wall is valid
		assertFalse(gb.moveBlock(0, 1, dir, 1)); //tries to go out of bounds and fails.
	}
	
	@Test
	public void testInValidMoveDirection() 
	{
		//can only move in a direction corresponding to its type either horizontal or vertical.
		dir = Direction.RIGHT;
		assertFalse(gb.moveBlock(0, 1, dir, 1));
		
		dir = Direction.UP;
		assertFalse(gb.moveBlock(3, 4, dir, 1));

	}
	
	@Test
	public void testNullBlock() 
	{
		dir = Direction.RIGHT;
		assertFalse(gb.moveBlock(0, 0, dir, 1));
	}
	
	@Test
	public void testBlockingBlock() 
	{
		//can legally move when unblocked
		gb.placeBlockAt(Hblock, 3, 1);
		dir = Direction.LEFT;
		assertTrue(gb.moveBlock(3, 1, dir, 1));
		dir = Direction.RIGHT;
		assertTrue(gb.moveBlock(3, 0, dir, 1));
		
		//cannot move onto a space with a block and cannot move past it.
		gb.placeBlockAt(new HorizontalBlock(), 3, 2);
		assertFalse(gb.moveBlock(3, 1, dir, 1));
		assertFalse(gb.moveBlock(3, 1, dir, 2));
	}

	
}