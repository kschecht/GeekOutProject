package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by colin on 11/19/17.
 */

public class NamingActivity extends Activity {
    String timerText;

    TextView questionView;
    TextView teamNameView;
    TextView scoreView;
    TextView timerView;
    ImageButton addButton;
    ImageButton subtractButton;
    CountDownTimer timer;

    String question;
    String namingTeam;
    int goal;
    int currentScore;
    int timeLimit; // in seconds

    private DialogFragment mDialog;

    public static final String FINISHED_ROUND = "finshedRound";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent callingIntent = getIntent();
        question = callingIntent.getStringExtra(BiddingActivity.QUESTION_KEY);
        namingTeam = callingIntent.getStringExtra(BiddingActivity.NAMING_TEAM_KEY);
        goal = callingIntent.getIntExtra(BiddingActivity.TARGET_KEY, -1);
        currentScore = 0;

        this.getWindow().getDecorView().setBackgroundResource(R.color.geekout);

        final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
        final SharedPreferences.Editor scoreEditor = scoreRoundsPrefs.edit();

        if (question == null || namingTeam == null || goal == -1)
        {
            return;
        }

        timeLimit = 20 * goal; // TODO - This is the time limit when I play usually - Colin
        timerText = getString(R.string.timeRemaining);

        setContentView(R.layout.activity_naming);
        questionView = findViewById(R.id.namingQuestionView);
        questionView.setText(question);

        teamNameView = findViewById(R.id.namingTeamView);
        teamNameView.setText(namingTeam);

        scoreView = findViewById(R.id.currentScoreView);
        scoreView.setText(Integer.toString(currentScore));

        timerView = findViewById(R.id.namingTimerView);

        addButton = findViewById(R.id.incrementScoreButton);
        addButton.setImageResource(android.R.drawable.ic_input_add); // TODO figure out why this isn't working from xml
        subtractButton = findViewById(R.id.decrementScoreButton);
        subtractButton.setImageResource(android.R.drawable.ic_input_delete); // TODO ditto

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScore++;

                if (currentScore == goal)
                {
                    timer.cancel();
                    scoreEditor.putInt(currTeamScoreKey(), scoreRoundsPrefs.getInt(currTeamScoreKey(), 0) + 1);
                    scoreEditor.putInt(AddTeamsActivity.ROUNDS_FINISHED, scoreRoundsPrefs.getInt(AddTeamsActivity.ROUNDS_FINISHED, 0) + 1);
                    scoreEditor.commit();
                    Intent scoreboard = new Intent(NamingActivity.this, ScoreboardActivity.class);
                    scoreboard.putExtra(FINISHED_ROUND, true);
                    startActivity(scoreboard);
                }
                scoreView.setText(Integer.toString(currentScore));
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScore--;
                if (currentScore <= 0)
                    currentScore =0;

                scoreView.setText(Integer.toString(currentScore));
            }
        });

        timer = new CountDownTimer(timeLimit * 1000, 1000) {

            @Override
            public void onTick(long l) {

                long timer = l/1000;
                timerView.setText(timerText + timer + " seconds");

                if(timer<5  && timer > 0){
                    MediaPlayer mPlayer= MediaPlayer.create(NamingActivity.this, R.raw.countdown);

                    mPlayer.start();
                }
            }


            @Override
            public void onFinish() {

                scoreEditor.putInt(currTeamScoreKey(), scoreRoundsPrefs.getInt(currTeamScoreKey(), 0) -2);
                scoreEditor.putInt(AddTeamsActivity.ROUNDS_FINISHED, scoreRoundsPrefs.getInt(AddTeamsActivity.ROUNDS_FINISHED, 0) + 1);
                scoreEditor.commit();
                Intent scoreboard = new Intent(NamingActivity.this, ScoreboardActivity.class);
                startActivity(scoreboard);
            }
        };

        timer.start();

        // TODO naming team shouldn't be holding timer while naming
        // Create a new AlertDialogFragment
        mDialog = PlayerChangeDialogFragment.newInstance();
        // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
        Bundle alertMessageBundle = new Bundle();
        alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                "Pass the phone to " + namingTeam);
        mDialog.setArguments(alertMessageBundle);
        // Show AlertDialogFragment
        mDialog.show(getFragmentManager(), "Alert");
    }

    /*
        To prevent issues from pressing the back button
     */
    @Override
    public void onBackPressed() {

    }

    // Get the string key for the current team's score pref based on team name
    String currTeamScoreKey()
    {
        if (namingTeam.equals("Team 1"))
        {
            return AddTeamsActivity.TEAM1_SCORE;
        }
        if (namingTeam.equals("Team 2"))
        {
            return AddTeamsActivity.TEAM2_SCORE;
        }
        if (namingTeam.equals("Team 3"))
        {
            return AddTeamsActivity.TEAM3_SCORE;
        }
        if (namingTeam.equals("Team 4"))
        {
            return AddTeamsActivity.TEAM4_SCORE;
        }
        if (namingTeam.equals("Team 5"))
        {
            return AddTeamsActivity.TEAM5_SCORE;
        }
        return "";
    }
}
