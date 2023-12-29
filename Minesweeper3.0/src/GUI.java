import javax.swing.*;

public class GUI extends JFrame
{	
	public GUI()
	{
		//the GUI is a frame with the board panel added to it
		//soon, the GUI will also show the bomb and flag counts
		add(new BoardPanel()); 
		
		pack();
		setTitle("MineSweeper3.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
