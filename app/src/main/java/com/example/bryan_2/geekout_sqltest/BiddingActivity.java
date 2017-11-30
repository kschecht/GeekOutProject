package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


public class BiddingActivity extends Activity {
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
    int firstBidder = 0; //< Increments every round

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


        // TODO get the list of teams, current question, question minimum bet... somewhere
        // Until we implement a way to pass that stuff around, use hardcoded debug values
        allTeams = new ArrayList<String>();
        allTeams.add("Team 1");
        allTeams.add("Team 2");
        allTeams.add("Team 3");

        // Everyone starts with a score of 0
        score = new HashMap<String, Integer>();
        for (int i = 0; i < allTeams.size(); i++)
        {
            score.put(allTeams.get(i), 0);
        }

        questionView = findViewById(R.id.biddingQuestionView);

        startNewRound();

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leadingTeam = biddingTeam;
                previousBid = currentBid;
                // Next team needs to bet at least one more
                currentBid++;
                biddingTeam = getNextTeam();

                // TODO "pass phone" pop-up
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

                // This was the last team to pass.  Current leader wins the bid
                if (biddingTeams.size() == 2)
                {
                    // TODO "pass the phone" pop-up
                    // Create a new AlertDialogFragment
                    mDialog = PlayerChangeDialogFragment.newInstance();
                    // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                    Bundle alertMessageBundle = new Bundle();
                    alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                            "Pass the phone to "+leadingTeam);
                    mDialog.setArguments(alertMessageBundle);
                    // Show AlertDialogFragment
                    mDialog.show(getFragmentManager(), "Alert");

                    Intent namingIntent = new Intent(BiddingActivity.this, NamingActivity.class);
                    namingIntent.putExtra(QUESTION_KEY, question);
                    namingIntent.putExtra(NAMING_TEAM_KEY, leadingTeam);
                    namingIntent.putExtra(TARGET_KEY, previousBid);
                    startActivityForResult(namingIntent, BEGIN_NAMING_REQUEST);
                    return;
                }
                currentBid = previousBid + 1;
                String nextTeam = getNextTeam();
                biddingTeams.remove(biddingTeam); // This team passed, so they're no longer part of the bidding loop
                biddingTeam = nextTeam;

                // TODO "pass the phone" pop-up
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

                // Can't bid less than the minimum for the round
                if (currentBid < minimumBid)
                    currentBid = minimumBid;
                // Can't bid less that the previous team
                // TODO Why is previousBid != minimumBid there?
                // TODO leads to a weird case: Team 1 bet minimum, team 2 can then decrease their bid
                // to the minimum.
                else if (currentBid <= previousBid && previousBid != minimumBid)
                    currentBid = previousBid+1;

                updateViews();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // If we don't recognize the request code, do nothing
        if (requestCode == BEGIN_NAMING_REQUEST)
        {
            if (resultCode > 0)
            {
                // Leading team shouldn't change between starting NamingActivity and getting its resutls
                score.put(leadingTeam, score.get(leadingTeam) + 1);
            }

            else if (resultCode < 0)
            {
                // The score is allowed to go negative.  This is not a bug.
                // Leading team shouldn't change between starting NamingActivity and getting its resutls
                score.put(leadingTeam, score.get(leadingTeam) - 2);
            }

            startNewRound();
        }
    }

    void startNewRound()
    {
        minimumBid = 1;
        previousBid = minimumBid;
        currentBid = minimumBid;

        biddingTeams.clear();
        biddingTeams.addAll(allTeams);

        biddingTeam = biddingTeams.get(firstBidder++);
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
}
