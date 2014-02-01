import java.util.*;

public class TicTacToe 
{
	
	
	public static void main(String args[])
	{
		TicTacToe game = new TicTacToe();
		game.playGame();
	}
	
	private GameBoard current_state;
	private ArrayList<GameBoard> moves; 
	private int current_search_depth;
	
	TicTacToe()
	{
		//Starts off With the Blank GameBoard
		current_state = new GameBoard();
		moves = new ArrayList<GameBoard>(); 
		current_search_depth = 3;
	}
	
	public void playGame()
	{
		current_state.printBoard();
		while (!current_state.won( current_state.getXs()) && !current_state.won( current_state.getOs()))
		{
			if (64 - (current_state.getOs().size() + current_state.getOs().size()) < 30)
				current_search_depth = 4;
			
			MinMax(current_state);
			current_state.printBoard();
			
			if (!current_state.won(current_state.getXs()))
					chooseMove();
				//randomMove(current_state, 'O');
			current_state.setDepth(0);
			current_state.printBoard();
			
		}
		
		for (GameBoard b : moves)
		{
			b.printBoard();
		}
		
		
	}
	
	public boolean CutOffTest(GameBoard state)
	{
		if (state.getDepth()== current_search_depth || state.won(state.getOs()) || state.won(state.getXs()))
			return true;
		else
			return false;
	}	
		
	
	public void MinMax(GameBoard state)
	{
		state.generateStates();
		ArrayList<GameBoard> successors = state.getSuccessors();
		int value = -999999999;
		int value2 = value;
		for (GameBoard b : successors)
		{
			value = Math.max(value , MinValue(b));
			if (value != value2)
			{
				value2 = value;
				current_state = b;
			}
		}
		successors.clear();
		System.out.println("Current Value of Choice" + current_state.getScore());
		moves.add(current_state);
		
	}
	
	public int MinValue(GameBoard state)
	{
		if (CutOffTest(state))
			return state.getScore(); 
		
		state.generateStates();
		ArrayList<GameBoard> successors = state.getSuccessors();
		int value = 999999999;
		for (GameBoard b : successors)
		{
			value = Math.min(value , MaxValue(b));
		}
		successors.clear();
		return value;
	}

	public int MaxValue(GameBoard state)
	{
		if (CutOffTest(state))
			return state.getScore();
		
		state.generateStates();
		int value = -999999999;
		ArrayList<GameBoard> successors = state.getSuccessors();
		for (GameBoard b : successors)
		{
			value = Math.max(value , MinValue(b));
		}
		
		successors.clear();
		return value;
		
		
	}

	public void randomMove(GameBoard state , char t)
	{
		boolean valid = false;
		int i = 0, j = 0, k = 0;
		Random random = new Random();
		while (!valid)
		{
			
			i = random.nextInt(4);
			j = random.nextInt(4);
			k = random.nextInt(4);
			
			if (state.getBoard()[i][j][k] == ' ')
				{
					valid = true;				
					GamePiece p = new GamePiece(i, j , k , t);
					current_state = new GameBoard(state.getXs(), state.getOs(), p, 0);
					moves.add(current_state);
				}
			 			
		}
		
		
	}
	
	void chooseMove()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the IJK Coordinates One At A Time");
		int i = scanner.nextInt();
		int j = scanner.nextInt();
		int k = scanner.nextInt();
	
		GamePiece p = new GamePiece(i,j,k , 'O');
		GameBoard b = new GameBoard(current_state.getXs(), current_state.getOs(), p ,0);
		moves.add(b);
		current_state = b;
		
	}
	
	
	

}