import java.util.ArrayList;


class GameState
{
	private int[][] game_model;
	
	private int black_score;
	private int white_score;
	private int depth;
	private ArrayList<GameState> successors;
	public static int CURRENT_PLAYER = 0;
	public static int NEXT_PLAYER = 1;
	
	GameState()
	{
		game_model = new int[8][8];
		successors = new ArrayList<GameState>();
		depth = 0;
		for (int i = 0 ; i < 8; i++)
		{
			for (int j = 0 ; j < 8; j++)
			{
				game_model[i][j] = -1;
			}
		}
		game_model[3][3] = 0;
    	game_model[4][4] = 0;    		
    	game_model[3][4] = 1;
    	game_model[4][3]  = 1;
    	black_score = 2;
    	white_score = 2;
	}
	
	GameState (int model[][], int depth)
	{
		game_model = new int[8][8];
		successors = new ArrayList<GameState>();
		this.depth = depth;
		black_score = 0;
		white_score = 0;
		
		for (int i = 0 ; i < 8; i++)
		{
			for (int j = 0 ; j < 8; j++)
			{			
				game_model[i][j] = model[i][j];
			}
		}
		
	}
	
	//Accessor Methods	
	public int[][] getModel(){ return game_model; }
	public int getDepth() { return depth; }
	public void setDepth(int x) { depth = x;}
	public void incBlackScore() {black_score++;}
	public int getWhiteScore() { return white_score ;  }
	public int getBlackScore() { return black_score; }
	
	public ArrayList<GameState> getSuccessors() { return successors; }
		
	public int getScore()
	{
		int score = white_score - black_score;
	 	//System.out.println( white_score + " " +  black_score);
		if (game_model[0][0] == 0) { score -= 20; }
		else if (game_model[0][0] == 1) { score += 20;}
		
		if (game_model[7][7] == 0) { score -= 20; }
		else if (game_model[7][7] == 1) { score += 20;}
		
		if (game_model[7][0] == 0) { score -= 20; }
		else if (game_model[7][0] == 1) { score += 20;}
		
		if (game_model[0][7] == 0) { score -= 20; }
		else if (game_model[0][7] == 1) { score += 20;}
		
		return score ;
	}
	
	public void setScore()
	{
		for (int i = 0 ; i < 8; i++)
		{
			for (int j = 0 ; j < 8; j++)
			{			
				if (game_model[i][j] == 0)
					black_score++;
				else if(game_model[i][j] == 1)
					white_score++;
			}
		}	
	} 
	
	public boolean valid(int i , int j)
	{
	   	// Check If The Selected Button is Valid
	   	if (game_model[i][j] == -1)
	   	{
	   		//Check if the area around selected button has other selected tiles.
	   		if ( (i + 1 < game_model.length && j+1 < game_model.length && game_model[i+1][j+1] != -1) || //Diagonals
	   			 (i + 1 < game_model.length && j-1 >= 0 && game_model[i+1][j-1] != -1) ||
	   			 (i - 1 >= 0 && j + 1 < game_model.length && game_model[i-1][j+1] != -1)||
	   			 (i - 1 >= 0 && j - 1 >= 0 && game_model[i-1][j-1] != -1) || //Diagonals
	   			 (i + 1 < game_model.length && game_model[i+1][j] != -1) || //or
	   			 (i - 1 >= 0 && game_model[i-1][j] != -1) ||                 //Or
	   			 (j + 1 < game_model.length && game_model[i][j+1] != -1) || //or
	   		     (j - 1 >= 0 && game_model[i][j-1] != -1))   		
	   		{
	   		//	System.out.println("Within Boundaries");
	   			return canFlip(i , j);
	   		} 
	   		else 
	   		{
	   			return false;
	   		}	
	   	}
	   	else
	   	{
	   		return false;
	   	}
	  
	}
	
	public boolean canFlip(int i , int j)
	  {
	  	boolean goodspot = false;
	  	
		if (i + 1 < game_model.length && game_model[i+1][j] == NEXT_PLAYER ){		
			goodspot = goodspot || canFlipHelper(i + 1 , j, "EAST");	
		}
		if (i + 1 < game_model.length && j - 1 >= 0 && game_model[i+1][j-1] == NEXT_PLAYER)
			goodspot = goodspot || canFlipHelper(i+1 , j-1 , "SOUTH EAST");
			
		if (j - 1 >= 0 && game_model[i][j-1] == NEXT_PLAYER )
			goodspot = goodspot  || canFlipHelper(i, j - 1 , "SOUTH");
			
		if (i - 1 >= 0 && j - 1 >= 0 && game_model[i-1][j-1] == NEXT_PLAYER)
			goodspot = goodspot || canFlipHelper(i-1, j-1, "SOUTH WEST");
		
		if (i - 1 >= 0 && game_model[i-1][j] == NEXT_PLAYER){
			goodspot = goodspot || canFlipHelper(i-1, j, "WEST");		
		}
			
		if (i - 1 >= 0 && j + 1 < game_model.length && game_model[i-1][j + 1] == NEXT_PLAYER )
			goodspot = goodspot || canFlipHelper(i-1, j + 1 , "NORTH WEST");
				
		if (j + 1 < game_model.length && game_model[i][j+1] == NEXT_PLAYER)
			goodspot = goodspot || canFlipHelper(i, j+1, "NORTH");		
		
		if (i + 1 < game_model.length && j + 1 < game_model.length && game_model[i+1][j+1] == NEXT_PLAYER)
		{
		
			goodspot = goodspot || canFlipHelper(i+1, j+1, "NORTH EAST");
		}	
		
		return goodspot;
		
	  }
	
