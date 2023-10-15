import java.util.Random;

public class Grid 
{
	private boolean[][]  bombGrid;
	private int[][] 	countGrid;
	
	private int    numRows;
	private int numColumns;
	private int   numBombs;
	
	//-------------------------------------------------
	
	public Grid()
	{
		numRows    = 10;
		numColumns = 10;
		numBombs   = 25;
		
		createBombGrid();
		createCountGrid();
	}
	
	public Grid(int rows, int columns)
	{
		numRows    =    rows;
		numColumns = columns;
		numBombs   =      25;
		
		createBombGrid();
		createCountGrid();
	}
	
	public Grid(int rows, int columns, int numBombs)
	{
		numRows       =     rows;
		numColumns    =  columns;
		this.numBombs = numBombs;
		
		createBombGrid();
		createCountGrid();
	}
	//--------------------------------------------------
	
	public int getNumRows()
	{
		return numRows;
	}
	
	public int getNumColumns()
	{
		return numColumns;
	}
	
	public int getNumBombs()
	{
		return numBombs;
	}
	
	public boolean[][] getBombGrid()
	{
		boolean copy[][] = new boolean[numRows][numColumns]; //new 2D array with same # of rows and columns
		
		for(int i = 0; i < bombGrid.length; i++)
		{
			for(int j = 0; j < bombGrid[i].length; j++)
			{
				copy[i][j] = bombGrid[i][j]; 				//copies all bombGrid cells to the copy cells
			}
		}
		return copy;
	}
	
	public int[][] getCountGrid()
	{
		int copy[][] = new int[numRows][numColumns]; //new 2D array with same # of rows and columns
		
		for(int i = 0; i < countGrid.length; i++)
		{
			for(int j = 0; j < countGrid[i].length; j++)
			{
				copy[i][j] = countGrid[i][j]; 		//copies all countGrid cells to the copy cells
			}
		}
		return copy;
	}
	
	public boolean isBombAtLocation(int row, int column)
	{
		if((row    >= 0 && row    <= numRows) && 
		   (column >= 0 && column <= numColumns))
		  {
			return bombGrid[row][column];
		  }
		else return false; //returns false if row and/or column is out of bounds
	}
	
	public int getCountAtLocation(int row, int column)
	{
		if(row < 0    || row > numRows) 	  return -1; //out of bounds
		if(column < 0 || column > numColumns) return -1; //out of bounds
		
		return(countGrid[row][column]);
	}
	//---------------------------------------------------
	
	private void createBombGrid()
	{
		bombGrid = new boolean[numRows][numColumns];
		
		Random random = new Random();
		
		int bombsPlaced = 0;
		
		while (bombsPlaced < numBombs) //continues until numBombs bombs are placed
		{
			int row    = random.nextInt(numRows); //generate random row number (not inclusive on the upper bound)
	        int column = random.nextInt(numColumns); // generate random column number

	        if (bombGrid[row][column] != true) //if a bomb is not already placed there
	        {
	            bombGrid[row][column]  = true; //place a bomb
	            
	            bombsPlaced++; //increment counter for bombs placed
	        }
		}
	}
	
	private void createCountGrid()
	{
		countGrid = new int[numRows][numColumns];
		
		for(int i = 0; i < numRows; i++)
		{
			for(int j = 0; j < numColumns; j++)
			{
				int mineCount = 0;	//a variable to count the mines around/at this specific cell
				
				if(bombGrid[i][j] == true) mineCount++; //checks current sell and increments minecount
				
				if((i - 1 >= 0)         && (bombGrid[i - 1][j] == true)) mineCount++; //checks cell above and increments minecount
				if((i + 1 < numRows)    && (bombGrid[i + 1][j] == true)) mineCount++; //checks cell below...
				
				if((j + 1 < numColumns) && (bombGrid[i][j + 1] == true)) mineCount++; //checks cell to right...
				if((j - 1 >= 0)         && (bombGrid[i][j - 1] == true)) mineCount++; //checks cell to left..
				
				if((i - 1 >= 0)      && (j + 1 < numColumns) && (bombGrid[i - 1][j + 1] == true)) mineCount++; //checks top    right
				if((i + 1 < numRows) && (j + 1 < numColumns) && (bombGrid[i + 1][j + 1] == true)) mineCount++; //checks bottom right
				
				if((i - 1 >= 0)      && (j - 1 >= 0)    && (bombGrid[i - 1][j - 1] == true)) mineCount++; //checks top    left
				if((i + 1 < numRows) && (j - 1 >= 0)    && (bombGrid[i + 1][j - 1] == true)) mineCount++; //checks bottom left
				
				countGrid[i][j] = mineCount; //makes the current cell's number reflect the amount of mines surrounding it (including itself)
			}
		}
	}
}
