package gj.kalah.player.second;

import gj.kalah.player.Player;
import gj.kalah.player.second.Board;

public class SecondPlayer implements Player {

	private Board board;
	private int turn;
	private boolean first;
	private int player;

	public void start(boolean isFirst) {
		board = new Board();
		board.fillBoard();
		first = isFirst;

		if (first) {
			player = 1;

		} else {
			player = 0;
		}

		turn = 0;
	}

	public void tellMove(int move) {
		board.boardMove(1 - player, move);
		turn++;
	}

	public int move() {
		int move = -1, steal = -1, again = -1;
		int counter = opponentCounter(1 - player);

		for (int i = 0; i < 6; i++) {
			int lastBowl = board.lastBowl(player, i);

			// System.out.println(i + ": " + lastBowl);

			if (board.bowlPoints(player, i) != 0 && (lastBowl >= 0 && lastBowl <= 5)
					&& board.bowlPoints(player, lastBowl) == 0) {
				steal = i;															//RENDERE PIU INTELLIGENTE... RUBA ANCHE 1 SOLA PIETRA
			} else if (board.bowlPoints(player, i) != 0 && lastBowl == 6) {
				again = i;
			} else {

			}
		}
		// System.out.println(steal +" "+ again);

		/*
		 * if (steal >= 0) { move = steal; } else if (again >= 0) { move = again; } else
		 * {
		 * 
		 * }
		 */
		// move = turn;

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

		board.boardMove(player, move);
		turn++;
		return move;
	}

	public int opponentCounter(int opponent) {
		int counterMove = -1, stealBowl = -1;

		for (int i = 5; i >= 0; i--) {
			int lastBowl = board.lastBowl(opponent, i);

			// System.out.println(i + ": " + lastBowl);

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
