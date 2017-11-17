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

                numberGenerated = randomNumber.nextInt(6)+1;

                if(numberGenerated==1) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_one);
                    myView.setBackgroundResource(R.color.red);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("redColor", R.color.red);
                            startActivity(sendIntent);
                        }
                    });

                }
                else if(numberGenerated==2) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_two);
                    myView.setBackgroundResource(R.color.yellow);


                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("yellow", R.color.yellow);
                            startActivity(sendIntent);
                        }
                    });


                }
                else if(numberGenerated==3) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_three);
                    myView.setBackgroundResource(R.color.green);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("green", R.color.green);

                            startActivity(sendIntent);
                        }
                    });
                }

                else if(numberGenerated==4) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_four);
                    myView.setBackgroundResource(R.color.teal);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("teal", R.color.teal);
                            startActivity(sendIntent);
                        }
                    });
                }

                else if(numberGenerated==5) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_five);
                    myView.setBackgroundResource(R.color.pink);
                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("pink", R.color.pink);

                            startActivity(sendIntent);
                        }
                    });
                }

                else if(numberGenerated==6) {
                    diceImage.setImageResource(R.drawable.dice_six_faces_six);
                    myView.setBackgroundResource(R.color.blue);

                    questionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent sendIntent = new Intent(DiceRoller.this, MainActivity.class);
                            sendIntent.putExtra("blue", R.color.blue);

                            startActivity(sendIntent);
                        }
                    });
                }

            }

        });





    }


}
