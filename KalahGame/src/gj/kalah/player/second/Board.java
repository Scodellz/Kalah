package gj.kalah.player.second;

public class Board {

	private int[][] board = new int[2][7];

	public void fillBoard() {
		for (int i = 0; i < 6; i++) {
			board[0][i] = 4;
			board[1][i] = 4;
		}
	}

	public void boardMove(int player, int bowl) {
		int points = bowlPoints(player, bowl);
		int side = player;
		board[side][bowl] = 0;

		int score = disposition(side, player, bowl + 1, points);

		//printBoard();

		//System.out.print("\n" + score + ": ");

		bowl = score % 10;
		side = (score / 10) - 1;

		//System.out.println(side + " " + bowl);
		//System.out.println(bowlPoints(player, bowl));

		if (side == player && bowlPoints(player, bowl) == 1 && bowl != 6) {
			//System.out.println(bowlPoints(1-player, bowl));
			board[player][6] = board[player][6] + (bowlPoints(player, bowl) + bowlPoints(1 - player, 5 - bowl));
			//System.out.println(bowlPoints(player, 6));
			board[player][bowl] = 0;
			board[1 - player][5 - bowl] = 0;
		}
		//System.out.println(bowlPoints(player, 6));
		//printBoard();

	}

	public int bowlPoints(int player, int bowl) {
		int bp = board[player][bowl];
		// System.out.println(bowl +": " + bp);
		return bp;
	}

	public int disposition(int side, int player, int bowl, int points) {
		int score = -1;
		if (points != 0) {
			if (bowl == 6 && side == 0) {
				if (side == player) {
					board[side][bowl]++;
					//System.out.println(side + " " + bowl);
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
				score = (++side * 10) + bowl; 				//NON SI DEVE SOTTRARRE QUANDO DISPOSIZIONE FINISCE IN SIDE != PLAYER
			} else if (side != player && bowl == 0) {
				score = (++player * 10) + 6; 
			} else {
				score = (++side * 10) + (bowl - 1); 
			}
			// System.out.println(side + " " + bowl);
			//score = (++side * 10) + bowl; 				// ++ serve per evitare che il "lato 0" mandi in confusione i calcoli
			return score;								// 	BOWL è SEMPRE AVANTI DI 1
		}
		//System.out.println(side + " "+ bowl);
		//score = (++side * 10) + bowl;
		// System.out.println(score);
		return score;
	}
	
/*
	public int disposition(int side, int player, int bowl, int points) {
		int score = -1;
		
		if(points == 0) {
			//System.out.println(side + " "+ bowl);
			score = (++side * 10) + bowl;
			// System.out.println(score);
		}else {
		
		if (bowl == 6 && side == 0) {
			if (side == player) {
				board[side][bowl]++;
				disposition(side + 1, player, 0, points - 1);
				score = (++side * 10) + bowl;
			} else {
				disposition(side + 1, player, 0, points);
				score = (++side * 10) + bowl;
			}
		} else if (bowl == 6 && side == 1) {
			if (side == player) {
				board[side][bowl]++;
				disposition(side - 1, player, 0, points - 1);
				score = (++side * 10) + bowl;
			} else {
				disposition(side - 1, player, 0, points);
				score = (++side * 10) + bowl;
			}
		} else {
			board[side][bowl]++;
			disposition(side, player, bowl + 1, points - 1);
			score = (++side * 10) + bowl;
		}
		}
		
		return score;
	}
*/
	
	public int lastBowl(int player, int bowl) {
		int lastBowl = -1;

		if (bowl >= 8 && bowl <= 19) {
			lastBowl = bowlPoints(player, bowl) - (7 + (6 - bowl));
		} else if (bowl < 8) {
			lastBowl = bowl + bowlPoints(player, bowl);
			// System.out.println(bowlPoints(player, bowl));
		}

		return lastBowl;
	}

	public boolean isEmpty(int player, int bowl) {
		boolean isEmpty = false;
		if (board[player][bowl] == 0) {
			isEmpty = true;
		}
		return isEmpty;
	}

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
}
