package sudoku;

/**
 * Potez, u resavanju sudoku problema
 */
class Move {
   /** x-coordinata polja [0, Board.DIMENSION-1] */
   int row;
   /** y-coordinata polja [0, Board.DIMENSION-1] */
   int column;
   /** vrednost [0, Board.DIMENSION-1] */
   int value;
   /** Prateci tekst (objasnjenje poteza) */
   String text;
   /** Operacija koju potez sprovodi */
   MoveOperation operation;

   public Move(int row, int column, int value, MoveOperation operation, String text) {
      this.row = row;
      this.column = column;
      this.value = value;
      this.operation = operation;
      this.text = text;
   }

   @Override
   public String toString() {
      return "(" + row + ", " + column + ") " + text;
   }
}
