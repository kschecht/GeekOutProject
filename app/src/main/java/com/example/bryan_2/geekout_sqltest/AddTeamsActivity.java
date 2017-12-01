package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
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
    Button addTeam;
    Button removeTeam;
    Button done;
    View myView;
    private ImageButton decrementTeamButton;
    private ImageButton incrementTeamButton;
    private TextView numTeams;

    public static final String NUM_TEAMS = "numTeams";

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

                Intent sendIntent = new Intent(AddTeamsActivity.this, DiceRoller.class);
                Log.i("TEAM", numTeams.getText().toString());
                sendIntent.putExtra(NUM_TEAMS, numTeams.getText().toString());

                startActivity(sendIntent);

            }

        });




    }


}
