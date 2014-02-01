
/**
 * @(#)Game.java
 * Brendan Smith / Danny Lu
 * @version 1.00 2010/4/20
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;


public class Game {

	// Game Variables
	private int player1, player2;
	private int current_search_depth;
	private Reversi_Window game;
	private GameState current_state;
	private ArrayList<GameState> moves;
	
	
	Game() 
    {
   		// Initially sets The Model to -1 to show no tiles are there.
    	current_search_depth = 3;
		current_state = new GameState();
    	moves = new ArrayList<GameState>();
    	// Initializes the Game Window
    	game = new Reversi_Window("Game Board");    	
    	game.setSize(600,600);
    	game.setLocation(500,300);
    	game.setVisible(true);
    	update(current_state);
    	
    	
    	ActionListener panelActionListener = new ActionListener() {
		
    		
		public void actionPerformed(ActionEvent event) 
		{		
			JButton[][] board = game.getBoard().getPanels();	
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j <  8 ; j++ )
					{
					if (event.getSource() == board[i][j])
						handleEvent(i, j);				
					}
			}
		}
		};
    	
    	// Adding the Event Listeners to each tile    	
    	for (int i = 0 ; i < 8; i ++)
    	{
    		for (int  j = 0 ; j < 8; j++)
    		{
    			game.getBoard().getPanels()[i][j].addActionListener(panelActionListener);
    		}
    	}
    }
    
  public void handleEvent(int i , int j)
  {
   	//System.out.println("X: " + i + "Y: " + j);
   	
   	if ( current_state.valid(i, j) )
   	{		
		current_state.getModel()[i][j] = 0;	   		
   		current_state.flipChips(i, j);
   		
   		moves.add(current_state);
   		update(current_state);
   		
   		GameState.CURRENT_PLAYER = 1;
   		GameState.NEXT_PLAYER = 0;

   		MinMax(current_state);
   		   		
   		current_state.setDepth(0);
   		update(current_state);
   		
		GameState.CURRENT_PLAYER = 0;
		GameState.NEXT_PLAYER = 1;
		//if (checkGameOver())
			//System.exit(0);
   		
   		
   	}
  } 
   

  public void update(GameState current)
  {
	  int board[][] = current.getModel();
	  for (int i = 0 ; i < board.length ; i++)
	  {
		  for (int j = 0 ; j < board.length; j++)
		  {
			  if (board[i][j] == 1)
				  game.getBoard().getPanels()[i][j].setBackground(Color.white);
			  else if (board[i][j] == 0)
				  game.getBoard().getPanels()[i][j].setBackground(Color.black);	  
		  }
	  }
   }
	  
  //Game AI Logic Mini-Max Tree
	public boolean CutOffTest(GameState state)
	{
		if (state.getDepth() == current_search_depth || state.gameOver() )
			return true;
		else
			return false;
	}	
		
	
	public void MinMax(GameState state)
	{
		state.generateStates();
		ArrayList<GameState> successors = state.getSuccessors();
		int value = -999999999;
		int value2 = value;
		for (GameState b : successors)
		{
			value = Math.max(value , MinValue(b));
			//System.out.println(value);
			if (value != value2)
			{
				value2 = value;
				current_state = b;
			}
			
		}	
		current_state.getSuccessors().clear();
		successors.clear();
		//System.out.println("Current Value of Choice " + current_state.getScore());
		moves.add(current_state);
		
	}
	
	public int MinValue(GameState state)
	{
		
		if (state.getDepth() % 2 == 1)
		{
			GameState.CURRENT_PLAYER = 0;
			GameState.NEXT_PLAYER = 1;
		}
		else
		{
			GameState.CURRENT_PLAYER = 1;
			GameState.NEXT_PLAYER = 0;
		}
		//if (state.getDepth() == 3) System.out.println(state.getDepth() + " score " + (state.getWhiteScore() -  state.getBlackScore()));
		state.generateStates();
		
		//System.out.println(current_state.getDepth());
		
		if (CutOffTest(state))
			return state.getScore(); 
		
		
		//System.out.println(state.getDepth());
		ArrayList<GameState> successors = state.getSuccessors();
		int value = 999999999;
		for (GameState b : successors)
		{
			value = Math.min(value , MaxValue(b));
		}
		successors.clear();
		//System.out.println(state.getDepth() + " score " + (state.getWhiteScore() -  state.getBlackScore()));
		return value;
	}

	public int MaxValue(GameState state)
	{
		
		if (state.getDepth() % 2 == 0)
		{
			GameState.CURRENT_PLAYER = 1;
			GameState.NEXT_PLAYER = 0;
		}
		else
		{
			GameState.CURRENT_PLAYER = 0;
			GameState.NEXT_PLAYER = 1;
		}
		state.generateStates();		
				
		if (CutOffTest(state))
			return state.getScore();
			
		int value = -999999999;
		ArrayList<GameState> successors = state.getSuccessors();
		for (GameState b : successors)
		{
			value = Math.max(value , MinValue(b));	
		}
		
	//	System.out.println(state.getDepth() + " score " + (state.getWhiteScore() -  state.getBlackScore()));
		successors.clear();
		return value;
		
		
	}
  
	public boolean checkGameOver()
	{
		current_state.generateStates();
		if (current_state.gameOver())
		{
			current_state.getSuccessors().clear();
			return true;
		}
		else
		{
			current_state.getSuccessors().clear();
			return false;
		}
		
	}
  
 //MAIN METHOD
 public static void main(String args[])
 {
 	Game test = new Game();
 	
 	
 	
 	
 }
   
   
    
}