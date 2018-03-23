package sudoku;

import java.util.ArrayList;
import java.util.List;

public final class LockedCandidates {
	
	private static enum Line {
		ROW(),
		COLUMN();
	}

	public static List<Move> lockedCandidatesOne(Board board) {
		FieldCollection fieldCollection = new FieldCollection(board);
		for (int i = 0; i < Board.DIMENSION; i++) {
			List<Move> moves = solveLCOne(fieldCollection.getOneBoxFields(i), fieldCollection);

			if(moves != null) return moves;
		}
		return null;
	}

	public static List<Move> lockedCandidatesTwo(Board board) {
		FieldCollection fieldCollection = new FieldCollection(board);
		for (int i = 0; i < Board.DIMENSION; i++) {
			List<Move> moves;

			if((moves = solveLCTwo(fieldCollection.getColumnFields(i), fieldCollection, i)) != null) 
				return moves;
			if((moves = solveLCTwo(fieldCollection.getRowFields(i), fieldCollection, i)) != null) 
				return moves;
		}
		return null;
	}
	
	private static List<Move> solveLCOne(List<Field> fields, FieldCollection board) {
		for (int possibleValue = 1; possibleValue <= Board.DIMENSION; possibleValue++) {
			Line rowOrColumn = null;
			List<Field> lockedCandidates = findLCOne(fields, possibleValue, rowOrColumn);
			if (areFieldsInTheSameRow(lockedCandidates) != -1)    rowOrColumn = Line.ROW;
			if (areFieldsInTheSameColumn(lockedCandidates) != -1) rowOrColumn = Line.COLUMN;
			
			List<Field> otherFields = findOtherFieldsWithValue(lockedCandidates, rowOrColumn, board, possibleValue);
			if (!otherFields.isEmpty()) {
				return lCConclusion(lockedCandidates, otherFields, possibleValue);
			}
		}
		return null;
	}
	
	private static List<Field> findLCOne(List<Field> fields, int value, Line rowOrColumn) {
		List<Field> fieldsWhereValueIsPossible = Util.returnFieldsWhereValueIsPossible(fields, value);
		if (fieldsWhereValueIsPossible.size() >= 2 && fieldsWhereValueIsPossible.size() <= Board.SIZE) {
			if ((areFieldsInTheSameRow(fieldsWhereValueIsPossible)) != -1) return fieldsWhereValueIsPossible;
			if ((areFieldsInTheSameColumn(fieldsWhereValueIsPossible)) != -1) return fieldsWhereValueIsPossible;
		}
		return null;
	}
	
	private static List<Field> findOtherFieldsWithValue(List<Field> lockedCandidates, Line rowOrColumn, FieldCollection board, int value) {
		List<Field> otherFieldsInLine = new ArrayList<>();
		if (lockedCandidates != null ) {
			if (rowOrColumn == Line.COLUMN) {
				otherFieldsInLine = board.getColumnFields(lockedCandidates.get(0).getColumn());
			} else if (rowOrColumn == Line.ROW){
				otherFieldsInLine = board.getRowFields(lockedCandidates.get(0).getRow());
			}
			otherFieldsInLine.removeAll(lockedCandidates);
			List<Field> fieldsToRemove = new ArrayList<>();
			for (Field field : otherFieldsInLine) {
				if (!field.isPossible(value)) fieldsToRemove.add(field);
			}
			otherFieldsInLine.removeAll(fieldsToRemove);
		}
		return otherFieldsInLine;
	}
	
	private static List<Move> lCConclusion(List<Field> lockedCandidates, List<Field> fieldsToRemoveValue, int value) {
		List<Move> moves = new ArrayList<>();
		for (Field field : lockedCandidates) {
			moves.add(markConclusion(field.getRow(), field.getColumn(), value));
		}
		for (Field field : fieldsToRemoveValue) {
			moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.CLUE, "Clue"));
			moves.add(new Move(field.getRow(), field.getColumn(), value - 1, MoveOperation.DISABLE, 
					"Disabled: " + value + " in (" + (field.getRow() + 1) + ", " + (field.getColumn() + 1) + ")"));
		}
		return moves;
	}
	
	private static Move markConclusion(int rowCoordinate, int columnCoordinate, int value) {
		return new Move(rowCoordinate, columnCoordinate, value, MoveOperation.CONCLUSION, "LockedCandidates: " + value);
	}
	
	private static int areFieldsInTheSameRow(List<Field> fields) {
		if(fields != null ) {
			for (Field field : fields) {
				if (fields.get(0).getRow() != field.getRow()) return -1;
			}
			return fields.get(0).getRow();
		}
		return -1;
	}
	
	private static int areFieldsInTheSameColumn(List<Field> fields) {
		if(fields != null ) {
			for (Field field : fields) {
				if (fields.get(0).getColumn() != field.getColumn()) return -1;
			}
			return fields.get(0).getColumn();
		}
		return -1;
	}
	
	private static List<Move> solveLCTwo(List<Field> fields, FieldCollection board, int rowOrColumnNumber) {
		for(int value = 1; value <= Board.DIMENSION; value++) {
			List<Field> lockedCandidates = findLCTwo(fields, value);
			List<Field> boxContainingCandidates = fieldsInTheSameBox(lockedCandidates, board);
			if (boxContainingCandidates != null) {
				boxContainingCandidates.removeAll(lockedCandidates);
				List<Field> fieldsWhereToRemoveValue = Util.returnFieldsWhereValueIsPossible(boxContainingCandidates, value);
				if (!fieldsWhereToRemoveValue.isEmpty()) return lCConclusion(lockedCandidates, fieldsWhereToRemoveValue, value);
			}
			
		}
		return null;
	}
	
	private static List<Field> findLCTwo(List<Field> fields, int value) {
		List<Field> fieldsWhereValueIsPossible = Util.returnFieldsWhereValueIsPossible(fields, value);
		return (fieldsWhereValueIsPossible.size() >= 2 && fieldsWhereValueIsPossible.size() <= Board.SIZE) ? fieldsWhereValueIsPossible : null;
	}
	
	private static List<Field> fieldsInTheSameBox(List<Field> fields, FieldCollection board) {
		if(fields != null ) {
			for (int i = 0; i < Board.DIMENSION; i++) {
				if(board.getOneBoxFields(i).containsAll(fields)) return board.getOneBoxFields(i);
			}
		}
		return null;
	}
}