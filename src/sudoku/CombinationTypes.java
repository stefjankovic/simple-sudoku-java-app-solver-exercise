package sudoku;

import java.util.List;

enum CombinationTypes {
	PAIR(2),
	TRIPLE(3),
	QUAD(4);

	private int dimension;
	private List<int[]> possibleCombinations;

	public int getDimension() {
		return dimension;
	}

	public List<int[]> getPossibleCombinations() {
		return possibleCombinations;
	}

	private CombinationTypes(int dimension) {
		this.dimension = dimension;
		this.possibleCombinations = Util.getPossibleCombinations(dimension);
	}
}
