package sudoku;

import java.util.ArrayList;
import java.util.List;

public final class NakedType {
	
	public static List<Move> solveNakedType(Board board) {
		FieldCollection fieldCollection = new FieldCollection(board);
		for(int i = 0; i < Board.DIMENSION; i++) {
			List<Move> moves;

			for (CombinationTypes type : CombinationTypes.values()) {
				if((moves = NakedType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getRowFields(i)), type)) != null) 
					return moves;
				if((moves = NakedType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getColumnFields(i)), type)) != null) 
					return moves;
				if((moves = NakedType.checkForNakedType(fieldCollection.getUnsolvedFields(fieldCollection.getOneBoxFields(i)), type)) != null) 
					return moves;
			}
		}
		return null;
	}

	private static List<Move> checkForNakedType(List<Field> fields, CombinationTypes type) {
		int nakedTypeValue = type.getDimension();
		List<Field> candidates = findCandidatesForNakedType(Util.filterFieldsByNumberOfPossibleValues(fields, 2, nakedTypeValue),
				nakedTypeValue);
		if(candidates != null) {
			List<Field> nakedTypeList = new ArrayList<>();
			int[] combination = findNakedType(nakedTypeList, candidates, type.getPossibleCombinations(), nakedTypeValue);
			if(nakedTypeList != null && combination != null && fields.size() != nakedTypeList.size()) {
				fields.removeAll(nakedTypeList);
				return nakedTypeConclusion(fields, nakedTypeList, combination, nakedTypeValue);
			}
		}
		return null;
	}
	
	private static List<Move> nakedTypeConclusion(List<Field> fields, List<Field> nakedType, int[] valuesToRemove, int nakedTypeValue) {
		List<Move> moves = new ArrayList<>();
		moves.addAll(Util.removePossibleValues(fields, valuesToRemove));
		if(!moves.isEmpty()) {
			for(Field field : nakedType) {
				moves.add(markConclusion(field.getRow(), field.getColumn(), field.getPossibleValues()[0], nakedTypeValue));
			}
			return moves;
		}
		return null;
	}
	
	private static List<Field> findCandidatesForNakedType(List<Field> candidates, int nakedTypeValue) {
		return (candidates.size() >= nakedTypeValue && Util.checkFieldsForNumberOfPossibleValuesInRange(candidates, 2, nakedTypeValue)) ? candidates : null;
	}
	
	private static int[] findNakedType(List<Field> listToPopulate, List<Field> candidates, List<int[]> combinations, int nakedTypeValue) {
		for(int[] combination : combinations) {
			listToPopulate.clear();
			for(Field candidate : candidates) {
				if(Util.compareCandidateToCombination(candidate.getPossibleValues(), combination) 
						&& !listToPopulate.contains(candidate)) 
					listToPopulate.add(candidate);
			}
			if(listToPopulate.size() == nakedTypeValue) {
				return combination;
			}
		}
		return null;
	}
	
	private static Move markConclusion(int rowCoordinate, int columnCoordinate, int value, int nakedTypeValue) {
		return new Move(rowCoordinate, columnCoordinate, value, MoveOperation.CONCLUSION, "Naked" + nakedTypeValue);
	}
}
