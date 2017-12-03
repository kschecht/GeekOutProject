package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by colin on 11/19/17.
 */


public class BiddingActivity extends AppCompatActivity {
    public static final String QUESTION_KEY = "QuestionString";
    public static final String NAMING_TEAM_KEY = "NamingTeamString";
    public static final String TARGET_KEY = "TargetString";

    public static final int BEGIN_NAMING_REQUEST = 12;

    Button bidButton;
    Button passButton;
    ImageButton incrementButton;
    ImageButton decrementButton;
    TextView teamName;
    TextView currentBidView;
    TextView questionView;
    private DialogFragment mDialog;

    // NOTE: This code is assuming that team names are unique
    HashMap<String, Integer> score;
    List<String> allTeams;
    List<String> biddingTeams = new ArrayList<String>(); //< All teams currently bidding.  As teams pass, names will be removed
    String leadingTeam; //< The team with the current accepted bid
    String biddingTeam;
    String question;
    int previousBid;
    int minimumBid;
    int currentBid;
    int firstBidder;// = 0; //< Increments every round
    boolean firstRoundOfBidding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        bidButton = findViewById(R.id.bidButton);
        passButton = findViewById(R.id.passButton);
        incrementButton = findViewById(R.id.incrementBidButton);
        incrementButton.setImageResource(android.R.drawable.ic_input_add); // TODO figure out why this isn't working from xml
        decrementButton = findViewById(R.id.decrementBidButton);
        decrementButton.setImageResource(android.R.drawable.ic_input_delete); // TODO ditto
        teamName = findViewById(R.id.teamNameView);
        currentBidView = findViewById(R.id.currentBidView);
        final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
        firstBidder = scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM_TURN, 0) - 1;


        this.getWindow().getDecorView().setBackgroundResource(R.color.geekout);

        /*
            Toolbar Settings
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        allTeams = new ArrayList<String>();
        Log.i("TEAM", "Got to here");
        Log.i("TEAM", getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));

        for (int i = 0; i < Integer.valueOf(getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS)); i++) {
            allTeams.add("Team " + (i + 1));
        }

        // Everyone starts with a score of 0
        score = new HashMap<String, Integer>();
        for (int i = 0; i < allTeams.size(); i++)
        {
            score.put(allTeams.get(i), 0);
        }

        questionView = findViewById(R.id.biddingQuestionView);

        startNewRound();

        firstRoundOfBidding = true;

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstRoundOfBidding = false;

                leadingTeam = biddingTeam;
                previousBid = currentBid;
                // Next team needs to bet at least one more
                currentBid++;
                biddingTeam = getNextTeam();

                // Create a new AlertDialogFragment
                mDialog = PlayerChangeDialogFragment.newInstance();
                // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                Bundle alertMessageBundle = new Bundle();
                alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                        "Pass the phone to "+biddingTeam);
                mDialog.setArguments(alertMessageBundle);
                // Show AlertDialogFragment
                mDialog.show(getFragmentManager(), "Alert");

                updateViews();
            }
        });

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstRoundOfBidding) {
                    // Create a new AlertDialogFragment
                    mDialog = PlayerChangeDialogFragment.newInstance();
                    // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                    Bundle alertMessageBundle = new Bundle();
                    alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                            "Team that rolled the dice cannot pass unless another team bids.");
                    mDialog.setArguments(alertMessageBundle);
                    // Show AlertDialogFragment
                    mDialog.show(getFragmentManager(), "Alert");
                } else {

                    // This was the last team to pass.  Current leader wins the bid
                    if (biddingTeams.size() == 2) {
                        Intent namingIntent = new Intent(BiddingActivity.this, NamingActivity.class);
                        namingIntent.putExtra(QUESTION_KEY, question);
                        namingIntent.putExtra(NAMING_TEAM_KEY, getNextTeam()); // "Next team" should be the only team left after the current team is passed
                        namingIntent.putExtra(TARGET_KEY, previousBid);
                        startActivity(namingIntent);
                        return;
                    }
                    currentBid = previousBid + 1;
                    String nextTeam = getNextTeam();
                    biddingTeams.remove(biddingTeam); // This team passed, so they're no longer part of the bidding loop
                    biddingTeam = nextTeam;

                    updateViews();

                    // Create a new AlertDialogFragment
                    mDialog = PlayerChangeDialogFragment.newInstance();
                    // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                    Bundle alertMessageBundle = new Bundle();
                    alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                            "Pass the phone to "+biddingTeam);
                    mDialog.setArguments(alertMessageBundle);
                    // Show AlertDialogFragment
                    mDialog.show(getFragmentManager(), "Alert");
                }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBid++;
                updateViews();
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentBid--;

                // Can't bid less that the previous team
                if (currentBid <= previousBid)
                    currentBid = previousBid+1;

                // Can't bid less than the minimum for the round
                else if (currentBid < minimumBid)
                    currentBid = minimumBid;

                updateViews();
            }
        });

    }

    void startNewRound()
    {
        previousBid = minimumBid-1;
        minimumBid = Integer.parseInt(getIntent().getStringExtra(QuestionActivity.INTENT_MIN_BID));
        currentBid = minimumBid;

        biddingTeams.clear();
        biddingTeams.addAll(allTeams);

        biddingTeam = biddingTeams.get(firstBidder);
        leadingTeam = biddingTeam; // To prevent the case where everyone passes without bidding, the first team has to bet at least the minimum

        // Don't bother holding on to this view.  We need to set it once and never interact again
        question = getIntent().getStringExtra(QuestionActivity.INTENT_BIDDING);
        questionView.setText(question);


        updateViews();
    }

    void updateViews()
    {
        teamName.setText(biddingTeam);
        currentBidView.setText(Integer.toString(currentBid));
    }

    String getNextTeam()
    {
        int index = biddingTeams.indexOf(biddingTeam) + 1;

        // We've wrapped around to the beginning of the list
        if (index >= biddingTeams.size())
            return biddingTeams.get(0);

        return biddingTeams.get(index);
    }

    /*
        To prevent issues from pressing the back button
     */
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent launchSettingsActInt = new Intent(BiddingActivity.this, SettingsActivity.class);
            startActivity(launchSettingsActInt);
        }
        if (id == R.id.action_scoreboard) {
            Intent launchScoreboardActInt = new Intent(BiddingActivity.this, ScoreboardActivity.class);
            startActivity(launchScoreboardActInt);
        }
        if (id == R.id.action_end_game) {
            final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                    (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
            final SharedPreferences settingsPrefs = getSharedPreferences
                    (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor settingsEditor = settingsPrefs.edit();
            settingsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.ROUND_MODE);
            settingsEditor.putInt(QuestionActivity.MAX_ROUNDS,
                    scoreRoundsPrefs.getInt(AddTeamsActivity.ROUNDS_FINISHED, -1));
            settingsEditor.apply();

            Intent launchScoreboardActInt = new Intent(BiddingActivity.this, ScoreboardActivity.class);
            startActivity(launchScoreboardActInt);
        }

        return super.onOptionsItemSelected(item);
    }
}
