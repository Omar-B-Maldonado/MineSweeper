import javax.swing.*;

public class GUI extends JFrame
{	
	public GUI(int numRows, int numCols, int numBombs)
	{
		//the GUI is a frame with the board panel added to it
		//soon, the GUI will also show the bomb and flag counts
		
		add(new BoardPanel(numRows, numCols, numBombs)); 
		
		pack();
		setTitle("MineSweeper3.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
