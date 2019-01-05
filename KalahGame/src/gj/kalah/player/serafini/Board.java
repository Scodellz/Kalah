package gj.kalah.player.serafini;

/**
 * Board e' la classe che rappresenta la tabella di gioco. Ad essa appartengono
 * i metodi che la modificano disponendo le pietre a seconda della mossa effettuata
 * dai giocatori.
 * 
 * @author Duccio Serafini
 */
public class Board {
	
	/**
	 * board e' la matrice bidimensionale che rappresenta il tavolo da gioco.
	 */
	private int[][] board = new int[2][7];
	
	/**
	 * fillBoard e' il metodo che inizializza {@link #board}, disponendo 4
	 * pietre in ogni conca, tranne nella sesta.
	 */
	public void fillBoard() {
		for (int i = 0; i < 6; i++) {
			board[0][i] = 4;
			board[1][i] = 4;
		}
	}
	
	/**
	 * Il metodo bowlPoints serve per contare quante pietre ci sono nella conca
	 * indicata.
	 * 
	 * @param side
	 * 			Indica il lato di tavolo a cui appartiene la conca.
	 * @param bowl
	 * 			Indica la conca che va esaminata
	 * @return La quantita di pietre all'interno della conca.
	 */
	public int bowlPoints(int side, int bowl) {
		int bp = board[side][bowl];
		return bp;
	}
	
	/**
	 * boardMove si occupa di modificare {@link #board} in base alla mossa effettuata
	 * dal giocatore chiamante. Chiama il metodo {@link #disposition(int, int, int, int)}
	 * per disporre le pietre e successivamente controlla se l'ultima di queste porti ad
	 * un "furto".
	 * 
	 * @param player
	 * 				Indica il giocatore chiamante.
	 * @param bowl
	 * 				La conca dalla quale prelevare le pietre.
	 */
	public void boardMove(int player, int bowl) {
		
		int points = bowlPoints(player, bowl);
		int side = player;
		board[side][bowl] = 0;

		int score = disposition(side, player, bowl + 1, points);

		bowl = score % 10;
		side = (score / 10) - 1;

		if (side == player && bowlPoints(player, bowl) == 1 && bowl != 6) {
			board[player][6] = board[player][6] + (bowlPoints(player, bowl) + bowlPoints(1 - player, 5 - bowl));
			board[player][bowl] = 0;
			board[1 - player][5 - bowl] = 0;
		}
		
		printBoard();
	}
	
	/**
	 * disposition e' un metodo ricorsivo chiamato da {@link #boardMove(int, int)} che
	 * dispone le pietre nelle conche, aumentando di 1 il loro valore ad ogni chiamata.
	 * Restituisce poi un valore che serve ad identificare l'ultima conca in cui e'
	 * stata disposta una pietra.
	 * 
	 * @param side
	 * 				Il lato di tavolo su cui deve essere disposta la pietra.
	 * @param player
	 * 				Il giocatore di turno che ha effettuato la mossa.
	 * @param bowl
	 * 				La conca in cui deve essere disposta la pietra.
	 * @param points
	 * 				Il numero di pietre ancora da da disporre.
	 * @return Un valore la cui prima cifra indica il lato di tavolo dove e' stata
	 * 		   disposta l'ultima pietra, mentre la seconda indica la conca.
	 */
	public int disposition(int side, int player, int bowl, int points) {
		int score = -1;
		if (points != 0) {
			if (bowl == 6 && side == 0) {
				if (side == player) {
					board[side][bowl]++;
					score = disposition(side + 1, player, 0, points - 1);
				} else {
					score = disposition(side + 1, player, 0, points);
				}
			} else if (bowl == 6 && side == 1) {
				if (side == player) {
					board[side][bowl]++;
					score = disposition(side - 1, player, 0, points - 1);
				} else {
					score = disposition(side - 1, player, 0, points);
				}
			} else {
				board[side][bowl]++;
				score = disposition(side, player, bowl + 1, points - 1);
			}
		} else {
			if (side == player && bowl == 0) {
				score = (++side * 10) + bowl;
			} else if (side != player && bowl == 0) {
				score = (++player * 10) + 6;
			} else {
				score = (++side * 10) + (bowl - 1); 
			}
			return score;								
		}
		return score;
	}
	/*
	public void printBoard() {
		System.out.print("-------------------------------" + "\n" + "B: " + "[" + bowlPoints(0, 6) + "] " );
		for (int i = 5; i >= 0; i--) {
			System.out.print(bowlPoints(0, i) + " ");
		}
		System.out.print("\n" + "A:     ");
		for (int i = 0; i < 6; i++) {
			System.out.print(bowlPoints(1, i) + " ");
		}
		System.out.print("[" + bowlPoints(1, 6) + "]" + "\n" + "---------------------------------" + "\n");
	}
	*/

}
