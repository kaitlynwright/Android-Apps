package com.example.kaitlynwright.piggame;

import java.util.Random;
import java.lang.String;

import static com.example.kaitlynwright.piggame.Winner.*;

public class PigGame {

    // Game Variables
    private Random rand = new Random();
    private int p1Score = 0;
    private int p2Score = 0;
    private Winner winner = None;


    // Preferences
    private int maxScore = 100;
    private int dieSides = 6;


    // Getters & Setters
    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public void setP1Score(int score) { p1Score = score; }

    public void setP2Score(int score) { p2Score = score; }

    public void setMaxScore(int score) { maxScore = score; }

    public void setDieSides(int sides) { dieSides = sides; }


    // Methods
    public void updateScore(String currTurn, int currScore) {
        if (currTurn.equals("Player 1")) {
            if (currScore == 0) p1Score = 0;
            else p1Score += currScore;
        } else {
            if (currScore == 0) p2Score = 0;
            else p2Score += currScore;
        }
    }

    public int takeTurn() {
        int currScore = rand.nextInt((dieSides - 1) + 1) + 1;

        if (currScore == 1) {
            return 0;
        }
        return currScore;
    }

    public Winner isWinner(String currTurn, int p1Score, int p2Score ) {
        if (currTurn.equals("Player 1") && p1Score >= maxScore && p1Score > p2Score) {
            winner = Player1;
        } else if(currTurn.equals("Player 2") && p2Score >= maxScore && p2Score > p1Score) {
            winner = Player2;
        } else if (p1Score >= maxScore && p2Score >= maxScore && p1Score == p2Score) {
            winner = Tie;
        } else {
            winner = None;
        }
        return winner;
    }
}





