package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kschechter on 12/1/2017.
 */

public class ScoreboardActivity extends Activity {
    private TextView team1Score;
    private TextView team2Score;
    private TextView team3Score;
    private TextView team4Score;
    private TextView team5Score;

    private SharedPreferences scoreRoundsPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        team1Score = findViewById(R.id.team1Score);
        team2Score = findViewById(R.id.team2Score);
        team3Score = findViewById(R.id.team3Score);
        team4Score = findViewById(R.id.team4Score);
        team5Score = findViewById(R.id.team5Score);

        scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
        final SharedPreferences settingsPrefs = getSharedPreferences(QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);

        // TODO score can go negative.  -1 not an acceptable default.
        String team1ScoreString = String.valueOf(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM1_SCORE, -1));
        team1Score.setText(team1ScoreString);
        String team2ScoreString = String.valueOf(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM2_SCORE, -1));
        team2Score.setText(team2ScoreString);
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, -1) != -1)
        {
            String team3ScoreString = String.valueOf(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, -1));
            team3Score.setText(team3ScoreString);
        }
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, -1) != -1)
        {
            String team4ScoreString = String.valueOf(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, -1));
            team4Score.setText(team4ScoreString);
        }
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, -1) != -1)
        {
            String team5ScoreString = String.valueOf(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, -1));
            team5Score.setText(team5ScoreString);
        }

        final Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO If a team has won, go back to the start screen

                Intent nextQuestion = new Intent(ScoreboardActivity.this, DiceRoller.class);
                nextQuestion.putExtra(AddTeamsActivity.NUM_TEAMS, String.valueOf(numTeams()));
                startActivity(nextQuestion);
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
}
