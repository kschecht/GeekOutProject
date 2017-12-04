package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.Activity;
import java.util.Random;
import android.widget.TextView;



public class AddTeamsActivity extends Activity {
    Button done;
    View myView;
    private ImageButton decrementTeamButton;
    private ImageButton incrementTeamButton;
    private TextView numTeams;

    public static final String NUM_TEAMS = "numTeams";
    public static final String SCORE_ROUNDS = "scoreRounds";
    public static final String TEAM1_SCORE = "team1Score";
    public static final String TEAM2_SCORE = "team2Score";
    public static final String TEAM3_SCORE = "team3Score";
    public static final String TEAM4_SCORE = "team4Score";
    public static final String TEAM5_SCORE = "team5Score";
    public static final String ROUNDS_FINISHED = "numRoundsFinished";
    public static final String TEAM_TURN = "teamTurn";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teams);
        myView = this.getWindow().getDecorView();

        myView.setBackgroundResource(R.color.geekout);

        numTeams = findViewById(R.id.currentTeamNum);
        incrementTeamButton = findViewById(R.id.incrementTeamButton);
        incrementTeamButton.setImageResource(android.R.drawable.ic_input_add);
        decrementTeamButton = findViewById(R.id.decrementTeamButton);
        decrementTeamButton.setImageResource(android.R.drawable.ic_input_delete);


        done = (Button) findViewById(R.id.doneButton);

        decrementTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numTeams.getText().toString()) > 2) {
                    String newNum = String.valueOf(Integer.parseInt(numTeams.getText().toString()) - 1);
                    numTeams.setText(newNum);
                }
            }
        });

        incrementTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(numTeams.getText().toString()) < 5) {
                    String newNum = String.valueOf(Integer.parseInt(numTeams.getText().toString()) + 1);
                    numTeams.setText(newNum);
                }
            }
        });


        done.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                        (SCORE_ROUNDS, MODE_PRIVATE);
                final SharedPreferences.Editor sRPrefsEditor = scoreRoundsPrefs.edit();
                // We rely on lack of a score entry to tell us when a team is not playing, so make sure the scores are cleared
                sRPrefsEditor.clear();
                sRPrefsEditor.putInt(ROUNDS_FINISHED, 0);
                sRPrefsEditor.putInt(TEAM1_SCORE, 0);
                sRPrefsEditor.putInt(TEAM2_SCORE, 0);
                int intNumTeams = Integer.parseInt(numTeams.getText().toString());
                if (intNumTeams >= 3) {
                    sRPrefsEditor.putInt(TEAM3_SCORE, 0);
                    if (intNumTeams >= 4) {
                        sRPrefsEditor.putInt(TEAM4_SCORE, 0);
                        if (intNumTeams == 5) {
                            sRPrefsEditor.putInt(TEAM5_SCORE, 0);
                        }
                    }
                }
                sRPrefsEditor.putInt(TEAM_TURN, 1);
                sRPrefsEditor.apply();

                Intent sendIntent = new Intent(AddTeamsActivity.this, DiceRoller.class);
                Log.i("TEAM", numTeams.getText().toString());
                sendIntent.putExtra(NUM_TEAMS, numTeams.getText().toString());

                startActivity(sendIntent);

            }

        });




    }


}
