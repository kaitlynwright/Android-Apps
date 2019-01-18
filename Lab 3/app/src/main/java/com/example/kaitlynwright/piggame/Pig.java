package com.example.kaitlynwright.piggame;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import android.content.SharedPreferences;
import android.view.Menu;
import android.content.Intent;
import android.widget.Toast;

public class Pig extends AppCompatActivity implements OnEditorActionListener, OnClickListener {

    PigGame game = new PigGame();

    //widgets
    private EditText p1EditText;
    private EditText p2EditText;
    private TextView score1;
    private TextView score2;
    private TextView turnLabel;
    private TextView turnScore;
    private Button rollDie;
    private Button endTurn;
    private Button newGame;
    private ImageView dieImage1;
    private ImageView dieImage2;
    private ImageView dieImage3;

    //Game Variables
    private int currScore = 0;
    private int turnCount = 0;
    private String currTurn = "Player 1";
    private String P1Name;
    private String P2Name;
    private Winner winner;

    //Preference Variables
    private boolean pref_showImages = true;
    private int pref_maxScore = 100;
    private int pref_dieSides = 6;
    private int pref_numDies = 1;

    //Shared Preferences
    private SharedPreferences savedValues;

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
        dieImage1 = (ImageView) findViewById(R.id.dieImage1);
        dieImage2 = (ImageView) findViewById(R.id.dieImage2);
        dieImage3 = (ImageView) findViewById(R.id.dieImage3);


        p1EditText.setOnEditorActionListener(this);
        p2EditText.setOnEditorActionListener(this);
        rollDie.setOnClickListener(this);
        endTurn.setOnClickListener(this);
        newGame.setOnClickListener(this);

        //savedValues = getSharedPreferences("Shared Preferences", MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        savedValues = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("currScore",currScore);
        editor.putInt("turnCount", turnCount);
        editor.putString("currTurn", currTurn);
        editor.putString("P1Name", P1Name);
        editor.putString("P2Name", P2Name);
        editor.putInt("P1Score", game.getP1Score());
        editor.putInt("P2Score", game.getP2Score());

        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        currScore = savedValues.getInt("currScore", 0);
        turnCount = savedValues.getInt("turnCount", 0);
        currTurn = savedValues.getString("currTurn", "Player 1");
        P1Name = savedValues.getString("P1Name", " ");
        P2Name = savedValues.getString("P2Name", " ");
        game.setP1Score(savedValues.getInt("P1Score", 0));
        game.setP2Score(savedValues.getInt("P2Score", 0));

        p1EditText.setText(P1Name);
        p2EditText.setText(P2Name);
        score1.setText(String.valueOf(game.getP1Score()));
        score2.setText(String.valueOf(game.getP2Score()));
        turnScore.setText(String.valueOf(turnCount));

        if (currTurn.equals("Player 1")) {
            turnLabel.setText(String.format("%s's Turn", P1Name));
        } else if (currTurn.equals("Player 2")) {
            turnLabel.setText(String.format("%s's Turn", P2Name));
        }

        // Preferences
        pref_maxScore = Integer.parseInt(savedValues.getString("pref_maxScore", "100"));
        pref_showImages = savedValues.getBoolean("pref_show_images", true);
        pref_numDies = Integer.parseInt(savedValues.getString("pref_numDies", "1"));
        pref_dieSides = Integer.parseInt(savedValues.getString("pref_dieSides", "6"));

        game.setMaxScore(pref_maxScore);
        game.setDieSides(pref_dieSides);

        if (!pref_showImages) {
            dieImage1.setVisibility(View.INVISIBLE);
            dieImage2.setVisibility(View.INVISIBLE);
            dieImage3.setVisibility(View.INVISIBLE);
            dieImage2.setVisibility(View.VISIBLE);

            if (pref_numDies == 2 || pref_numDies == 3) dieImage1.setVisibility(View.VISIBLE);
            else dieImage1.setVisibility(View.INVISIBLE);

            if (pref_numDies == 3) dieImage3.setVisibility(View.VISIBLE);
            else dieImage3.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rollDie:
                currScore = game.takeTurn();
                if (pref_showImages) displayImage(1);
                turnCount += currScore;
                turnScore.setText(String.valueOf(turnCount));

                if ((pref_numDies == 2 || pref_numDies == 3) && currScore != 0) {
                    currScore = game.takeTurn();
                    if (pref_showImages) displayImage(2);
                    turnCount += currScore;
                    turnScore.setText(String.valueOf(turnCount));
                }

                if (pref_numDies == 3 && currScore != 0) {
                    currScore = game.takeTurn();
                    if (pref_showImages) displayImage(3);
                    turnCount += currScore;
                    turnScore.setText(String.valueOf(turnCount));
                }

                // Check for a roll of 1, update score
                if (currTurn.equals("Player 1") && currScore == 0) {
                    game.updateScore(currTurn, currScore);
                    score1.setText(String.valueOf(game.getP1Score()));
                    currTurn = "Player 2";

                    turnLabel.setText(String.format("%s's Turn", P2Name));
                    turnCount = 0;
                    turnScore.setText(String.valueOf(0));

                } else if (currTurn.equals("Player 2") && currScore == 0) {
                    game.updateScore(currTurn, currScore);
                    score2.setText(String.valueOf(game.getP2Score()));
                    currTurn = "Player 1";

                    turnLabel.setText(String.format("%s's Turn", P1Name));
                    turnCount = 0;
                    turnScore.setText(String.valueOf(0));
                } else {
                    game.updateScore(currTurn, turnCount);
                }

                score1.setText(String.valueOf(game.getP1Score()));
                score2.setText(String.valueOf(game.getP2Score()));

                // Check for Winner
                winner = game.isWinner(currTurn, game.getP1Score(), game.getP2Score());

                if (winner == Winner.Player1) {
                    turnLabel.setText(String.format("%s Wins!", P1Name));
                } else if (winner == Winner.Player2) {
                    turnLabel.setText(String.format("%s Wins!", P2Name));
                } else if (winner == Winner.Tie) {
                    turnLabel.setText("Tie!");
                }

                break;

            case R.id.endTurn:
                if (currTurn.equals("Player 1")) {
                    turnLabel.setText(String.format("%s's Turn", P2Name));
                    currTurn = "Player 2";
                } else {
                    turnLabel.setText(String.format("%s's Turn", P1Name));
                    currTurn = "Player 1";
                }
                turnCount = 0;
                turnScore.setText(String.valueOf(0));

                break;

            case R.id.newGame:
                game = new PigGame();
                game.setMaxScore(pref_maxScore);

                currTurn = "Player 1";
                turnCount = 0;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                Toast.makeText(this, "About Coming Soon", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayImage(int dieNum) {
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

        if (dieNum == 1) dieImage2.setImageResource(id);
        if (dieNum == 2) dieImage1.setImageResource(id);
        if (dieNum == 3) dieImage3.setImageResource(id);
    }
}
