package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import java.util.Random;



public class StartMenu extends Activity {
    Button settingsButton;


    Button questionButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dice roller activity
        setContentView(R.layout.start_menu);

        settingsButton = (Button) findViewById(R.id.settings_button);
        questionButton = (Button) findViewById(R.id.start_Button);


        settingsButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                Intent sendIntent = new Intent(StartMenu.this, SettingsActivity.class);

                startActivity(sendIntent);

            }

        });

        questionButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                Intent sendIntent = new Intent(StartMenu.this, AddTeamsActivity.class);

                startActivity(sendIntent);

            }

        });





    }


}
