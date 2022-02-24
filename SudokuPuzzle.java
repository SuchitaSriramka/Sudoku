package sudoku;

public class SudokuPuzzle {
	/*
	public static void main(String args[]) {
		String[] s = {"1","2","3","4","5","6","7","8","9"};
		SudokuPuzzle sp = new SudokuPuzzle(9,9,3,3,s);
		System.out.println(sp);
	}*/
	/*
	public static void main(String args[]) {
		SudokuPuzzleType sp = SudokuPuzzleType.NINEBYNINE;
		SudokuPuzzleType sp1 = SudokuPuzzleType.SIXBYSIX;
		SudokuPuzzleType []s = SudokuPuzzleType.values();
		for(SudokuPuzzleType p: s) {
			System.out.print(p.name()+" "+p);
		}
		System.out.println();
		System.out.println(sp);
		System.out.println(sp1);
	}*/
	
	
	public static void main(String[] args) {
		SudokuPuzzleType sp = SudokuPuzzleType.NINEBYNINE;
		SudokuPuzzle s;
		SudokuGenerator sg = new SudokuGenerator();
		s = sg.generateRandomSudoku(sp);
		System.out.println(s);
	}
	
	
	protected String [][] board;
	//To determine if a slot is mutable
	protected boolean [][] mutable;
	private final int ROWS;
	private final int COLUMNS;
	private final int BOXWIDTH;
	private final int BOXHEIGHT;
	private final String [] VALIDVALUES;
	
	public SudokuPuzzle(int rows, int columns, int boxWidth, int boxHeight, String [] validValues) {
		this.ROWS = rows;
		this.COLUMNS = columns;
		this.BOXWIDTH = boxWidth;
		this.BOXHEIGHT = boxHeight;
		this.VALIDVALUES = validValues;
		this.board = new String[ROWS][COLUMNS];
		this.mutable = new boolean[ROWS][COLUMNS];
		initializeBoard();
		initializeMutableSlots();
	}
	
