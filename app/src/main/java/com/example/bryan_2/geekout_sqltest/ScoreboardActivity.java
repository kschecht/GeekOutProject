package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        team1Score = findViewById(R.id.team1Score);
        team2Score = findViewById(R.id.team2Score);
        team3Score = findViewById(R.id.team3Score);
        team4Score = findViewById(R.id.team4Score);
        team5Score = findViewById(R.id.team5Score);

        final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
        team1Score.setText(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM1_SCORE, -1));
        team2Score.setText(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM2_SCORE, -1));
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, -1) != -1)
            team3Score.setText(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM3_SCORE, -1));
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, -1) != -1)
            team4Score.setText(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM4_SCORE, -1));
        if (scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, -1) != -1)
            team5Score.setText(scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM5_SCORE, -1));

        final Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO based on why this was called, go to different activities

                finish();
            }
        });
    }
}
