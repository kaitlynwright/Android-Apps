package com.example.kaitlynwright.piggame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView.OnEditorActionListener;
import android.view.View;
import android.view.View.OnClickListener;
import java.lang.String;
import android.content.Context;

public class Pig extends AppCompatActivity implements OnEditorActionListener, OnClickListener {

    PigGame game = new PigGame();
    private EditText p1EditText;
    private EditText p2EditText;
    private TextView score1;
    private TextView score2;
    private TextView turnLabel;
    private TextView turnScore;
    private Button rollDie;
    private Button endTurn;
    private Button newGame;
    private ImageView dieImage;

    private int currScore = 0;
    private int turnCount = 0;
    private String currTurn = "Player 1";
    private String P1Name;
    private String P2Name;
    private int p1Turns;
    private int p2Turns;
    private Winner winner;
    private boolean firstTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig);

        p1EditText = (EditText) findViewById(R.id.p1EditText);
        p2EditText = (EditText) findViewById(R.id.p2EditText);
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        turnLabel = (TextView) findViewById(R.id.turnLabel);
        turnScore = (TextView) findViewById(R.id.turnScore);
        rollDie = (Button) findViewById(R.id.rollDie);
        endTurn = (Button) findViewById(R.id.endTurn);
        newGame = (Button) findViewById(R.id.newGame);
        dieImage = (ImageView) findViewById(R.id.dieImage);

        p1EditText.setOnEditorActionListener(this);
        p2EditText.setOnEditorActionListener(this);
        rollDie.setOnClickListener(this);
        endTurn.setOnClickListener(this);
        newGame.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rollDie:
                currScore = game.takeTurn();
                displayImage();
                turnCount += currScore;
                turnScore.setText(String.valueOf(turnCount));

                if (currTurn == "Player 1" && currScore == 0) {
                    game.updateScore(currTurn, currScore);
                    score1.setText(String.valueOf(game.getP1Score()));
                    currTurn = "Player 2";
                    turnLabel.setText(String.format("%s's Turn", P2Name));
                    p1Turns += 1;
                    turnCount = 0;
                    turnScore.setText(String.valueOf(turnCount));

                } else if (currTurn == "Player 2" && currScore == 0) {
                    game.updateScore(currTurn, currScore);
                    score2.setText(String.valueOf(game.getP2Score()));
                    currTurn = "Player 1";
                    turnLabel.setText(String.format("%s's Turn", P1Name));
                    p2Turns += 1;
                    turnCount = 0;
                    turnScore.setText(String.valueOf(turnCount));
                } else {
                    game.updateScore(currTurn, currScore);
                }

                score1.setText(String.valueOf(game.getP1Score()));
                score2.setText(String.valueOf(game.getP2Score()));

                winner = game.isWinner(currTurn, game.getP1Score(), game.getP2Score());

                if (winner == Winner.Player1 && p1Turns == p2Turns) {
                    turnLabel.setText(String.format("%s Wins!", P1Name));
                } else if (winner == Winner.Player1 && p1Turns != p2Turns) {
                    currTurn = "Player 2";
                    turnLabel.setText(String.format("%s's Turn", P2Name));
                } else if (winner == Winner.Player2) {
                    turnLabel.setText(String.format("%s Wins!", P2Name));
                } else if (winner == Winner.Tie) {
                    turnLabel.setText("Tie!");
                }

                break;

            case R.id.endTurn:
                if (currTurn == "Player 1" && !firstTurn) {
                    p1Turns += 1;
                } else if (currTurn == "Player 2"){
                    p2Turns += 1;
                }

                if (currTurn == "Player 1") {
                    turnLabel.setText(String.format("%s's Turn", P2Name));
                    currTurn = "Player 2";
                } else {
                    turnLabel.setText(String.format("%s's Turn", P1Name));
                    currTurn = "Player 1";
                }
                turnCount = 0;
                turnScore.setText(String.valueOf(turnCount));
                firstTurn = false;

                break;

            case R.id.newGame:
                game = new PigGame();
                currTurn = "Player 1";
                p2Turns = 0;
                p1Turns = 1;
                turnCount = 0;
                firstTurn = true;

                turnScore.setText(String.valueOf(0));
                score1.setText(String.valueOf(0));
                score2.setText(String.valueOf(0));
                turnLabel.setText(String.format("%s's Turn", P1Name));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

            switch(v.getId()) {
                case R.id.p1EditText:
                    P1Name = p1EditText.getText().toString();
                    break;
                case R.id.p2EditText:
                    P2Name = p2EditText.getText().toString();
                    break;
            }

            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) throw new AssertionError();
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    public void displayImage() {
        int id = 0;

        switch(currScore)
        {
            case 0:
                id = R.drawable.die1;
                break;
            case 2:
                id = R.drawable.die2;
                break;
            case 3:
                id = R.drawable.die3;
                break;
            case 4:
                id = R.drawable.die4;
                break;
            case 5:
                id = R.drawable.die5;
                break;
            case 6:
                id = R.drawable.die6;
                break;
        }
        dieImage.setImageResource(id);
    }
}
