package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent callingIntent = getIntent();
        question = callingIntent.getStringExtra(BiddingActivity.QUESTION_KEY);
        namingTeam = callingIntent.getStringExtra(BiddingActivity.NAMING_TEAM_KEY);
        goal = callingIntent.getIntExtra(BiddingActivity.TARGET_KEY, -1);
        currentScore = 0;

        if (question == null || namingTeam == null || goal == -1)
        {
            //TODO what to do on error like this?
            return;
        }

        timeLimit = 5; // TODO
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
        subtractButton = findViewById(R.id.decrementScoreButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScore++;

                if (currentScore >= goal)
                {
                    // TODO go to victory
                    timer.cancel();
                    setResult(1);
                    finish();
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
                timerView.setText(timerText + l/1000 + " seconds");
            }

            @Override
            public void onFinish() {
                //TODO go to failure
                setResult(-1);
                finish();
            }
        };

        timer.start();
    }

    /*
        To prevent issues from pressing the back button
     */
    @Override
    public void onBackPressed() {

    }
}