	public boolean canFlipHelper(int i, int j, String direction)
	{
	if (i < 0 || j < 0 || i >= game_model.length || j >= game_model.length)
		return false;
	  	
	if (game_model[i][j] == -1)
		return false;
	  		
	if (game_model[i][j] == CURRENT_PLAYER) 
		return true;  	
	  	
	if (direction.equals("EAST")) // Conquer And Destroy
	{
		return canFlipHelper(i + 1 , j, "EAST");
	} 	
	else if (direction.equals("SOUTH EAST"))
	{
		return canFlipHelper(i + 1 , j -1 , "SOUTH EAST");
	}
	  	else if (direction.equals("SOUTH"))
	  	{
	  		return canFlipHelper(i, j-1 , "SOUTH");
	  	}
	  	else if (direction.equals("SOUTH WEST"))
	  	{
	  		return canFlipHelper(i - 1 , j -1 , "SOUTH WEST");
	  	}
	  	else if (direction.equals("WEST"))
	  	{
	  		return canFlipHelper(i - 1 , j, "WEST");
	  	}
	  	else if (direction.equals("NORTH WEST"))
	  	{
	  		return canFlipHelper(i - 1 , j + 1 , "NORTH WEST");
	  	}
	  	else if (direction.equals("NORTH"))
	  	{
	  		return canFlipHelper(i , j + 1, "NORTH");
	  	}
	  	else
	  	{
	  		//System.out.println("North East Check");
	  		return canFlipHelper(i + 1 , j + 1, "NORTH EAST");
	  	}
	  	
	  }
	  
	public void flipChips( int i, int j )
	  {
		int x = i;
		int y = j;
		
		if (i + 1 < game_model.length && game_model[i+1][j] == NEXT_PLAYER && canFlipHelper(i+1, j, "EAST") ) {while (game_model[x+1][j] != CURRENT_PLAYER) {x++; flipChip(x,y); }}
		x = i;
		y = j;
		if (i + 1 < game_model.length && j + 1 < game_model.length && game_model[i+1][j+1] == NEXT_PLAYER && canFlipHelper(i+1, j+1, "NORTH EAST") ) { while (game_model[x+1][y+1] != CURRENT_PLAYER) {x++; y++; flipChip(x,y); }}
		x = i;
		y = j;
		if (i + 1 < game_model.length && j - 1 >= 0 &&  game_model[i+1][j-1] == NEXT_PLAYER && canFlipHelper(i+1, j-1, "SOUTH EAST") ) { while (game_model[x+1][y-1] != CURRENT_PLAYER) {x++; y--; flipChip(x,y); }}
		x = i;
		y = j;
		if (j - 1 >= 0 && game_model[i][j-1] == NEXT_PLAYER && canFlipHelper(i, j-1, "SOUTH") ) {while (game_model[x][y-1] != CURRENT_PLAYER) {y--;flipChip(x,y); }}
		x = i;
		y = j;
		if (i - 1 >= 0 && j - 1 >= 0 && game_model[i-1][j-1] == NEXT_PLAYER && canFlipHelper(i-1, j-1, "SOUTH WEST") ) {while (game_model[x-1][y-1] != CURRENT_PLAYER) {x-- ; y--; flipChip(x,y); }}
		x = i;
		y = j;
		if (i - 1 >= 0 && game_model[i-1][j] == NEXT_PLAYER && canFlipHelper(i-1, j, "WEST") ) {while (game_model[x-1][y] != CURRENT_PLAYER) {x-- ;flipChip(x,y); }}
		x = i;
		y = j;
		if (i - 1 >= 0 && j + 1 < game_model.length && game_model[i-1][j+1] == NEXT_PLAYER && canFlipHelper(i-1, j+1, "NORTH WEST") ) {while (game_model[x-1][y+1] != CURRENT_PLAYER) {x-- ; y++; flipChip(x,y); }}
		x = i;
		y = j;
		if (j + 1 < game_model.length && game_model[i][j+1] == NEXT_PLAYER && canFlipHelper(i, j+1 , "NORTH") ) {while (game_model[x][y+1] != CURRENT_PLAYER) {y++ ;flipChip(x,y); }}
		x = i;
		y = j;
		 
	  }
	  
	public void flipChip(int i , int j)
	  {
	  	game_model[i][j] = CURRENT_PLAYER;
	  }
	 
	public void generateStates()
	{
		for ( int i = 0 ; i < 8; i++)
			for ( int j = 0 ; j < 8 ; j ++)
			{
				if (valid(i, j))
				{
					int new_board[][] = new int[8][8];
					GameState new_state;
					for (int x = 0 ; x < 8; x++)
						for (int y = 0 ; y < 8; y++)
						{
							new_board[x][y] = game_model[x][y];
						}
					if (depth % 2 == 0)
						new_board[i][j] = 1;
					else 
						new_board[i][j] = 0;
					
					new_state = new GameState(new_board, depth + 1);					
					new_state.flipChips(i, j);
					new_state.setScore();
					successors.add(new_state);
					//System.out.println("Potential Move: " + i + " " + j);
				}		
			}
		
		return;
	}
	 
	
	public boolean gameOver() { return ( black_score + white_score == 64 || successors.isEmpty()) ; }
	
	
	
}