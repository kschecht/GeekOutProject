package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import java.util.Random;



public class AddTeamsActivity extends Activity {
    Button addTeam;
    Button removeTeam;
    Button done;
    View myView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teams);
        myView = this.getWindow().getDecorView();

        myView.setBackgroundResource(R.color.geekout);


        done = (Button) findViewById(R.id.doneButton);


        done.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                Intent sendIntent = new Intent(AddTeamsActivity.this, DiceRoller.class);

                startActivity(sendIntent);

            }

        });




    }


}
