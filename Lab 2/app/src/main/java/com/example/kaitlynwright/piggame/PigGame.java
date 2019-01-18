package com.example.kaitlynwright.piggame;

import java.util.Random;
import java.lang.String;

import static com.example.kaitlynwright.piggame.Winner.*;

public class PigGame {

    private Random rand = new Random();
    private int p1Score = 0;
    private int p2Score = 0;
    private Winner winner = None;

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public void updateScore(String currTurn, int currScore) {
        if (currTurn == "Player 1") {
            if (currScore == 0) p1Score = 0;
            else p1Score += currScore;
        } else {
            if (currScore == 0) p2Score = 0;
            else p2Score += currScore;
        }
    }

    public int takeTurn() {
        int currScore = rand.nextInt((6 - 1) + 1) + 1;

        if (currScore == 1) {
            return 0;
        }
        return currScore;
    }

    public Winner isWinner(String currTurn, int p1Score, int p2Score ) {
        if (currTurn == "Player 1" && p1Score >= 10 && p1Score > p2Score) {
            winner = Player1;
        } else if(currTurn == "Player 2" && p2Score >= 10 && p2Score > p1Score) {
            winner = Player2;
        } else if (p1Score >= 10 && p2Score >= 10 && p1Score == p2Score) {
            winner = Tie;
        } else {
            winner = None;
        }
        return winner;
    }
}





