package sudoku;

enum Command {
	LOAD("Учитај", "Учитавање почетне позиције"),
	RESET("Ресетуј", "Враћање на почетну позицију"),
	SOLVE("Реши", "Решавање текуће позиције"),
	SOLVESTEP("Реши корак", "Решавање наредног корака"),
	HINT("Предлог", "Понуди предлог за решење следећег корака"),
	UNDO ("Назад", "Врати се корак уназад");
	
	String mSymbol;
	String mToolTip;
	
	Command (String pSymbol, String pToolTip) {
		mSymbol = pSymbol;
		mToolTip = pToolTip;
	}
	
	static Command findBySymbol (String s) {
	   for ( Command c : Command.values() ) {
	      if ( c.mSymbol.equalsIgnoreCase(s) ) {
	         return c;
	      }
	   }
	   return null;
	}
}
