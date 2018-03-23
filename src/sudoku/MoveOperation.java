package sudoku;

import java.awt.Color;

public enum MoveOperation {
   /** Upisivanje konacnog resenja u polje tabele */
	WRITE (false, null),
	/** Izbacivanje mogucnosti da se neka vrednost upise u polje tabele */
	DISABLE (false, null),
	/** Farbanje polja koje posredno ucestvuje u formiranju zakljucka **/
	CLUE (true, new Color(255, 255, 166)),
	/** Farbanje polja na kojem se izvodi zakljucak **/
	CONCLUSION (true, new Color(166, 255, 166));
	
   /** Boja kojom treba ofarbati pozadinu polja, kada se prikazuje hint **/
   Color backgroundColor = null;
   
   /** Indikator da li se radi o "hint"-operaciji ili i operaciji upisa **/
   boolean isHintOperation = false;
   
   MoveOperation(boolean isHintOperation, Color backgroundColor) {
      this.isHintOperation = isHintOperation;
      this.backgroundColor = backgroundColor;
   }
}
