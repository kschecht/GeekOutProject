package com.example.bryan_2.geekout_sqltest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;

/**
 * Created by kschechter on 11/15/2017.
 */

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        SharedPreferences settingsPrefs = getSharedPreferences
                (MainActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // Settings have not been initialized yet, should not happen here
        if (settingsPrefs.getAll().isEmpty()) {
            settingsPrefsEditor.putInt(MainActivity.GAME_MODE, MainActivity.POINTS_MODE);
            settingsPrefsEditor.putInt(MainActivity.MAX_POINTS, MainActivity.DEFAULT_POINTS);
            settingsPrefsEditor.putInt(MainActivity.MAX_MINUTES, MainActivity.DEFAULT_MINUTES);
            settingsPrefsEditor.putInt(MainActivity.MAX_ROUNDS, MainActivity.DEFAULT_ROUNDS);
        } else {

        }

        settingsPrefsEditor.apply();
    }
}
