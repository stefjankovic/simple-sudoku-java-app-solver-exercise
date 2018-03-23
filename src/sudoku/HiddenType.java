package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HiddenType {

	public static List<Move> solveHiddenType(Board board) {
		FieldCollection fieldCollection = new FieldCollection(board);
		for(int i = 0; i < Board.DIMENSION; i++) {
			List<Move> moves;

			for (CombinationTypes type : CombinationTypes.values()) {
				if((moves = HiddenType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getRowFields(i)), type)) != null) 
					return moves;
				if((moves = HiddenType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getColumnFields(i)), type)) != null) 
					return moves;
				if((moves = HiddenType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getOneBoxFields(i)), type)) != null) 
					return moves;
			}
		}
		return null;
	}
	
	private static List<Move> checkForNakedType(List<Field> fields, CombinationTypes type) {
		int hiddenTypeValue = type.getDimension();
		int[] savedCombination = new int[hiddenTypeValue];
		if(fields != null) {
			List<Field> hiddenType = findHiddenType(fields, type.getPossibleCombinations(), hiddenTypeValue, savedCombination); 
			if(hiddenType != null) return hiddenTypeConclusion(hiddenType, savedCombination, hiddenTypeValue);
		}
		return null;
	}
	
	private static List<Move> hiddenTypeConclusion(List<Field> hiddenType, int[] combination, int hiddenTypeValue) {
		List<Move> moves = new ArrayList<>();
		for(Field field : hiddenType) {
			int[] fieldPossibleValues = Arrays.copyOfRange(field.getPossibleValues(), 0, field.getPossibleValues().length);
			int[] valuesToRemove = Util.differentiateArrays(fieldPossibleValues, combination);
			moves.addAll(Util.removePossibleValues(field, valuesToRemove));
		}
		if(!moves.isEmpty()) {
			for(Field hidden : hiddenType) {
				moves.add(markConclusion(hidden.getRow(), hidden.getColumn(), hidden.getPossibleValues()[0], hiddenTypeValue));
			}
			return moves;
		}
		return null;
	}
	
	private static List<Field> findHiddenType(List<Field> fields, List<int[]> combinations, int hiddenTypeValue, int[] whereToSaveCombination) {
		for(int[] combination : combinations) {
			List<Field> possibleHiddenType = new ArrayList<>();
			for(Field candidate : fields) {
				if(Util.fieldHasAllPossibleValues(candidate, combination)) possibleHiddenType.add(candidate);
			}
			if(possibleHiddenType.size() == hiddenTypeValue) {
				List<Field> hiddenType = possibleHiddenType;
				fields.removeAll(hiddenType);
				for (int i = 0; i < whereToSaveCombination.length; i++) {
					whereToSaveCombination[i] = combination[i];
				}
				if(Util.fieldsContainOneOfTheValues(fields, whereToSaveCombination)) return null;
				return hiddenType;
			}
		}
		return null;
	}
	
	private static Move markConclusion(int rowCoordinate, int columnCoordinate, int value, int hiddenTypeValue) {
		return new Move(rowCoordinate, columnCoordinate, value, MoveOperation.CONCLUSION, "Hidden" + hiddenTypeValue);
	}
}