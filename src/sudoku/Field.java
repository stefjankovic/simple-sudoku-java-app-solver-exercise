package sudoku;

public class Field {
	
	private int rowCoordinate;
	private int columnCoordinate;
	private boolean isSolved;
	private int[] possibleValues;
	
	public Field(int rowCoordinate, int columnCoordinate, Board board) {
		this.rowCoordinate = rowCoordinate;
		this.columnCoordinate = columnCoordinate;
		this.isSolved = board.isSolved(rowCoordinate, columnCoordinate);
		this.possibleValues = getArrayOfPossibleValues(rowCoordinate, columnCoordinate, board);
	}

	public boolean isSolved() {
		return this.isSolved;
	}

	public int[] getPossibleValues() {
		return this.possibleValues;
	}
	
	public int getRow() {
		return rowCoordinate;
	}
	
	public int getColumn() {
		return columnCoordinate;
	}
	
	public boolean isPossible(int value) {
		for(int i = 0; i < possibleValues.length; i++) {
			if(possibleValues[i] == value) return true;
		}
		return false;
	}

	private int[] getArrayOfPossibleValues(int rowCoordinate, int columnCoordinate, Board board) {
		int arrayPosition = 0;
		int[] possible = new int[board.getPossibilitiesCount(rowCoordinate, columnCoordinate)];
		for (int value = 0; value < Board.DIMENSION; value++) {
			if (board.isPossible(rowCoordinate, columnCoordinate, value)) {
				possible[arrayPosition] = value + 1;
				arrayPosition++;
			}
		}
		return possible;
	}
}
