package gj.kalah.player.serafini;

import gj.kalah.player.Player;
import gj.kalah.player.serafini.Board;

/**
 * SerafiniPlayer e' un giocatore di Kalah che analizza la tabella di 
 * gioco per effettuare mosse come il "doppio turno" oppure il "furto"
 * di pedine avversarie, pensando anche a come difendersi in caso di
 * possibili mosse da parte dell'avversario.
 * 
 * @author Duccio Serafini
 */
public class SerafiniPlayer implements Player {
	
	/**
	 * board e' il campo che {@link SerafiniPlayer()} utilizza per decidere
	 * la mossa da eseguire.
	 */
	private Board board;
	
	/**
	 * turn viene utilizzato per tenere traccia dell'attuale turno di gioco.
	 */
	private int turn;
	
	/**
	 * first assume il valore di isFirst e viene utilizzato per determinare chi ha
	 * il primo turno e che mosse eseguire.
	 */
	private boolean first;
	
	/**
	 * player e' un valore che viene utilizzato per riconoscere il lato di campo
	 * appartenente al giocatore.
	 */
	private int player;
	
	/**
	 * start e' il metodo che inizializza {@link #board}, {@link #first} e 
	 * {@link #turn}. Imposta oltretutto {@link #player} a "1" se il giocatore
	 * e' primo, "0" se e' il secondo.
	 * 
	 * @param isFirst
	 * 				Il parametro passato dal GameManager che specifica se si e' il
	 *            	primo giocatore.
	 */
	public void start(boolean isFirst) {
		board = new Board();
		board.fillBoard();
		board.printBoard();
		first = isFirst;

		if (first) {
			player = 1;
		} else {
			player = 0;
		}
		
		turn = 0;
	}
	
	/**
	 * tellMove e' il metodo che riceve dal GameManager la mossa dell'avversario e
	 * aggiorna {@link #board} di conseguenza, aumentando inoltre il turno.
	 * 
	 * @param move
	 *            La mossa dell'avversario.
	 */
	public void tellMove(int move) {
		board.boardMove(1 - player, move);
		turn++;
	}
	
	/**
	 * move e' il metodo che sceglie la mossa da effettuare dando la precedenza alle
	 * possibilità nel seguente ordine: "rubare" pietre all'avversario, effettuare un
	 * turno doppio, evitare che l'avversario "rubi" pietre. Se nessuna delle mosse
	 * e' possibile, viene eseguita una mossa difensiva di accumulo pietre.
	 * 
	 * @return La mossa da restituire al GameManager.
	 */
	public int move() {
		int move = -1, steal = -1, again = -1;
		int counter = opponentCounter(1 - player);

		for (int i = 0; i < 6; i++) {
			int lastBowl = lastBowl(player, i);
			if (board.bowlPoints(player, i) != 0 && (lastBowl >= 0 && lastBowl <= 5)
					&& board.bowlPoints(player, lastBowl) == 0) {
				steal = i;
			} else if (board.bowlPoints(player, i) != 0 && lastBowl == 6) {
				again = i;
			} else {

			}
		}

		if (turn == 1) {
			move = 4;
		} else if (steal >= 0) {
			move = steal;
		} else if (again >= 0) {
			move = again;
		} else if (counter >= 0) {
			move = counter;
		} else {
			move = defensiveMove();
		}
		System.out.println(move);
		board.boardMove(player, move);
		turn++;
		return move;
	}
	
	/**
	 * opponentCounter e' il metodo che viene invocato da {@link #move()} per
	 * controllare se l'avversario ha la possibilità di "rubare" pietre. In questo
	 * caso viene poi calcolata la mossa che impedisce che ciò accada.
	 * 
	 * @param opponent
	 * 				Indica il lato della tabella dell'avversario
	 * @return La mossa che impedisce il "furto".
	 */
	public int opponentCounter(int opponent) {
		int counterMove = -1, stealBowl = -1;

		for (int i = 5; i >= 0; i--) {
			int lastBowl = lastBowl(opponent, i);
			if (board.bowlPoints(opponent, i) != 0 && (lastBowl >= 0 && lastBowl <= 5)
					&& board.bowlPoints(opponent, lastBowl) == 0 && board.bowlPoints(player, lastBowl) != 0) {
				stealBowl = i;
				break;
			}
		}
		
		if (stealBowl != -1) {
			for (int i = 5; i >= 0; i--) {
				int cont = board.bowlPoints(player, i) - (7 - i);
				if (board.bowlPoints(player, i) != 0 && cont >= stealBowl) {
					counterMove = i;
					break;
				}
			}
		}
		return counterMove;
	}
	
	/**
	 * Il metodo lastBowl, chiamato sia da {@link #move()} che da {@link #opponentCounter(int)},
	 * serve per calcolare la conca in cui viene disposta l'ultima sfera sul lato di campo
	 * del chiamante.
	 * 
	 * @param side
	 * 				Il lato di campo prescelto.
	 * @param bowl
	 * 				La conca da cui iniziare a calcolare la disposizione. 
	 * @return L'indice della conca dove viene disposta l'ultima pietra.
	 */
	public int lastBowl(int side, int bowl) {
		int lastBowl = -1;

		if (bowl >= 8 && bowl <= 19) {
			lastBowl = board.bowlPoints(side, bowl) - (7 + (6 - bowl));
		} else if (bowl < 8) {
			lastBowl = bowl + board.bowlPoints(side, bowl);
		}
		return lastBowl;
	}
	
	/**
	 * defensiveMove calcola la mossa difensiva di "accumulo" pietre cercando
	 * la prima conca non vuota.
	 * 
	 * @return L'indice della prima conca non vuota su cui eseguire la mossa.
	 */
	public int defensiveMove() {
		int defense = -1;

		for (int i = 0; i < 6; i++) {
			if (board.bowlPoints(player, i) != 0) {
				defense = i;
				break;
			}
		}
		return defense;
	}

}
