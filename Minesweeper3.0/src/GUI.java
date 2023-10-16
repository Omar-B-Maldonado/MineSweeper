import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUI extends JFrame
{
	JPanel    panel;
	GameBoard board;
	
	public GUI()
	{
		panel = new JPanel();	
		board = new GameBoard();
		
		panel.setLayout(new BorderLayout());
		panel.add(board, BorderLayout.CENTER);
		
		add(panel);
		pack();
		setTitle("MineSweeper3.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
	}
	
	private class GameBoard extends    JPanel 
							implements ActionListener 
	{
		private JButton[][] board;
		private Grid grid;
		
		int numRows;
		int numCols;
		int numBombs;
		
		String revealed   = "<html></html>"; //whitespace does not get replaced by elipses

		String unrevealed = "";
		
		private final int IMAGE_SIZE = 25;
		
		private final String GAME_OVER_TITLE = "Oopsie!";
		private final String GAME_OVER_MESSAGE = "You made an oopsie. It's okay though.";
		
		private final String WIN_TITLE   = "WOWZERS";
		private final String WIN_MESSAGE = "You won!";
		
		private final String PLAY_AGAIN_TITLE = "Again, AGAIN!";
		private final String PLAY_AGAIN_MESSAGE = "Play again?";
		
		private int revealedCount;
		
		private boolean newGame = false;

		ImageIcon unpressed  = new ImageIcon("Images/button_new.png" );
		ImageIcon bomb       = new ImageIcon("Images/button_bomb.png");
		ImageIcon zero       = new ImageIcon("Images/button_0.png"   );
		ImageIcon one        = new ImageIcon("Images/button_1.png"   );
		ImageIcon two        = new ImageIcon("Images/button_2.png"   );
		ImageIcon three      = new ImageIcon("Images/button_3.png"   );
		ImageIcon four       = new ImageIcon("Images/button_4.png"   );
		ImageIcon five       = new ImageIcon("Images/button_5.png"   );
		ImageIcon six        = new ImageIcon("Images/button_6.png"   );
		ImageIcon seven      = new ImageIcon("Images/button_7.png"   );
		ImageIcon eight      = new ImageIcon("Images/button_8.png"   );
		
		ImageIcon unpressedScaled  = new ImageIcon(unpressed.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon bombScaled       = new ImageIcon(bomb.getImage().getScaledInstance     (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon zeroScaled       = new ImageIcon(zero.getImage().getScaledInstance     (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon oneScaled        = new ImageIcon(one.getImage().getScaledInstance      (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon twoScaled        = new ImageIcon(two.getImage().getScaledInstance      (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon threeScaled      = new ImageIcon(three.getImage().getScaledInstance    (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon fourScaled       = new ImageIcon(four.getImage().getScaledInstance     (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon fiveScaled       = new ImageIcon(five.getImage().getScaledInstance     (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon sixScaled        = new ImageIcon(six.getImage().getScaledInstance      (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon sevenScaled      = new ImageIcon(seven.getImage().getScaledInstance    (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon eightScaled      = new ImageIcon(eight.getImage().getScaledInstance    (IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		
		
		public GameBoard()
		{
			initialize();
			
			setLayout(new GridLayout(numRows, numCols));
			
			displayBoard();	
		}
		
		public void initialize()
		{
			Grid grid = new Grid(16, 16, 40);
			this.grid = grid;					  //make a new grid for the buttons
			this.numRows = grid.getNumRows();
			this.numCols = grid.getNumColumns();
			this.numBombs = grid.getNumBombs();
			revealedCount = 0;
			newGame = true;
		}
		
		public void displayBoard()
		{
			board = new JButton[numRows][numCols];
			
			for(int row = 0; row < board.length; row++)
			{
				for(int col = 0; col < board[row].length; col ++)
				{			
					board[row][col] = new JButton();
					
					setNewButtonImage(board[row][col]);
					markAsUnrevealed(board[row][col]);
	
					int i = row; //capture row as i
					int j = col; //capture col as j
					
					//attach the common ActionListener to each button
		            board[row][col].addActionListener(this::actionPerformed);
		            board[row][col].putClientProperty("row", i); //store i as client property "row"
		            board[row][col].putClientProperty("col", j); //store j as client property "col"
					
		            board[row][col].setBorder(BorderFactory.createEmptyBorder());//gets rid of button padding
					board[row][col].setFocusPainted(false);
		             
					this.add(board[row][col]);					 //add JButton to the GameBoard
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			int row   = (int) ((JButton) e.getSource()).getClientProperty("row");	
			int col   = (int) ((JButton) e.getSource()).getClientProperty("col");
			
			newGame = false;
			
			handleButtonClick(row, col);
			
			repaint();
		}
		
		private void handleButtonClick(int row, int col) 
	    {        
	    	if (grid.isBombAtLocation(row, col) == true) 						//if a bomb button was clicked...
	        {
	    		revealGrid();													//reveal the rest of the grid            
	    		displayGameOverPane();           
	    		promptReplay();
	        } 
	       
	        else 																//if the button clicked was not a bomb...
	        {
	            int count  =    grid.getCountAtLocation(row, col);					//get the count of bombs nearby based on the gri     	
	           
	            if (count == 0) revealButtonsAdjacentTo(row, col);				//recursively reveal buttons if 0 bombs are nearby
	            
	            if (count >  0) revealImageBasedOnCount(count, board[row][col]);
	            	
	            if (winConditionMet() == true) 
            	{
            		displayWinPane();
   		         	promptReplay();
            	}	
	        }
	    }
		
	    private void revealGrid() 
	    {
	    																				//go through entire grid
	        for (int row = 0; row < board.length; row++) 
	        {
	            for (int col = 0; col < board[row].length; col++) 
	            {     	            	 	
	            	int count = grid.getCountAtLocation(row, col); 					   //get the grid button's adjacent bomb count
	            	
	            	if (grid.isBombAtLocation(row, col))	board[row][col].setIcon(bombScaled);//set its icon to bomb icon if its a bomb
			        
	            	else revealImageBasedOnCount(count, board[row][col]); //we don't check for win condition in this case
	            	
	            }//inner for loop ends
	        }//outer for loop ends
	    }

		private void revealButtonsAdjacentTo(int row, int col)
		{
				if ((row < 0 || row >= board.length) || 
					(col < 0 || col >= board[0].length)) 				//if out of bounds..
				{
					return;												//come back from recursion
			    }
	
				if ((isUnrevealed(board[row][col])) && (newGame == false))						//if current button's text was not revealed
				{
					int count = grid.getCountAtLocation(row, col);		//get the count
					
					if (count > 0) 
					{
						revealImageBasedOnCount(count, board[row][col]);
						
						if (winConditionMet()) 
		            	{
		            		displayWinPane();
			            	
		   		         	promptReplay();
		            	}
					}
					
					if (count == 0) 
					{	
						revealImageBasedOnCount(count, board[row][col]);
						
						if (winConditionMet()) 
		            	{
		            		displayWinPane();
			            	
		   		         	promptReplay();
		            	}
						
						else 
						{
							//recursively reveal adjacent cells
					        revealButtonsAdjacentTo(row - 1, col - 1);  //top left
					        revealButtonsAdjacentTo(row - 1, col);		//top
					        revealButtonsAdjacentTo(row - 1, col + 1);  //top right
					           
					        revealButtonsAdjacentTo(row, col - 1);		//left
					        revealButtonsAdjacentTo(row, col + 1);		//right
					            
					        revealButtonsAdjacentTo(row + 1, col - 1);  //bottom left
					        revealButtonsAdjacentTo(row + 1, col);		//bottom
					        revealButtonsAdjacentTo(row + 1, col + 1);	//bottom right
						}
					}
				}
		}		
		
		public void resetGame() 
		{ 
			initialize();
			
			for(int row = 0; row < numRows; row++)
			{
				for(int col = 0; col < numCols; col++)
				{ 		
					setNewButtonImage(board[row][col]);
					markAsUnrevealed(board[row][col]);
				}
			}
			repaint();
		}
		
		private boolean winConditionMet()
		{
			return (revealedCount == ((numRows * numCols) - numBombs));
		}
		
		private void displayGameOverPane()
		{
			JOptionPane.showMessageDialog(null, GAME_OVER_MESSAGE, GAME_OVER_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
		
		private void displayWinPane()
		{
			JOptionPane.showMessageDialog(null, WIN_MESSAGE, WIN_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
		
		private void promptReplay() 
		{
			int decision = JOptionPane.showConfirmDialog(null, PLAY_AGAIN_MESSAGE, PLAY_AGAIN_TITLE, JOptionPane.YES_NO_OPTION);
			
			if (decision == JOptionPane.YES_OPTION) resetGame();
			
			else System.exit(EXIT_ON_CLOSE);
		}
		
		private void revealImageBasedOnCount(int count, JButton button)
		{
			if (count == 0) button.setIcon(zeroScaled  );
			if (count == 1) button.setIcon(oneScaled   );
        	if (count == 2) button.setIcon(twoScaled   );
        	if (count == 3) button.setIcon(threeScaled );
        	if (count == 4) button.setIcon(fourScaled  );
        	if (count == 5) button.setIcon(fiveScaled  );
        	if (count == 6) button.setIcon(sixScaled   );
        	if (count == 7) button.setIcon(sevenScaled );
        	if (count == 8) button.setIcon(eightScaled );
        	
        	markAsRevealed(button);
        	revealedCount++;
		}
		
		private void setNewButtonImage(JButton button)	{       button.setIcon(unpressedScaled);    }
		
		private void markAsRevealed   (JButton button) 	{       button.setText(revealed);		    }
		
		private void markAsUnrevealed (JButton button)	{		button.setText(unrevealed);			}
		
		private boolean  isUnrevealed (JButton button)	{return button.getText().equals(unrevealed);}		
		
	}
}
