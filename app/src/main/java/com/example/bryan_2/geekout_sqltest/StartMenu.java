package com.example.bryan_2.geekout_sqltest;

import android.content.Intent;
import android.content.SharedPreferences;
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
    View myView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        settingsButton = (Button) findViewById(R.id.settings_button);
        questionButton = (Button) findViewById(R.id.start_Button);
        myView = this.getWindow().getDecorView();

        myView.setBackgroundResource(R.color.geekout);

        final SharedPreferences settingsPrefs = getSharedPreferences
                (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // initialize settings to defaults if they haven't been initialized yet
        if (settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
        }
        if (settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, QuestionActivity.DEFAULT_POINTS);
        }
        if (settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, QuestionActivity.DEFAULT_ROUNDS);
        }
        settingsPrefsEditor.apply();


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
    /*
            To prevent issues from pressing the back button
         */
    @Override
    public void onBackPressed() {

    }

}
