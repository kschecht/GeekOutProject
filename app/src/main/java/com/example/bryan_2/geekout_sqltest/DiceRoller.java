package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

    Button questionButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dice roller activity
        setContentView(R.layout.dice_roller);

        rollDice = (Button) findViewById(R.id.roll_button);

        questionButton = (Button) findViewById(R.id.questionButton);

        diceImage = (ImageView) findViewById(R.id.dice_Image);

        myView = this.getWindow().getDecorView();

        randomNumber = new Random();


        rollDice.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                // TODO Change dice number if we need more than 5 categories
                numberGenerated = randomNumber.nextInt(5)+1;

                // Games
                if(numberGenerated==1) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_one);
                    myView.setBackgroundResource(R.color.games);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.GAMES);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.games));
                            startActivity(sendIntent);
                        }
                    });

                }
                // Sci Fi
                else if(numberGenerated==2) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_two);
                    myView.setBackgroundResource(R.color.scifi);


                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.SCI_FI);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.scifi));
                            startActivity(sendIntent);
                        }
                    });


                }
                // Fantasy
                else if(numberGenerated==3) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_three);
                    myView.setBackgroundResource(R.color.fantasy);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.FANTASY);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.fantasy));
                            startActivity(sendIntent);
                        }
                    });
                }

                // Miscellaneous
                else if(numberGenerated==4) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_four);
                    myView.setBackgroundResource(R.color.miscellaneous);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.MISCELLANEOUS);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.miscellaneous));
                            startActivity(sendIntent);
                        }
                    });
                }

                // Comic Books
                else if(numberGenerated==5) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_five);
                    myView.setBackgroundResource(R.color.comicbooks);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, QuestionActivity.class);
                            sendIntent.putExtra(QuestionActivity.INTENT_TAG, QuestionActivity.COMICS);
                            sendIntent.putExtra(QuestionActivity.INTENT_COLOR, String.valueOf(R.color.comicbooks));
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
//
//                            startActivity(sendIntent);
//                        }
//                    });
//                }

            }

        });





    }


}
