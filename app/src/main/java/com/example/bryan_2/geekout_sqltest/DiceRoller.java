package com.example.bryan_2.geekout_sqltest;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.DialogFragment;

import java.util.Random;

/**
 * Created by narjitsingh on 11/17/17.
 */

public class DiceRoller extends AppCompatActivity {
    Button rollDice;
    ImageView diceImage;
    Random randomNumber;
    int numberGenerated;
    View myView;
    private DialogFragment mDialog;

    Button questionButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dice roller activity
        setContentView(R.layout.dice_roller);

        final SharedPreferences scoreRoundsPrefs = getSharedPreferences
                (AddTeamsActivity.SCORE_ROUNDS, MODE_PRIVATE);
        
        // TODO only display if DiceRoller is not being called as a result of "ReRoll"
        // Create a new AlertDialogFragment
        mDialog = PlayerChangeDialogFragment.newInstance();
        // method for passing text from https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
        Bundle alertMessageBundle = new Bundle();
        alertMessageBundle.putString(PlayerChangeDialogFragment.ALERT_MESSAGE,
                "Pass the phone to Team "+scoreRoundsPrefs.getInt(AddTeamsActivity.TEAM_TURN, 0));
        mDialog.setArguments(alertMessageBundle);
        // Show AlertDialogFragment
        mDialog.show(getFragmentManager(), "Alert");


        rollDice = (Button) findViewById(R.id.roll_button);

        questionButton = (Button) findViewById(R.id.questionButton);

        diceImage = (ImageView) findViewById(R.id.dice_Image);

        myView = this.getWindow().getDecorView();
        myView.setBackgroundResource(R.color.geekout);

        randomNumber = new Random();

        // TODO handle user clicking question without rolling first

        rollDice.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                TextView games = (TextView)findViewById(R.id.cat_name);

                // TODO Change dice number if we need more than 5 categories
                numberGenerated = randomNumber.nextInt(5)+1;

                // Games
                if(numberGenerated==1) {
                    games.setText("Games");
                    diceImage.setImageResource(R.drawable.dice_games);
                    myView.setBackgroundResource(R.color.games);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.GAMES);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.games));
                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
                            startActivity(sendIntent);
                        }
                    });

                }
                // Sci Fi
                else if(numberGenerated==2) {
                    games.setText("Science-Fiction");
                    diceImage.setImageResource(R.drawable.dice_scifi);
                    myView.setBackgroundResource(R.color.scifi);


                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.SCI_FI);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.scifi));
                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
                            startActivity(sendIntent);
                        }
                    });


                }
                // Fantasy
                else if(numberGenerated==3) {
                    games.setText("Fantasy");
                    diceImage.setImageResource(R.drawable.dice_fantasy);
                    myView.setBackgroundResource(R.color.fantasy);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.FANTASY);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.fantasy));
                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
                            startActivity(sendIntent);
                        }
                    });
                }

                // Miscellaneous
                else if(numberGenerated==4) {
                    games.setText("Miscellaneous");
                    diceImage.setImageResource(R.drawable.dice_misc);
                    myView.setBackgroundResource(R.color.miscellaneous);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.MISCELLANEOUS);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.miscellaneous));
                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
                            startActivity(sendIntent);

                        }
                    });
                }

                // Comic Books
                else if(numberGenerated==5) {
                    games.setText("Comic Books");
                    diceImage.setImageResource(R.drawable.dice_comics);
                    myView.setBackgroundResource(R.color.comicbooks);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.COMICS);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.comicbooks));
                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
                            startActivity(sendIntent);
                        }
                    });
                }

                // TODO implement if we need more categoreis
//                else if(numberGenerated==6) {
//                    diceImage.setImageResource(R.drawable.dice_six_faces_six);
//                    myView.setBackgroundResource(R.color.pink);
//
//                    questionButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v)
//                        {
//                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
//                            sendIntent.putExtra("pink", R.color.pink);
//                            sendIntent.putExtra(AddTeamsActivity.NUM_TEAMS, getIntent().getStringExtra(AddTeamsActivity.NUM_TEAMS));
//                            startActivity(sendIntent);
//                        }
//                    });
//                }

            }

        });





    }


}
