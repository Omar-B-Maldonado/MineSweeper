import javax.swing.JOptionPane;

public class Main 
{
	public static GUI game;
	public static void main(String[] args) 
	{
		//new GUI();
		
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
		
		if (answer == 0) game = new GUI(16, 30, 99); //hard
		if (answer == 1) game = new GUI(16, 16, 40); //medium
		if (answer == 2) game = new GUI( 9,  9, 10); //easy	
	}
}
