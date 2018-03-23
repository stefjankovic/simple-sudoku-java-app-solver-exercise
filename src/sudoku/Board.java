package sudoku;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model sudoku table
 */
class Board {
   private static final String EOL = "\n";

   static final int SIZE = 3;
   static final int DIMENSION = SIZE*SIZE;
   public int getDimension() { return DIMENSION; }
   private Field[][] field;

   private enum FieldStatus {
      SOLVED,
      UNSOLVED;
   }

   /**
    * Sudoku polje
    */
   private class Field {
      int row;
      int column;
      int solution = -1;
      Color backgroundColor = null;
      FieldStatus status = FieldStatus.UNSOLVED;
      boolean[] possible = new boolean[DIMENSION];
      int cntPossibilities = DIMENSION;

      Field(int row, int column) {
         this.row = row;
         this.column = column;
         reset();
      }

      void reset() {
         solution = -1;
         status = FieldStatus.UNSOLVED;
         cntPossibilities = DIMENSION;
         for ( int i=0 ; i<DIMENSION ; i++ ) {
            possible[i] = true;
         }
      }

      boolean isSolved() {
         return status.equals(FieldStatus.SOLVED);
      }

      /**
       * U polje upisuje sadrzaj value
       * @param value
       */
      private void set(int value) {
         solution = value;
         status = FieldStatus.SOLVED;
         for ( int i=0 ; i<DIMENSION ; i++ ) {
            disable(i);
         }
         enable(value);
      }

      private void setColor(Color color) {
         backgroundColor = color;
      }

      /**
       * U polju omogucuje upis sadrzaja value
       * @param value
       */
      private void enable(int value) {
         if ( !possible[value] ) {
            possible[value] = true;
            cntPossibilities++;
         }
      }

      /**
       * U polju onemogucuje upis sadrzaja value
       * @param value
       */
      private void disable(int value) {
         if ( possible[value] ) {
            possible[value] = false;
            cntPossibilities--;
         }
      }

      @Override
      public String toString() {
         return String.format("(%d, %d) : %s", row, column, isSolved() ? ""+(solution+1): "x ("+cntPossibilities+")");
      }
   }

