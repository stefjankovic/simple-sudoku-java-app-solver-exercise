package sudoku;

import java.util.ArrayList;
import java.util.List;

public final class Singles {
	
	private static enum Type {
		NAKEDSINGLE("Naked Single"),
		HIDDENSINGLE("Hidden Single");
		
		private String name;
		
		private String getName() {
			return this.name;
		}
		
		private Type(String name) {
			this.name = name;
		}
	}
	
	public static final List<Move> solveNakedSingle(Board board) {
		FieldCollection boardFields = new FieldCollection(board);
		List<Field> unsolvedFields = boardFields.getUnsolvedFields(boardFields.getWholeTableFields());
		for (int value = 1; value <= Board.DIMENSION; value++) {
			Field nakedSingle = findNakedSingle(unsolvedFields, value);
			if (nakedSingle != null) return nakedSingleConclusion(nakedSingle, boardFields, value);
		}
		return null;
	}
	
	private static final Field findNakedSingle(List<Field> fields, int value) {
		for (Field field : fields) {
			if (field.isPossible(value) && field.getPossibleValues().length == 1) return field;
		}
		return null;
	}
	
	private static final List<Move> nakedSingleConclusion(Field field, FieldCollection board, int value) {
		List<Field> column = board.getColumnFields(field.getColumn());
		List<Field> row = board.getRowFields(field.getRow());
		List<Field> box = board.getOneBoxFields(field.getRow(), field.getColumn());
		List<Field> clueFields = new ArrayList<>();
		clueFields.addAll(column);
		Util.appendFieldsWithoutDuplicates(clueFields, row);
		Util.appendFieldsWithoutDuplicates(clueFields, box);
		return singlesConclusion(clueFields, field, value, Type.NAKEDSINGLE);
	}
	
	private static final List<Move> singlesConclusion(List<Field> clueFields, Field single, int value, Type type) {
		List<Move> moves = new ArrayList<>();
		for(Field field : clueFields) {
			moves.add(new Move(field.getRow(), field.getColumn(), value -1, MoveOperation.CLUE, "Clue"));
		}
		moves.add(new Move(single.getRow(), single.getColumn(), value - 1, MoveOperation.CONCLUSION, 
				type.getName() + " (" + (single.getRow() + 1) + ", " + (single.getColumn() + 1) + ") : " + value));
		moves.add(new Move(single.getRow(), single.getColumn(), value - 1, MoveOperation.WRITE, 
				type.getName() + " (" + (single.getRow() + 1) + ", " + (single.getColumn() + 1) + ") : " + value));
		return moves;	
	}
	
	public static final List<Move> solveHiddenSingle(Board board) {
		FieldCollection boardFields = new FieldCollection(board);
		for (int i = 0; i < Board.DIMENSION; i++) {
			for (int value = 1; value <= Board.DIMENSION; value++) {
				Field single = findHiddenSingle(boardFields.getUnsolvedFields(boardFields.getColumnFields(i)), value);
				if(single != null) return singlesConclusion(boardFields.getUnsolvedFields(boardFields.getColumnFields(i)), single, value, Type.HIDDENSINGLE);
				single = findHiddenSingle(boardFields.getUnsolvedFields(boardFields.getColumnFields(i)), value);
				if(single != null) return singlesConclusion(boardFields.getUnsolvedFields(boardFields.getRowFields(i)), single, value, Type.HIDDENSINGLE);
				single = findHiddenSingle(boardFields.getUnsolvedFields(boardFields.getColumnFields(i)), value);
				if(single != null) return singlesConclusion(boardFields.getUnsolvedFields(boardFields.getOneBoxFields(i)), single, value, Type.HIDDENSINGLE);
			}
		}
		return null;
	}
	
	private static final Field findHiddenSingle(List<Field> fields, int value) {
		List<Field> field = Util.returnFieldsWhereValueIsPossible(fields, value);
		if(field.size() == 1) return field.get(0);
		return null;
	}
}