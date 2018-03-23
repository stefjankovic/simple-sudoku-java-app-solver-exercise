package sudoku;

import java.util.ArrayList;
import java.util.List;

public class FieldCollection {
	private List<Field> fields;

	public FieldCollection(Board board) {
		this.fields = createWholeTableFields(board);
	}
	
	public List<Field> getRowFields(int row) {
		List<Field> rowFields = new ArrayList<>(); 
		for(Field field : this.fields) {
			if(field.getRow() == row) rowFields.add(field);
		}
		return rowFields;
	}
	
	public List<Field> getColumnFields(int column) {
		List<Field> columnFields = new ArrayList<>(); 
		for(Field field : this.fields) {
			if(field.getColumn() == column) columnFields.add(field);
		}
		return columnFields;
	}
	
	public List<Field> getWholeTableFields() {
		return this.fields;
	}
	
	public List<Field> getOneBoxFields(int i) {
		switch (i) {
		case 0:
			return getOneBoxFields(0, 0);
		case 1:
			return getOneBoxFields(3, 0);
		case 2:
			return getOneBoxFields(6, 0);
		case 3:
			return getOneBoxFields(0, 3);
		case 4:
			return getOneBoxFields(3, 3);
		case 5:
			return getOneBoxFields(6, 3);
		case 6:
			return getOneBoxFields(0, 6);
		case 7:
			return getOneBoxFields(3, 6);
		case 8:
			return getOneBoxFields(6, 6);
		default:
			return getOneBoxFields(0, 0);
		}
	}

	public List<Field> getUnsolvedFields(List<Field> fields) {
		List<Field> unsolvedFields = new ArrayList<>();
		for(Field field : fields) {
			if(!field.isSolved()) unsolvedFields.add(field);
		}
		return unsolvedFields;
	}

	private List<Field> createWholeTableFields(Board board) {
		List<Field> list = new ArrayList<>();
		for (int i = 0; i < Board.DIMENSION; i++ ) {
			for (int j = 0; j < Board.DIMENSION; j++) {
				list.add(new Field(i, j, board));
			}
		}
		return list;
	}
	
	public List<Field> getOneBoxFields(int row, int column) {
		List<Field> boxFields = new ArrayList<>();
		int startingRowInCurrentBox = getStartingBoxIndex(row);
		int startingColumnInCurrentBox = getStartingBoxIndex(column);
		for (int i = startingRowInCurrentBox; i < startingRowInCurrentBox + Board.SIZE; i++) {
			for (int j = startingColumnInCurrentBox; j < startingColumnInCurrentBox + Board.SIZE; j++) {
				for(Field field : fields) {
					if(field.getRow() == i && field.getColumn() == j) boxFields.add(field);
				}
			}
		}
		return boxFields;
	}

	private int getStartingBoxIndex(int index) {
		return index - (index % Board.SIZE);
	}
}
