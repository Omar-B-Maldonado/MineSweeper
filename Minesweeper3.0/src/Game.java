import javax.swing.*;

public class Game extends JFrame
{	
	public static JFrame frame;
	
	public Game(int numRows, int numCols, int numBombs)
	{
		//the Game is a frame with the board panel added to it
		//soon, the GUI will also show the bomb and flag counts
		
		add(new BoardPanel(numRows, numCols, numBombs)); 
		
		pack();
		setTitle("MineSweeper3.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		String[] options = {"Hard", "Medium", "Easy"};
		
		int answer = JOptionPane.showOptionDialog
		(
				null,
				"Select Your Difficulty",
				"MineSweeper 3.0",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,//image icon
				options,
				null
		);
		     if (answer == 0) frame = new Game(16, 30, 99); //hard
		else if (answer == 1) frame = new Game(16, 16, 40); //medium
		else if (answer == 2) frame = new Game( 9,  9, 10); //easy
	}
}
