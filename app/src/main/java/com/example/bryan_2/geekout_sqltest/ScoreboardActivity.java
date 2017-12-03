package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kschechter on 12/1/2017.
 */

public class ScoreboardActivity extends Activity {
    private TextView team1Score;
    private TextView team2Score;
    private TextView team3Score;
    private TextView team4Score;
    private TextView team5Score;

    private int team1points;
    private int team2points;
    private int team3points;
    private int team4points;
    private int team5points;

    private SharedPreferences scoreRoundsPrefs;

    private boolean foundWinner;
    private boolean donePressed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        team1Score = findViewById(R.id.team1Score);
        team2Score = findViewById(R.id.team2Score);
        team3Score = findViewById(R.id.team3Score);
        team4Score = findViewById(R.id.team4Score);
        team5Score = findViewById(R.id.team5Score);
        
        this.getWindow().getDecorView().setBackgroundResource(R.color.geekout);

        scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);

        foundWinner = false;
        donePressed = false;

        // Get the simple score values in before we check for a winner.  Strings might change after that
        team1points = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM1_SCORE, Integer.MIN_VALUE);
        String team1ScoreString = String.valueOf(team1points);
        team1Score.setText(team1ScoreString);

        team2points = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM2_SCORE, Integer.MIN_VALUE);
        String team2ScoreString = String.valueOf(team2points);
        team2Score.setText(team2ScoreString);

        team3points = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, Integer.MIN_VALUE);
        if (team3points != Integer.MIN_VALUE)
        {
            String team3ScoreString = String.valueOf(team3points);
            team3Score.setText(team3ScoreString);
        }

        team4points = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, Integer.MIN_VALUE);
        if (team4points != Integer.MIN_VALUE)
        {
            String team4ScoreString = String.valueOf(team4points);
            team4Score.setText(team4ScoreString);
        }

        team5points = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, Integer.MIN_VALUE);
        if (team5points != Integer.MIN_VALUE)
        {
            String team5ScoreString = String.valueOf(team5points);
            team5Score.setText(team5ScoreString);
        }

        check_for_winner();

        final Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foundWinner)
                {
                    Intent startScreen = new Intent(ScoreboardActivity.this, StartMenu.class);
                    startActivity(startScreen);
                    return;
                }

                if (getIntent().getBooleanExtra(NamingActivity.FINISHED_ROUND, false)) {
                    if (!donePressed) {
                        donePressed = true;
                        final SharedPreferences.Editor scoreRoundsEditor = scoreRoundsPrefs.edit();
                        // increment team turn
                        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM_TURN, -1) == numTeams()) {
                            scoreRoundsEditor.putInt(AddTeamsActivity.TEAM_TURN, 1);
                        } else {
                            scoreRoundsEditor.putInt(AddTeamsActivity.TEAM_TURN,
                                    scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM_TURN, -1) + 1);
                        }
                        scoreRoundsEditor.apply();
                        Intent nextQuestion = new Intent(ScoreboardActivity.this, DiceRoller.class);
                        nextQuestion.putExtra(AddTeamsActivity.NUM_TEAMS, String.valueOf(numTeams()));
                        startActivity(nextQuestion);
                    }
                } else {
                    finish();
                }
            }
        });
    }

    // Search down from five to find the highest team playing.  Use max integer value as default
    int numTeams()
    {
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, Integer.MIN_VALUE) != Integer.MIN_VALUE)
        {
            int test = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, Integer.MIN_VALUE);
            return 5;
        }

        else if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, Integer.MIN_VALUE) != Integer.MIN_VALUE)
        {
            int test = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, Integer.MIN_VALUE);
            return 4;
        }

        else if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, Integer.MIN_VALUE) != Integer.MIN_VALUE)
        {
            int test = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, Integer.MIN_VALUE);
            return 3;
        }

        // Min is 2 teams
        return 2;
    }

    // Checks for winner based on round mode, round number, and current scores
    // Updates the user-facing string of the winner
    void check_for_winner()
    {
        final SharedPreferences settingsPrefs = getSharedPreferences(QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);

        ArrayList<TextView> winnerTextViews = new ArrayList<TextView>();
        int winnerScore = 0;

        if (settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1) == QuestionActivity.ROUND_MODE)
        {
            if (scoreRoundsPrefs.getInt(AddTeamsActivity.ROUNDS_FINISHED, 0) >=
                    settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, Integer.MAX_VALUE))
            {
                winnerScore = team1points;
                foundWinner = true;

                // find winning # of points
                if (team2points > winnerScore)
                {
                    winnerScore = team2points;
                }

                if (team3points > winnerScore)
                {
                    winnerScore = team3points;
                }

                if (team4points > winnerScore)
                {
                    winnerScore = team4points;
                }

                if (team5points > winnerScore)
                {
                    winnerScore = team5points;
                }

                // make all teams with that number of points winners
                if (team1points == winnerScore) {
                    winnerTextViews.add(team1Score);
                }
                if (team2points == winnerScore) {
                    winnerTextViews.add(team2Score);
                }
                if (team3points == winnerScore) {
                    winnerTextViews.add(team3Score);
                }
                if (team4points == winnerScore) {
                    winnerTextViews.add(team4Score);
                }
                if (team5points == winnerScore) {
                    winnerTextViews.add(team5Score);
                }
            }
        }

        else // Points mode
        {

            int target = settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1);

            // NOTE: this code assumes point target won't change mid-game
            if (team1points >= target) {
                winnerScore = team1points;
                foundWinner = true;
            } else if (team2points >= target) {
                if (team2points > winnerScore)
                    winnerScore = team2points;
                foundWinner = true;
            } else if (team3points >= target) {
                if (team3points > winnerScore)
                    winnerScore = team3points;
                foundWinner = true;
            } else if (team4points >= target) {
                if (team4points > winnerScore)
                    winnerScore = team4points;
                foundWinner = true;
            } else if (team5points >= target) {
                if (team5points > winnerScore)
                    winnerScore = team5points;
                foundWinner = true;
            }

            // make all teams with that number of points winners
            if (team1points == winnerScore) {
                winnerTextViews.add(team1Score);
            }
            if (team2points == winnerScore) {
                winnerTextViews.add(team2Score);
            }
            if (team3points == winnerScore) {
                winnerTextViews.add(team3Score);
            }
            if (team4points == winnerScore) {
                winnerTextViews.add(team4Score);
            }
            if (team5points == winnerScore) {
                winnerTextViews.add(team5Score);
            }
        }

        if (foundWinner && winnerTextViews.size() > 0)
        {
            Toast.makeText(ScoreboardActivity.this, "Game Over",
                    Toast.LENGTH_SHORT).show();
            for (TextView wTV : winnerTextViews) {
                wTV.setText("Winner! (" + winnerScore + ")");
            }
        }
    }

    /*
        To prevent issues from pressing the back button
     */
    @Override
    public void onBackPressed() {

    }
}