   Board() {
      field = new Field[DIMENSION][DIMENSION];
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            field[row][column] = new Field(row, column);
         }
      }
      reset();
      load(mLastTask);
   }

   @SuppressWarnings("unused")
   private void printBoard() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            if ( isSolved(row, column) ) {
               System.out.print(getSolution(row, column)+1);
            } else {
               System.out.print(".");
            }
         }         
         System.out.println();
      }
   }
   
   public boolean solve() {
      Solver solver = new Solver(this);
      while ( !isSolved() ) {
         List<Move> moves = solver.getMove();
         if ( moves==null || moves.size()==0 ) {
            break;
         }

         for ( Move move : moves ) {
            switch (move.operation) {
               case WRITE:
                  set(move.row, move.column, move.value);
                  break;
               case DISABLE:
                  disable(move.row, move.column, move.value);
                  break;
               default:
                  break;
            }
         }
      }

      return isSolved();
   }


   private String mLastTask = "xxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\n";

   public List<Move> load() {
      return load(mLastTask);
   }

   /**
    * Ucitava sadrzaj fajla u kome se nalazi opis pocetne pozicije sudoku problema
    * @param file Ime fajla
    */
   public List<Move> load(File file) {
      if (file == null) return null;

      BufferedReader in;
      String s = "";
      try {
         in = new BufferedReader(new FileReader(file));

         String inputLine;
         while ((inputLine = in.readLine()) != null) {
            s += inputLine + EOL;
         }
         in.close();
         return load(s);
      } catch (IOException ioe) {
         return null;
      }
   }

   /**
    * Ucitava pocetnu poziciju zadatka
    * @param text String koji definise pocetnu poziciju<br>
    * Svaki red je predstavljen nizom brojeva (od 1 do 9) ili znacima 'x' (tamo gde je polje nepopunjeno)<br>
    * Redovi su medjusobno razdvojeni EOL simbolom
    */
   public List<Move> load(String s) {
      mLastTask = s;
      String[] rowData = s.split("[\n]");
      reset();
      int number;

      List<Move> moves = new ArrayList<Move>();
      
      char minValue = '0';
      char maxValue = '9';      
      int currRow = 0;
      for ( int row=0 ; row<rowData.length ; row++ ) {
         boolean numberFound = false;
         int currColumn = -1;
         for ( int index=0 ; index<rowData[row].length() ; index++ ) {
            char c = rowData[row].charAt(index);
            if ( (c>=minValue && c<=maxValue) || c=='x' || c=='X' || c=='.' ) {
               currColumn++;
               numberFound = true;
            }
            if ( c<minValue || c>maxValue ) continue;

            try {
               number = Integer.valueOf(rowData[row].substring(index, index+1));
               moves.add(new Move(currRow, currColumn, number-1, MoveOperation.WRITE, ""));
            } catch (NumberFormatException e) {
            }
         }
         if ( numberFound ) currRow++;
      }
      return moves;
   }

   /**
    * Resetuje stanje na tabli
    */
   public void reset() {
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            field[row][column].reset();
         }
      }
   }

   private boolean validPosition (int row, int column) {
      if ( row<0 || row>=DIMENSION ) return  false;
      if ( column<0 || column>=DIMENSION ) return  false;
      return true;
   }

   private Field getField(int row, int column) {
      if ( !validPosition(row, column) ) return null;
      return field[row][column];
   }

   Color getFieldColor(int row, int column) {
      return getField(row, column).backgroundColor;
   }

   public void setColor (int row, int column, Color color) {
      if ( !validPosition(row, column) ) return;
      getField(row, column).setColor(color);
   }

   void resetColors() {
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            getField(row, column).setColor(null);
         }
      }
   }

   /**
    * Upisuje sadrzaj 'value' u polje sa koordinatama (row, column)
    * @param row x-coordinata polja u koje se vrsi upis
    * @param column y-coordinata polja u koje se vrsi upis
    * @param value Vrednost koja se upisuje
    */
   public void set (int row, int column, int value) {
      if ( !validPosition(row, column) ) return;
      getField(row, column).set(value);
      for ( int index=0 ; index<DIMENSION ; index++ ) getField(row, index).disable(value);
      for ( int index=0 ; index<DIMENSION ; index++ ) getField(index, column).disable(value);
      int startx = 3*(row/3);
      int startY = 3*(column/3);
      for ( int i=0 ; i<SIZE ; i++ ) { 
         for ( int j=0 ; j<SIZE ; j++ ) {
            getField(startx+i, startY+j).disable(value);
         }
      }	
   }

   /**
    * Vraca informaciju da li je reseno polje sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public boolean isSolved(int row, int column) {
      return field[row][column].isSolved();
   }

   /**
    * Vraca informaciju da li je zadatak resen
    */
   public boolean isSolved() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            if ( !isSolved(row, column) ) {
               return false;
            }
         }
      }
      return true;
   }

   /**
    * Vraca broj koji je resenje polja sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public int getSolution(int row, int column) {
      return field[row][column].solution;
   }

   /**
    * Vraca informaciju da li je u polju sa koordinatama (row, column) jos uvek moguce upisati broj value
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    * @param value Vrednost
    */
   public boolean isPossible(int row, int column, int value) {
      return field[row][column].possible[value];
   }

   /**
    * Vraca broj preostalih mogucnosti u polju sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public int getPossibilitiesCount(int row, int column) {
      return field[row][column].cntPossibilities;
   }

   /**
    * U polju sa koordinatama (row, column) onemogucava upis broj value
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    * @param value Vrednost
    */
   public void disable(int row, int column, int value) {
      field[row][column].disable(value);
   }

   private int[] solutionCounter = new int[Board.DIMENSION];
   private void resetSolutionCounter() {
      for ( int i=0 ; i<Board.DIMENSION ; i++ ) {
         solutionCounter[i] = 0;
      }
   }
   private boolean isPartialSolutionGood() {
      for ( int i=0 ; i<Board.DIMENSION ; i++ ) {
         if ( solutionCounter[i]!=1 ) {
            return false;
         }
      }
      return true;
   }

   /**
    * Vraca informaciju da li je zadatak resen
    */
   public boolean isSolutionGood() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         resetSolutionCounter();
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            solutionCounter[getSolution(row, column)]++;
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
         resetSolutionCounter();
         for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
            solutionCounter[getSolution(row, column)]++;
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      for ( int block=0 ; block<Board.DIMENSION ; block++ ) {
         resetSolutionCounter();
         for ( int row=Board.SIZE * (block / Board.SIZE) ; row<Board.SIZE * ((block / Board.SIZE) + 1) ; row++ ) {
            for ( int column=Board.SIZE * (block % Board.SIZE) ; column<Board.SIZE * ((block % Board.SIZE) + 1) ; column++ ) {
               solutionCounter[getSolution(row, column)]++;
            }
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      return true;
   }
}
