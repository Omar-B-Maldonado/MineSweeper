import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BoardPanel extends    JPanel 
					    implements ActionListener, MouseListener
	{
		private JButton[][] board;
		private Grid 		 grid;
		
		int numRows;
		int numCols;
		int numBombs;
		
		private final String GAME_OVER_TITLE    = "Oopsie!";
		private final String GAME_OVER_MESSAGE  = "You made an oopsie. It's okay though.";
		
		private final String WIN_TITLE          = "WOWZERS";
		private final String WIN_MESSAGE        = "You won!";
		
		private final String PLAY_AGAIN_TITLE   = "Again, AGAIN!";
		private final String PLAY_AGAIN_MESSAGE = "Play again?";	
		
		private int            revealedCount;
		private boolean      gameJustStarted;
		
		private final int    IMAGE_SIZE = 28;

		ImageIcon unpressed = new ImageIcon(getClass().getClassLoader().getResource("button_new.png" ));
		ImageIcon bomb      = new ImageIcon(getClass().getClassLoader().getResource("button_bomb.png"));
		ImageIcon flag      = new ImageIcon(getClass().getClassLoader().getResource("flag.png"       ));
		ImageIcon _0        = new ImageIcon(getClass().getClassLoader().getResource("button_0.png"   ));
		ImageIcon _1        = new ImageIcon(getClass().getClassLoader().getResource("button_1.png"   ));
		ImageIcon _2        = new ImageIcon(getClass().getClassLoader().getResource("button_2.png"   ));
		ImageIcon _3       	= new ImageIcon(getClass().getClassLoader().getResource("button_3.png"   ));
		ImageIcon _4        = new ImageIcon(getClass().getClassLoader().getResource("button_4.png"   ));
		ImageIcon _5        = new ImageIcon(getClass().getClassLoader().getResource("button_5.png"   ));
		ImageIcon _6        = new ImageIcon(getClass().getClassLoader().getResource("button_6.png"   ));
		ImageIcon _7        = new ImageIcon(getClass().getClassLoader().getResource("button_7.png"   ));
		ImageIcon _8        = new ImageIcon(getClass().getClassLoader().getResource("button_8.png"   ));
		
		ImageIcon unpressedScaled = new ImageIcon(unpressed.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon bombScaled      = new ImageIcon(     bomb.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon flagScaled      = new ImageIcon(     flag.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _0Scaled        = new ImageIcon(       _0.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _1Scaled        = new ImageIcon(       _1.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _2Scaled        = new ImageIcon(       _2.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _3Scaled        = new ImageIcon(       _3.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _4Scaled        = new ImageIcon(       _4.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _5Scaled        = new ImageIcon(       _5.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _6Scaled        = new ImageIcon(       _6.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _7Scaled        = new ImageIcon(       _7.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		ImageIcon _8Scaled        = new ImageIcon(       _8.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST));
		
		//------------------------------ INNER CLASS' METHODS ARE BELOW THIS LINE ---------------------------------------------------//
		
		public BoardPanel(int numRows, int numCols, int numBombs)
		{
			initialize(numRows, numCols, numBombs);	
			setLayout(new GridLayout(numRows, numCols));	
			createBoard();	
		}
		
		public void initialize(int numRows, int numCols, int numBombs)
		{
			grid            = new Grid(numRows, numCols, numBombs);
			this.numRows    = grid.getNumRows   ();
			this.numCols    = grid.getNumColumns();
			this.numBombs   = grid.getNumBombs  ();
			revealedCount   = 0;
			gameJustStarted = true;
		}
		
		public void createBoard()
		{
			board = new JButton[numRows][numCols];
			
			for(int row = 0; row < board.length; row++)
			{
				for(int col = 0; col < board[row].length; col ++)
				{			
					JButton button = new JButton(); //make a new JButton
					
					board[row][col] = button;	    //make the spot on the board refer to that button
					
					setNewButtonImage(button);
					
					button.putClientProperty("row", row); //store the button's row as client property "row"
		            button.putClientProperty("col", col); //store the button's col as client property "col"
					
					button.addMouseListener (this);
					button.addActionListener(this);
		            
					button.setFocusPainted (false);
		            button.setBorder(BorderFactory.createEmptyBorder()); //gets rid of button padding
		             
					this.add(button);
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			int row = (int) ((JButton) e.getSource()).getClientProperty("row");	
			int col = (int) ((JButton) e.getSource()).getClientProperty("col");
			
			gameJustStarted = false; //the game is no longer in a brand new state once you click a button
			
			handleButtonClick(row, col);
			repaint();
		}
		
		private void handleButtonClick(int row, int col) 
	    {        
	    	if (bombClickedAt(row, col)) handleGameOver();
	       
	        else 																				//if the button clicked was not a bomb...
	        {
	            JButton clickedButton = board[row][col];
	            
	        	int count  =           	grid.getCountAtLocation(row, col);				//get the count of bombs nearby based on the gri     	
	           
	            if (count == 0)         revealButtonsAdjacentTo(row, col);				//recursively reveal buttons if 0 bombs are nearby
	            
	            if (count  > 0)         revealImageBasedOnCount(count, clickedButton);
	            	
	            if (allSafeSpotsRevealed()) handleWin();	
	        }
	    }
		
	    private void revealGrid() 
	    {
	    																			//go through entire grid
	        for (int row = 0; row < board.length; row++) 
	        {
	            for (int col = 0; col < board[row].length; col++) 
	            {     	            	 	
	            	JButton button = board[row][col];
	            	
	            	int count  = grid.getCountAtLocation(row, col); 				   //get the grid button's adjacent bomb count
	            	
	            	if          (grid.isBombAtLocation  (row, col)) button.setIcon(bombScaled);//set its icon to bomb icon if its a bomb
			        
	            	else        revealImageBasedOnCount (count,     button); //we don't check for win condition in this case
	            }
	        }
	    }

		private void revealButtonsAdjacentTo(int row, int col)
		{
				if (outOfBoundsAt(row, col)) {return;}
				
				JButton button = board[row][col];
	
				if ((isUnrevealed(button)) && (!gameJustStarted))
				{
					int count = grid.getCountAtLocation(row, col);		//get the count
					
					if (count > 0) 
					{
						revealImageBasedOnCount(count, button);
						if (allSafeSpotsRevealed()) handleWin();
					}
					
					if (count == 0) 
					{	
						revealImageBasedOnCount(count, button);
						if (allSafeSpotsRevealed()) handleWin();
						
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
			Game.frame.dispose();   //disposes the current game's frame
			Game.main(null);		//calls the game's main method to make a new game frame
		}
		
		private void handleGameOver()
		{
			revealGrid();           //reveal the rest of the grid            
    		displayGameOverPane();           
    		promptReplay();
		}
		
		private void handleWin()
		{
			revealGrid();
			displayWinPane();	
	        promptReplay();
		}
		
		private boolean outOfBoundsAt(int row, int col)
		{
			return (row < 0 || row >= board.length) || (col < 0 || col >= board[0].length);
		}
		
		private boolean allSafeSpotsRevealed()
		{
			return revealedCount == ((numRows * numCols) - numBombs);
		}
		
		private boolean bombClickedAt(int row, int col)
		{
			return grid.isBombAtLocation(row, col);
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
			int decision =  JOptionPane.showConfirmDialog(null, PLAY_AGAIN_MESSAGE, PLAY_AGAIN_TITLE, JOptionPane.YES_NO_OPTION);
			
			if (decision == JOptionPane.YES_OPTION) resetGame();
			
			else System.exit(0);
		}
		
		private void revealImageBasedOnCount(int count, JButton button)
		{
			if (count == 0) button.setIcon(_0Scaled);
			if (count == 1) button.setIcon(_1Scaled);
        	if (count == 2) button.setIcon(_2Scaled);
        	if (count == 3) button.setIcon(_3Scaled);
        	if (count == 4) button.setIcon(_4Scaled);
        	if (count == 5) button.setIcon(_5Scaled);
        	if (count == 6) button.setIcon(_6Scaled);
        	if (count == 7) button.setIcon(_7Scaled);
        	if (count == 8) button.setIcon(_8Scaled);
        	
        	revealedCount++;
		}
		
		private void setNewButtonImage(JButton button)	{       button.setIcon     (unpressedScaled);}	
		private boolean isUnrevealed  (JButton button)	{return button.getIcon() == unpressedScaled; }
		private boolean isFlagged     (JButton button)  {return button.getIcon() == flagScaled;      }
		@Override 
		public void mouseClicked 	  (MouseEvent   e) 
		{	
			JButton button = (JButton) e.getSource();
			
			if (SwingUtilities.isRightMouseButton(e)) 
			{
				if      (isFlagged   (button)) setNewButtonImage(button) ;	
				else if (isUnrevealed(button)) button.setIcon(flagScaled);
			}
		}
		
		//unused methods from MouseListener implementation
		public void mousePressed  (MouseEvent e) {}
		public void mouseReleased (MouseEvent e) {}
		public void mouseEntered  (MouseEvent e) {}
		public void mouseExited   (MouseEvent e) {}		
	}
	
