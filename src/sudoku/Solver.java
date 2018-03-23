package sudoku;

import java.util.List;

public class Solver {
   /**
    * Tabla na kojoj se resava Sudoku problem
    */
   private Board board;

   // =================================================================================
   // Constructor
   // =================================================================================
   Solver(Board board) {
      this.board = board;
   }

   /**
    * Vraca sledeci potez pri resavanju sudoku problema (ili null, ako resenja nema
    */
   List<Move> getMove() {
      List<Move> move;

      move = Singles.solveNakedSingle(board);
      if (move != null) return move;

      move = Singles.solveHiddenSingle(board);
      if (move != null) return move;

      move = LockedCandidates.lockedCandidatesOne(board);
      if (move != null) return move;

      move = LockedCandidates.lockedCandidatesTwo(board);
      if (move != null) return move;
      
      move = NakedType.solveNakedType(board);
      if (move != null) return move;
      
      move = HiddenType.solveHiddenType(board);
      if (move != null) return move;
      
      move = XWing.solveXWing(board);
      if (move != null) return move;

      return null;
   }
}
