package sudoku;

import java.util.ArrayList;
import java.util.List;

public final class Util {
	
	public static boolean compareCandidateToCombination(int[] candidate, int[] combination) {
		for(int i = 0; i < candidate.length; i++) {
			int counter = 0;
			for(int j = 0; j < combination.length; j++) {
				if((candidate[i]) == combination[j]) {
					continue;
				} else {
					counter++;
				}
			}
			if(counter == combination.length) return false;
		}
		return true;
	}
	
	public static boolean fieldHasAllPossibleValues(Field field, int[] valuesToCheck) {
		for(int i = 0; i < valuesToCheck.length; i++) {
			if(!field.isPossible(valuesToCheck[i])) return false;
		}
		return true;
	}
	
	public static boolean fieldsContainOneOfTheValues(List<Field> fields, int[] values) {
		for(Field field : fields) {
			for(int i = 0; i < values.length; i++) {
				if(field.isPossible(values[i])) return true;
			}
		}
		return false;
	}
	
	public static List<Field> returnFieldsWhereValueIsPossible(List<Field> fields, int value) {
		List<Field> list = new ArrayList<>();
		for (Field field : fields) {
			if (field.isPossible(value)) list.add(field);
		}
		return list;
	}
	
	public static boolean checkForCandidateWithCertainNumberOfPossibleValues(List<Field> candidates, int number) {
		for(Field cand : candidates) {
			if(cand.getPossibleValues().length == number) return true;
		}
		return false;
	}

	public static Field getCandidateWithCertainNumberOfPossibleValues(List<Field> candidates, int number) {
		for(Field cand : candidates) {
			if(cand.getPossibleValues().length == number) return cand;
		}
		return null;
	}
	
	public static List<Field> filterFieldsByNumberOfPossibleValues(List<Field> fields, int from, int upTo) {
		List<Field> filteredFields = new ArrayList<>();
		for(Field field : fields) {
			if(field.getPossibleValues().length >= from && field.getPossibleValues().length <= upTo)
				filteredFields.add(field);
		}
		return filteredFields;
	}
	
	
	public static boolean checkFieldsForNumberOfPossibleValuesInRange(List<Field> candidates, int from, int upTo) {
		for(Field field : candidates) {
			if(field.getPossibleValues().length < from && field.getPossibleValues().length > upTo) return false;
		}
		return true;
	}
	
	public static List<Move> removePossibleValue(List<Field> fields, int value) {
		List<Move> moves = new ArrayList<>();
		for (Field field : fields) {
			moves.add(markClue(field.getRow(), field.getColumn(), value));
			moves.add(removeValue(field.getRow(), field.getColumn(), value));
		}
		return moves;
	}
	
	public static List<Move> removePossibleValues(List<Field> fields, int[] valuesToRemove) {
		List<Move> moves = new ArrayList<>();
		for(int i = 0; i < valuesToRemove.length; i++) {
			for(Field field : fields) {
				if(field.isPossible(valuesToRemove[i])) {
					moves.add(markClue(field.getRow(), field.getColumn(), valuesToRemove[i]));
					moves.add(removeValue(field.getRow(), field.getColumn(), valuesToRemove[i]));
				}
			}
		}
		return moves;
	}
	
	public static List<Move> removePossibleValues(Field field, int[] valuesToRemove) {
		List<Move> moves = new ArrayList<>();
		for(int i = 0; i < valuesToRemove.length; i++) {
			if(field.isPossible(valuesToRemove[i])) {
				moves.add(markClue(field.getRow(), field.getColumn(), valuesToRemove[i]));
				moves.add(removeValue(field.getRow(), field.getColumn(), valuesToRemove[i]));

			}
		}
		return moves;
	}
	
	public static int[] differentiateArrays(int[] array, int[] valuesToRemove) {
		int counter = 0;
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < valuesToRemove.length; j++) {
				if(array[i] == valuesToRemove[j]) {
					array[i] = -1;
					counter++;
				}
			}
		}
		int[] newArray = new int[array.length - counter];
		int position = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] != -1) {
				newArray[position] = array[i];
				position++;
			}
		}
		return newArray;
	}
	
	private static Move removeValue(int rowCoordinate, int columnCoordinate, int value) {
		return new Move(rowCoordinate, columnCoordinate, value - 1, MoveOperation.DISABLE, 
				"Removed: " + value + " from (" + (rowCoordinate + 1) + ", " + (columnCoordinate + 1) +")");
	}

	private static Move markClue(int rowCoordinate, int columnCoordinate, int value) {
		return new Move(rowCoordinate, columnCoordinate, value - 1, MoveOperation.CLUE, 
				"Clue: " + value + " in (" + (rowCoordinate + 1) + ", " + (columnCoordinate + 1) +")");
	}
	
	public static void appendFieldsWithoutDuplicates(List<Field> original, List<Field> toAppend) {
		for (Field field : toAppend) {
			if (!original.contains(field)) original.add(field);
		}
		return;
	}

	public static List<int[]> getPossibleCombinations(int ofSize) {
		int[] inputArr = new int [Board.DIMENSION];
		List<int[]> combinations = new ArrayList<>();
		
		int[] data = new int[ofSize];
		
		for(int i = 1; i <= Board.DIMENSION; i++)
			inputArr [i-1] = i;
		
		Util.combinationUtil(inputArr, data, 0, Board.DIMENSION - 1, 0, data.length, combinations);
		return combinations;
	}
	
	/* arr[]  ---> Input Array
       data[] ---> Temporary array to store current combination
       start & end ---> Staring and Ending indexes in arr[]
       index  ---> Current index in data[]
       r ---> Size of a combination to be printed */
	private static void combinationUtil(int arr[], int data[], int start, int end, int index, int r, List<int[]> list) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			int[] array = new int[r];
			for (int j=0; j<r; j++) {
				array[j] = data[j];
			}
			list.add(array);
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i=start; i <= end && end-i+1 >= r-index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i+1, end, index+1, r, list);
		}
	}
}
