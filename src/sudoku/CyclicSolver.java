package sudoku;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CyclicSolver {
   private static List<SolutionStep> solutionSteps = new ArrayList<SolutionStep>();
   private static Board board;
   
   private static class SSFilenameFilter implements FileFilter {
      @Override
      public boolean accept(File file) {
         String path = file.getAbsolutePath().toLowerCase();
         if ( path.endsWith(".ss") ) return true;
         return false;
      }
   }
   
   public static void main(String[] args) {
      board = new Board();
      int counter = 0;
      int success = 0;
      
      File currFolder = new File(".");
      File[] ssFile = currFolder.listFiles(new SSFilenameFilter());
      if ( ssFile==null ) {
    	  System.out.println("No ss files.");
    	  return;
      }
      
      for ( File ss : ssFile ) {
         String answer;
         counter++;
         try {
            List<Move> moves = board.load(ss);
            solutionSteps.clear();
            solutionSteps.add(new SolutionStep(moves));
            playStep(0);
            
            boolean b = board.solve();
            answer = b ? "Solved" : "UNSOLVED";
            if ( b ) {
               if ( !board.isSolutionGood() ) {
                  System.out.println("BAAAAAAAAAAAAAAAAAAD");
               }
               success++;
            }
         } catch (Exception e) {
            answer = "EXCEPTION";
         }
         System.out.println(ss.getName() + " : " + answer);
      }
      System.out.println(success + "/" + counter);
   }
   
   static void playStep(int index) {
      List<Move> moves = solutionSteps.get(index).moves;

      for ( Move move : moves ) {
         switch (move.operation) {
            case WRITE:
               board.set(move.row, move.column, move.value);
               break;
            case DISABLE:
               board.disable(move.row, move.column, move.value);
               break;
            default:
               break;
         }
      }
   }
}
