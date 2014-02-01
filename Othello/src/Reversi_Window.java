/**
Me and brendan are awesome!
April 17th 2010
 */
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


//Main View
public class Reversi_Window extends JFrame{

Board gameboard;


	public Board getBoard() {return gameboard;}
	//Constructor
    public Reversi_Window(String title)
    {
    	super(title);
    	
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);
		
		gameboard = new Board();
		gameboard.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
    	layout.setConstraints(gameboard, constraints);
    	add(gameboard);
    	
    }
    
       
}

//Main Board Component
class Board extends JPanel{
	
	JButton[][] panels;
	
	public JButton[][] getPanels() { return panels; }
	
	public Board(){
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);
		
		panels = new JButton[8][8];
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(2,2,2,2);
		
		for (int i = 0 ; i < 8 ; i++)
		{
			for (int j = 0 ; j < 8 ; j++)
			{
				panels[i][j] = new JButton();
				panels[i][j].setBackground(new Color(0.0f, 0.5f, 0.0f, 1.0f));
				panels[i][j].setPreferredSize(new Dimension(40,40));
				panels[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 5));
				constraints.gridx = i;
				constraints.gridy = j;	
				layout.setConstraints(panels[i][j], constraints);
				add(panels[i][j])	;
			}			
		}	
		
	}
	
	
}