package sudoku;

import java.util.ArrayList;
import java.util.List;

public class XWing {
	
	private static enum Line {
		ROW,
		COLUMN;
	}
	
	public static List<Move> solveXWing(Board board) {
		FieldCollection fieldCollection = new FieldCollection(board);
		for (int i = 0; i < Board.DIMENSION; i++) {
			List<Move> moves;

			if ((moves = checkForXWing(fieldCollection.getUnsolvedFields(fieldCollection.getRowFields(i)), fieldCollection, Line.ROW)) != null) return moves;
			if ((moves = checkForXWing(fieldCollection.getUnsolvedFields(fieldCollection.getColumnFields(i)), fieldCollection, Line.COLUMN)) != null) return moves;
		}
		return null;
	}
	
	private static List<Move> checkForXWing(List<Field> fields, FieldCollection fieldCollection, Line type) {
		for (int value = 1; value <= Board.DIMENSION; value++) {
			List<Field> candidate = Util.returnFieldsWhereValueIsPossible(fields, value);
			if (candidate.size() == 2) {
				List<Field> secondPair = null;
				if (type == Line.ROW) secondPair = findXWingRow(candidate, fieldCollection, value, type);
				if (type == Line.COLUMN) secondPair = findXWingColumn(candidate, fieldCollection, value, type);
				if (secondPair != null) {
					candidate.addAll(secondPair);
					return xWingConclusion(candidate, fieldCollection, value, type);
				}
			}
		}
		return null;
	}
	
	private static List<Field> findXWingRow(List<Field> firstPair, FieldCollection fieldCollection, int value, Line type) {
		List<Field> secondPair = new ArrayList<>();
		for(int row = 0; row < Board.DIMENSION; row++) {
			if (row != firstPair.get(0).getRow()) {
				List<Field> secondPairCandidate = Util.returnFieldsWhereValueIsPossible(fieldCollection.getUnsolvedFields(fieldCollection.getRowFields(row)), value);
				if (secondPairCandidate.size() == 2) {
					if (firstPair.get(0).getColumn() == secondPairCandidate.get(0).getColumn() && firstPair.get(1).getColumn() == secondPairCandidate.get(1).getColumn() && type == Line.ROW)
						secondPair.addAll(secondPairCandidate);
				}
			}
		}
		if (secondPair.size() == 2) return secondPair;
		return null;
	}
	
	private static List<Field> findXWingColumn(List<Field> firstPair, FieldCollection fieldCollection, int value, Line type) {
		List<Field> secondPair = new ArrayList<>();
		for(int column = 0; column < Board.DIMENSION; column++) {
			if (column != firstPair.get(0).getColumn()) {
				List<Field> secondPairCandidate = Util.returnFieldsWhereValueIsPossible(fieldCollection.getUnsolvedFields(fieldCollection.getColumnFields(column)), value);
				if (secondPairCandidate.size() == 2) {
					if (firstPair.get(0).getRow() == secondPairCandidate.get(0).getRow() && firstPair.get(1).getRow() == secondPairCandidate.get(1).getRow() && type == Line.COLUMN)
						secondPair.addAll(secondPairCandidate);
				}
			}
		}
		if (secondPair.size() == 2) return secondPair;
		return null;
	}
	
	private static List<Move> xWingConclusion(List<Field> xWing, FieldCollection fieldCollection, int value, Line type) {
		List<Move> moves = new ArrayList<>();
		List<Field> fieldsToRemove = getFieldsToRemoveValueFrom(xWing, fieldCollection, value, type);
		if(fieldsToRemove.size() > 0) {
			moves.addAll(Util.removePossibleValue(fieldsToRemove, value));
			for(Field field : xWing) {
				moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.CONCLUSION, "XWing" + " (" + field.getRow() + ", " + field.getColumn() + ") : " + value));
			}
			return moves;
		}
		return null;
	}
	
	private static List<Field> getFieldsToRemoveValueFrom (List<Field> xWing, FieldCollection fieldCollection, int value, Line type) {
		List<Field> fieldsToRemove = new ArrayList<>();
		List<Field> unsolvedTableFields = fieldCollection.getUnsolvedFields(fieldCollection.getWholeTableFields());
		for (Field field : unsolvedTableFields) {
			if ((field.getColumn() == xWing.get(0).getColumn() || field.getColumn() == xWing.get(1).getColumn())
				&& field.getRow() != xWing.get(0).getRow() && field.getRow() != xWing.get(2).getRow() && field.isPossible(value) && type == Line.ROW) fieldsToRemove.add(field);
			if ((field.getRow() == xWing.get(0).getRow() || field.getRow() == xWing.get(1).getRow())
					&& field.getColumn() != xWing.get(0).getColumn() && field.getColumn() != xWing.get(2).getColumn() && field.isPossible(value) && type == Line.COLUMN) fieldsToRemove.add(field);
		}
		return fieldsToRemove;
	}
}