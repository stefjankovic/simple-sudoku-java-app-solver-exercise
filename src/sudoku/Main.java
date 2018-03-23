package sudoku;

/**
 * Main klasa projekta
 */
public class Main {
	public static void main (String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Board board = new Board();
				new GUI(board, new Solver(board));
			}
		});
	}
}
