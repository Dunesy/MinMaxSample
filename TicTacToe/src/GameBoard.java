import java.util.*;


class GamePiece
{
	private int i;
	private int j;
	private int k;
	char type;

	
	GamePiece(int i, int j, int k,  char type)
	{
		this.i = i;
		this.j = j;
		this.k = k;
		this.type = type;
	}
	
	public int i(){ return i; }
	public int j() { return j; }
	public int k() { return k; }
	public char getType(){return type;}
	public boolean isType( char t ) { return (t == type) ;}
	
}

public class GameBoard {


	public enum Direction {N , NW, SW, S, SE, E, NE, W }
	static int BOARD_SIZE = 4;
	
	private char[][][] board;
	private ArrayList<GamePiece> Xs; 
	private ArrayList<GamePiece> Os;
	
	//Depth Search Requirements
	private ArrayList<GameBoard> successors;
	private int score;
	private int depth;
	
	GameBoard()
	{
		board = new char[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
		successors = new ArrayList<GameBoard>();
		for (int k = 0 ; k < BOARD_SIZE; k++)
			for (int i = 0 ; i < BOARD_SIZE; i++)
				for (int j = 0 ; j < BOARD_SIZE; j++)
					board[i][j][k] = ' ';
		Xs = new ArrayList<GamePiece>();
		Os = new ArrayList<GamePiece>();
		score = 0;
		depth = 0;
		evaluate();
	}
	GameBoard(ArrayList<GamePiece> xs, ArrayList<GamePiece> os, GamePiece p, int depth)
	{
		board = new char[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
		successors = new ArrayList<GameBoard>();
		for (int k = 0 ; k < BOARD_SIZE; k++)
			for (int i = 0 ; i < BOARD_SIZE; i++)
				for (int j = 0 ; j < BOARD_SIZE; j++)
					board[i][j][k] = ' ';
		
		this.depth = depth;
		Xs = new ArrayList<GamePiece>();
		Os = new ArrayList<GamePiece>();
		for (GamePiece a: xs) {Xs.add(a);}
		for (GamePiece a: os) {Os.add(a); }
		
		if (p.getType() == 'X')
			Xs.add(p);
		else
			Os.add(p);
		
		
		for (GamePiece a : Xs)
		{
			board[a.i()][a.j()][a.k()] = 'X';
		}
		for (GamePiece a : Os)
		{
			board[a.i()][a.j()][a.k()] = 'O';
		}
		depth = 0;
		evaluate();
	}	
	//Game Piece Specific
	public void addPiece(GamePiece p)
	{ 
		if (p.getType() == 'X')
			Xs.add(p);
		else
			Os.add(p);
	}
	public void clearPieces() { Xs.clear(); Os.clear(); }
	
	public void setScore(int x) { score = x;}
	public ArrayList<GamePiece> getXs() {return Xs; }
	public ArrayList<GamePiece> getOs() {return Os; }
	
	//Game Board Methods
	public void addPiece(int i, int j, int k, char t)
	{
		GamePiece p = new GamePiece(i, j, k, t);
		addPiece(p);
		board[i][j][k] = t;
	}
	public void clearBoard() 
	{ 
		for (int i = 0 ; i < BOARD_SIZE; i++)
			for (int j = 0 ; j < BOARD_SIZE; j++)
				for (int k = 0 ; k < BOARD_SIZE; k++)
					board[i][j][k] = 0;
		clearPieces();
	} 
	public int getScore() {return score; }
	public char[][][] getBoard() { return board; }
	public void setDepth(int x) { depth = x; }
	//PrintLevel
	public void printBoard()
	{
		System.out.println( toString() );
	}
	public String toString() 
	{
		String s = "";
		s += "Board One\tBoard Two\tBoard Three\tBoard Four\n";
		for (int j = 0; j < BOARD_SIZE ; j++)
		{
			s += "---------\t---------\t---------\t---------\n";
			for (int k = 0 ; k < BOARD_SIZE; k++)
			{
				for (int i = 0; i < BOARD_SIZE; i++ )
				{
					s += "|" + board[i][j][k];		
				}
				s += "|\t";
			}
			s+= "\n";
		}
		s += "---------\t---------\t---------\t---------\n";
		return s;		
	}	
	//State Generator
	
	public void generateStates()
	{
		if (successors.isEmpty())
		{
			char t;
			if (depth % 2 == 0)
				t = 'X';
			else
				t = 'O';
				for (int k = 0 ; k < BOARD_SIZE ; k++)
					for (int i = 0; i < BOARD_SIZE; i++)
						for( int j = 0 ; j < BOARD_SIZE; j++)
							if (board[i][j][k] == ' ')
								successors.add( new GameBoard(Xs, Os, new GamePiece(i, j, k, t), depth + 1));
		}
	}
	public int getDepth() { return depth; }
	
	public ArrayList<GameBoard> getSuccessors() { return successors; } 
	
	public boolean won(ArrayList<GamePiece> gamepieces)
	{
		//Recursively Checks if Win Condition is Met
		for (GamePiece p : gamepieces)
		{
			if (p.i() == 0 || p.j() == 0 || p.k() == 0)
			{
				// Scan I and J Plane			
				if(recurs_helper_ij(p.i() , p.j(), p.k(), p.getType(), Direction.E) == 4)
					return true;
				else if(recurs_helper_ij(p.i() , p.j(), p.k(), p.getType(), Direction.SE) == 4)
					return true;
				else if(recurs_helper_ij(p.i() , p.j(), p.k(), p.getType(), Direction.S) == 4)
					return true;
				else if (recurs_helper_ij(p.i(), p.j(), p.k(), p.getType(), Direction.SW) == 4)
					return true;
				
				//Scan I and K plane			
				else if(recurs_helper_ik(p.i() , p.j(), p.k(), p.getType(), Direction.E) == 4)
					return true;
				else if(recurs_helper_ik(p.i() , p.j(), p.k(), p.getType(), Direction.SE) == 4)
					return true;
				else if(recurs_helper_ik(p.i() , p.j(), p.k(), p.getType(), Direction.S) == 4)
					return true;
				else if (recurs_helper_ik(p.i(), p.j(), p.k(), p.getType(), Direction.SW) == 4)
					return true;
				// Scan J and K Plane
				else if (recurs_helper_jk(p.i() , p.j(), p.k(), p.getType(), Direction.E) == 4)
					return true;
				else if(recurs_helper_jk(p.i() , p.j(), p.k(), p.getType(), Direction.SE) == 4)
					return true;
				else if(recurs_helper_jk(p.i() , p.j(), p.k(), p.getType(), Direction.S) == 4)
					return true;
				else if (recurs_helper_jk(p.i(), p.j(), p.k(), p.getType(), Direction.SW) == 4)
					return true;
				
				else if (board[0][0][0] == p.getType() && board[1][1][1] == p.getType() && board[2][2][2] == p.getType() && board[3][3][3] == p.getType())
					return true;
				else if (board[3][0][0] == p.getType() && board[2][1][1] == p.getType() && board[1][2][2] == p.getType() && board[0][3][3] == p.getType())
					return true;
				else if (board[0][0][3] == p.getType() && board[1][1][2] == p.getType() && board[2][2][1] == p.getType() && board[3][3][0] == p.getType())
					return true;
				else if (board[0][3][0] == p.getType() && board[1][2][1] == p.getType() && board[2][1][2] == p.getType() && board[3][0][3] == p.getType())
					return true;
			}
		}
		// Check Diagonals
		
		return false;		
	}
	
	
	public int recurs_helper_ij(int i, int j, int k, char t, Direction d)
	{
		if (i < 0 || j < 0|| i >= 4 || j >= 4 || board[i][j][k] != t )
			return 0;
		if (d == Direction.E)
			return recurs_helper_ij(i, j + 1, k , t, d) + 1;
		else if (d == Direction.SE)
			return recurs_helper_ij(i + 1, j + 1, k , t, d) + 1;
		else if (d == Direction.S)
			return recurs_helper_ij(i + 1, j, k , t, d) + 1;
		else if (d == Direction.SW)
			return recurs_helper_ij(i + 1, j - 1, k , t ,d) + 1;
		else if (d == Direction.W)
			return recurs_helper_ij(i , j - 1, k, t, d) + 1;
		else if (d == Direction.NW)
			return recurs_helper_ij(i - 1, j - 1, k ,t ,d) + 1;
		else if (d == Direction.N)
			return recurs_helper_ij(i - 1 , j, k , t, d) + 1;
		else
			return recurs_helper_ij(i -1, j + 1, k , t ,d) + 1;
			
	}
	public int recurs_helper_ik(int i, int j, int k, char t, Direction d)
	{
		if ( (i < 0 || i > 3) || (k < 0 || k > 3) || board[i][j][k] != t )
			return 0;
		if (d == Direction.E)
			return recurs_helper_ik(i, j,  k + 1 , t, d) + 1;
		else if (d == Direction.SE)
			return recurs_helper_ik(i + 1, j , k + 1 , t, d) + 1;
		else if (d == Direction.S)
			return recurs_helper_ik(i + 1, j, k  , t, d) + 1;
		else if (d == Direction.SW)
			return recurs_helper_ik(i + 1, j , k - 1 , t ,d) + 1;
		else if (d == Direction.W)
			return recurs_helper_ik(i , j , k - 1, t, d) + 1;
		else if (d == Direction.NW)
			return recurs_helper_ik(i - 1, j , k - 1 ,t ,d) + 1;
		else if (d == Direction.N)
			return recurs_helper_ik(i - 1 , j, k , t, d) + 1;
		else
			return recurs_helper_ij(i - 1, j , k + 1, t ,d) + 1;
	}
	public int recurs_helper_jk(int i, int j, int k, char t, Direction d)
	{
		if ( (k < 0 || k > 3) || (j < 0 || j > 3) || board[i][j][k] != t )
			return 0;		
		if (d == Direction.E)
			return recurs_helper_jk(i, j, k + 1 , t, d) + 1;
		else if (d == Direction.SE)
			return recurs_helper_jk(i, j + 1, k + 1 , t, d) + 1;
		else if (d == Direction.S)
			return recurs_helper_jk(i, j + 1, k , t, d) + 1;
		else if (d == Direction.SW)
			return recurs_helper_jk(i, j + 1 , k - 1 , t ,d) + 1;
		else if (d == Direction.W)
			return recurs_helper_jk(i, j , k - 1, t, d) + 1;
		else if (d == Direction.NW)
			return recurs_helper_jk(i, j , k - 1 ,t ,d) + 1;
		else if (d == Direction.N)
			return recurs_helper_jk(i, j - 1, k , t, d) + 1;
		else
			return recurs_helper_jk(i, j - 1, k + 1 , t ,d) + 1;
	}
	
	void evaluate() 
	{ 
		
		score = evaluatePositions('X');
		score -= evaluatePositions('O');
		score += evaluateTacticalPosition('X');
		score += evaluateTacticalPosition('O');
		//score -= depth * 100;
		//System.out.println("BOARD SCORE: " + score);
	}
	
	int evaluatePositions(char t)
	{
		int score = 0;
		ArrayList<GamePiece> pieces;
	
		if (t == 'X')
			pieces = Xs;
		else 
			pieces = Os;
		
			for (GamePiece p : Xs)
			{				
				if (p.i() > 0 && p.i() < 3 && p.j() > 0 && p.j() < 3 && (p.k() == 0 || p.k() == 3) ) //Middle Squares on Layers 0 and 3
					score += 4;
				else if (p.i() > 0 && p.i() < 3 && p.j() > 0 && p.j() < 3 && (p.k() == 1 || p.k() == 2)) //Middle Squares on Layers 1 and 2
					score += 7;
				else if ( (p.i() == 0 || p.i()  == 3) && p.j() != 3 && p.j() != 0 && (p.k() == 2 || p.k() == 1))  //Edge Squares on Levels 1 and 2
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && p.i() != 3 && p.i() != 0 && (p.k() == 3 || p.k() == 0)) //Edge Squares on Levels 0 and 3
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && (p.i() == 3 || p.i() == 0) && (p.k() == 2 || p.k() == 1) ) // Corners of the Middle Levels
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && (p.i() == 3 || p.i() == 0) && (p.k() == 0 || p.k() == 3) ) //Corners of the Top and Bottom Levels
					score += 7;
			}
		
		
			for (GamePiece p : Os)
			{
				if (p.i() > 0 && p.i() < 3 && p.j() > 0 && p.j() < 3 && (p.k() == 0 || p.k() == 3) ) //Middle Squares on Layers 0 and 3
					score += 4;
				else if (p.i() > 0 && p.i() < 3 && p.j() > 0 && p.j() < 3 && (p.k() == 1 || p.k() == 2)) //Middle Squares on Layers 1 and 2
					score += 7;
				else if ( (p.i() == 0 || p.i()  == 3) && p.j() != 3 && p.j() != 0 && (p.k() == 2 || p.k() == 1))  //Edge Squares on Levels 1 and 2
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && p.i() != 3 && p.i() != 0 && (p.k() == 3 || p.k() == 0)) //Edge Squares on Levels 0 and 3
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && (p.i() == 3 || p.i() == 0) && (p.k() == 2 || p.k() == 1) ) // Corners of the Middle Levels
					score += 4;
				else if ( (p.j() == 0 || p.j() == 3) && (p.i() == 3 || p.i() == 0) && (p.k() == 0 || p.k() == 3) ) //Corners of the Top and Bottom Levels
					score += 7;
			
			}
		
		//System.out.println("Score for Placement: " + score);
		return score;
	}
	
	int evaluateTacticalPosition(char t)
	{
		//Checks piece position in relation to other pieces.  the More pieces that are aligned that are of the same piece type the better the score.
		int score = 0;
		int scores[] = new int[13];
		int x,y,z;
		ArrayList<GamePiece> pieces;
		GamePiece lastadded;		
		
		
		
		if (t == 'X')
			pieces = Xs;
		else 
			pieces = Os;
				
		if (pieces.isEmpty())
			return 0;
		
		for (GamePiece p: pieces)
		{
			x = p.i();
			y = p.j();
			z = p.k();		
			//Check Each Direction
		for (int i = 0 ; i < 13 ; i++)
		{
			if (p.getType() == 'X')
				scores[i] = 1;
			else
				scores[i] = 10;
		}
			
			
			
			
		for (int i = 1; i < BOARD_SIZE; i++)
		{
			//XY Plane					
			scores[0] += checkSquare(x + i, y, z); 
			scores[0] += checkSquare(x - i, y, z);
			
			scores[1] += checkSquare(x , y + i, z);
			scores[1] += checkSquare(x, y - i, z);
				
			//XZ Plane 
			scores[2] += checkSquare(x , y , z + i);
			scores[2] += checkSquare(x, y , z - i);
			
			if (x == y)
			{
				scores[3] += checkSquare(x + i, y + i, z);
				scores[3] += checkSquare(x - i, y - i , z);
			}
			
			if (x == (Math.abs(BOARD_SIZE-1 - y)))
			{
				scores[4] += checkSquare(x + i, y - i, z);
				scores[4] += checkSquare(x - i, y + i, z);
			}
				
			if (x == z)
			{
				scores[5] += checkSquare(x + i, y , z + i);
				scores[5] += checkSquare(x - i, y , z - i);
			}
			if (x == Math.abs(z - BOARD_SIZE - 1))
			{
				scores[6] += checkSquare(x + i, y, z - i);
				scores[6] += checkSquare(x - i, y, z + i);
			}
			//YZ Plane 
				
			if (y == z)
			{
				scores[7] += checkSquare(x , y + i, z + i);
				scores[7] += checkSquare(x , y - i, z - i);
			}
			if (y == Math.abs(z - BOARD_SIZE- 1))
			{
				scores[8] += checkSquare(x , y + i, z - i);
				scores[8] += checkSquare(x , y - i, z + i);
			}
				
				//XYZ Plane
			if (x == y || x == z || y == z)
			{
				scores[9] += checkSquare(x + i, y + i, z + i);
				scores[9] += checkSquare(x - i, y - i, z - i);
				
				scores[10] += checkSquare(x + i, y + i, z - i);
				scores[10] += checkSquare(x - i, y - i, z + i);
				
				scores[11] += checkSquare(x + i, y - i, z + i);
				scores[11] += checkSquare(x - i, y + i, z - i);
				
				scores[12] += checkSquare(x + i, y - i, z - i);
				scores[12] += checkSquare(x - i, y + i, z + i);					
			}
		}
					
		for ( int i = 0;  i < 12 ; i++)
		{
			
			if (scores[i] == 20) { score -= 40; }
			else if (scores[i] == 2) { score += 40; }
			
			else if (scores[i] == 30) {score -= 80;}
			else if (scores[i] == 3) {score += 80; }

			else if (scores[i] == 40) 
			{
				score -= 10000 + depth * 1000; 
			}  //Lost
			else if (scores[i] == 4) 
			{ 
				score += 10000 - depth * 1000; 
			}  //Won
		}
				
	}
	return score;
	}	
		
	
	int checkSquare(int i, int j, int k)
	{
		if (i > 3 || i < 0 || j < 0 || j > 3 || k < 0 || k > 3)
			return 0; 
				
		if (board[i][j][k] == 'X')
			return 1;
		else if (board[i][j][k] != 'X' && board[i][j][k] != ' ' )
			return 10;
		else
			return 0;
	}
	 
	 

}
	
	
	