	public SudokuPuzzle(SudokuPuzzle puzzle) {
		this.ROWS = puzzle.ROWS;
		this.COLUMNS = puzzle.COLUMNS;
		this.BOXWIDTH = puzzle.BOXWIDTH;
		this.BOXHEIGHT = puzzle.BOXHEIGHT;
		this.VALIDVALUES = puzzle.VALIDVALUES;
		this.board = new String[ROWS][COLUMNS];
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				board[r][c] = puzzle.board[r][c];
			}
		}
		this.mutable = new boolean[ROWS][COLUMNS];
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				this.mutable[r][c] = puzzle.mutable[r][c];
			}
		}
	}
	
	public int getNumRows() {
		return this.ROWS;
	}
	
	public int getNumColumns() {
		return this.COLUMNS;
	}
	
	public int getBoxWidth() {
		return this.BOXWIDTH;
	}
	
	public int getBoxHeight() {
		return this.BOXHEIGHT;
	}
	
	public String [] getValidValues() {
		return this.VALIDVALUES;
	}
	
	public void makeMove(int row, int col, String value, boolean isMutable) {
		if(this.isValidValue(value) && this.isValidMove(row, col, value) && this.isSlotMutable(row, col)) {
			this.board[row][col] = value;
			this.mutable[row][col] = isMutable;
		}
	}
	
	public boolean isValidValue(String value) {
		for(String str : this.VALIDVALUES) {
			if(str.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidMove(int row, int col, String value) {
		if(this.inRange(row, col)) {
			if(!this.numInCol(col, value) && !this.numInRow(row, value) && !this.numInBox(row, col, value)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inRange(int row, int col) {
		return row <= this.ROWS && col <= this.COLUMNS && row >= 0 && col >= 0; 
	}
	
	public boolean numInCol(int col, String value) {
		if(col <= this.COLUMNS) {
			for(int r = 0;r < this.ROWS;r++) {
				if(this.board[r][col].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean numInRow(int row, String value) {
		if(row <= this.ROWS) {
			for(int c = 0;c < this.COLUMNS;c++) {
				if(this.board[row][c].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean numInBox(int row, int col, String value) {
		if(this.inRange(row, col)) {
			int boxRow = row / this.BOXHEIGHT;
			int boxCol = col / this.BOXWIDTH;
			
			int startingRow = boxRow * this.BOXHEIGHT;
			int startingCol = boxCol * this.BOXWIDTH;
			
			for(int r = startingRow;r <= (startingRow+this.BOXHEIGHT)-1;r++) {
				for(int c = startingCol;c <= (startingCol+this.BOXWIDTH)-1; c++) {
					if(this.board[r][c].equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isSlotMutable(int row, int col) {
		return this.mutable[row][col];
	}
	
	public boolean isSlotAvailable(int row, int col) {
		return (this.inRange(row, col) && this.board[row][col].equals("") && this.isSlotMutable(row, col));
	}
	
	public String getValue(int row, int col) {
		if(this.inRange(row, col)) {
			return this.board[row][col];
		}
		return "";
	}
	
	public String [][] getBoard(){
		return this.board;
	}
	
	public boolean boardFull() {
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				if(this.board[r][c].equals("")) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void makeSlotEmpty(int row, int col) {
		this.board[row][col] = "";
	}
	
	@Override
	public String toString() {
		String str = "Game Board:\n";
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				str += this.board[r][c] + " ";
			}
			str += "\n";
		}
		return str+"\n";
	}
	
	private void initializeBoard() {
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				this.board[r][c] = "";
			}
		}
	}
	
	private void initializeMutableSlots() {
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				this.mutable[r][c] = true;
			}
		}
	}

	/*
	protected String [][] board;
	// Table to determine if a slot is mutable
	protected boolean [][] mutable;
	private final int ROWS;
	private final int COLUMNS;
	private final int BOXWIDTH;
	private final int BOXHEIGHT;
	private final String [] VALIDVALUES;
	
	public SudokuPuzzle(int rows,int columns,int boxWidth,int boxHeight,String [] validValues) {
		this.ROWS = rows;
		this.COLUMNS = columns;
		this.BOXWIDTH = boxWidth;
		this.BOXHEIGHT = boxHeight;
		this.VALIDVALUES = validValues;
		this.board = new String[ROWS][COLUMNS];
		this.mutable = new boolean[ROWS][COLUMNS];
		initializeBoard();
		initializeMutableSlots();
	}
	
	public SudokuPuzzle(SudokuPuzzle puzzle) {
		this.ROWS = puzzle.ROWS;
		this.COLUMNS = puzzle.COLUMNS;
		this.BOXWIDTH = puzzle.BOXWIDTH;
		this.BOXHEIGHT = puzzle.BOXHEIGHT;
		this.VALIDVALUES = puzzle.VALIDVALUES;
		this.board = new String[ROWS][COLUMNS];
		for(int r = 0;r < ROWS;r++) {
			for(int c = 0;c < COLUMNS;c++) {
				board[r][c] = puzzle.board[r][c];
			}
		}
		this.mutable = new boolean[ROWS][COLUMNS];
		for(int r = 0;r < ROWS;r++) {
			for(int c = 0;c < COLUMNS;c++) {
				this.mutable[r][c] = puzzle.mutable[r][c];
			}
		}
	}
	
	public int getNumRows() {
		return this.ROWS;
	}
	
	public int getNumColumns() {
		return this.COLUMNS;
	}
	
	public int getBoxWidth() {
		return this.BOXWIDTH;
	}
	
	public int getBoxHeight() {
		return this.BOXHEIGHT;
	}
	
	public String [] getValidValues() {
		return this.VALIDVALUES;
	}
	
	public void makeMove(int row,int col,String value,boolean isMutable) {
		if(this.isValidValue(value) && this.isValidMove(row,col,value) && this.isSlotMutable(row, col)) {
			this.board[row][col] = value;
			this.mutable[row][col] = isMutable;
		}
	}
	
	public boolean isValidMove(int row,int col,String value) {
		if(this.inRange(row,col)) {
			if(!this.numInCol(col,value) && !this.numInRow(row,value) && !this.numInBox(row,col,value)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean numInCol(int col,String value) {
		if(col <= this.COLUMNS) {
			for(int row=0;row < this.ROWS;row++) {
				if(this.board[row][col].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean numInRow(int row,String value) {
		if(row <= this.ROWS) {
			for(int col=0;col < this.COLUMNS;col++) {
				if(this.board[row][col].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean numInBox(int row,int col,String value) {
		if(this.inRange(row, col)) {
			int boxRow = row / this.BOXHEIGHT;
			int boxCol = col / this.BOXWIDTH;
			
			int startingRow = (boxRow*this.BOXHEIGHT);
			int startingCol = (boxCol*this.BOXWIDTH);
			
			for(int r = startingRow;r <= (startingRow+this.BOXHEIGHT)-1;r++) {
				for(int c = startingCol;c <= (startingCol+this.BOXWIDTH)-1;c++) {
					if(this.board[r][c].equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isSlotAvailable(int row,int col) {
		 return (this.inRange(row,col) && this.board[row][col].equals("") && this.isSlotMutable(row, col));
	}
	
	public boolean isSlotMutable(int row,int col) {
		return this.mutable[row][col];
	}
	
	public String getValue(int row,int col) {
		if(this.inRange(row,col)) {
			return this.board[row][col];
		}
		return "";
	}
	
	public String [][] getBoard() {
		return this.board;
	}
	
	private boolean isValidValue(String value) {
		for(String str : this.VALIDVALUES) {
			if(str.equals(value)) return true;
		}
		return false;
	}
	
	public boolean inRange(int row,int col) {
		return row <= this.ROWS && col <= this.COLUMNS && row >= 0 && col >= 0;
	}
	
	public boolean boardFull() {
		for(int r = 0;r < this.ROWS;r++) {
			for(int c = 0;c < this.COLUMNS;c++) {
				if(this.board[r][c].equals("")) return false;
			}
		}
		return true;
	}
	
	public void makeSlotEmpty(int row,int col) {
		this.board[row][col] = "";
	}
	
	@Override
	public String toString() {
		String str = "Game Board:\n";
		for(int row=0;row < this.ROWS;row++) {
			for(int col=0;col < this.COLUMNS;col++) {
				str += this.board[row][col] + " ";
			}
			str += "\n";
		}
		return str+"\n";
	}
	
	private void initializeBoard() {
		for(int row = 0;row < this.ROWS;row++) {
			for(int col = 0;col < this.COLUMNS;col++) {
				this.board[row][col] = "";
			}
		}
	}
	
	private void initializeMutableSlots() {
		for(int row = 0;row < this.ROWS;row++) {
			for(int col = 0;col < this.COLUMNS;col++) {
				this.mutable[row][col] = true;
			}
		}
	}
	*/
}
