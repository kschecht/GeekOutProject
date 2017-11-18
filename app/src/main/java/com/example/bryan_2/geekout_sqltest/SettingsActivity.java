package com.example.bryan_2.geekout_sqltest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by kschechter on 11/15/2017.
 */

public class SettingsActivity extends Activity {

    private RadioGroup mGameModeRadioGroup;
    private EditText mPointLimitText;
    private EditText mRoundLimitText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGameModeRadioGroup = (RadioGroup) findViewById(R.id.gameModeRadioButtons);
        mPointLimitText = (EditText) findViewById(R.id.pointLimitEditText);
        mRoundLimitText = (EditText) findViewById(R.id.roundLimitEditText);

        SharedPreferences settingsPrefs = getSharedPreferences
                (MainActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // Settings have not been initialized yet, should not happen here
        if (settingsPrefs.getInt(MainActivity.GAME_MODE, -1) == -1) {
            settingsPrefsEditor.putInt(MainActivity.GAME_MODE, MainActivity.POINTS_MODE);
        }
        if (settingsPrefs.getInt(MainActivity.MAX_ROUNDS, -1) == -1) {
            settingsPrefsEditor.putInt(MainActivity.MAX_ROUNDS, MainActivity.DEFAULT_ROUNDS);
        }
        // if implementing time, uncomment this
        /*
        if (settingsPrefs.getInt(MainActivity.MAX_MINUTES, -1) == -1) {
            settingsPrefsEditor.putInt(MainActivity.MAX_MINUTES, MainActivity.DEFAULT_MINUTES);
        }
        */
        if (settingsPrefs.getInt(MainActivity.MAX_POINTS, -1) == -1) {
            settingsPrefsEditor.putInt(MainActivity.MAX_POINTS, MainActivity.DEFAULT_POINTS);
        }
        // apply changes to create defaults if necessary
        settingsPrefsEditor.apply();
        mPointLimitText.setText(settingsPrefs.getInt(MainActivity.MAX_POINTS, -1));
        mRoundLimitText.setText(settingsPrefs.getInt(MainActivity.MAX_ROUNDS, -1));

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button updateButton = (Button) findViewById(R.id.updateButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // set shared prefs game mode
                if (mGameModeRadioGroup.getCheckedRadioButtonId() == R.id.pointsLimit) {
                    settingsPrefsEditor.putInt(MainActivity.GAME_MODE, MainActivity.POINTS_MODE);
                } else {
                    // rounds limit
                    settingsPrefsEditor.putInt(MainActivity.GAME_MODE, MainActivity.ROUND_MODE);
                }

                // set shared prefs point limit
                settingsPrefsEditor.putInt(MainActivity.MAX_POINTS, Integer.parseInt(mPointLimitText.getText().toString()));

                // set shared prefs round limit
                settingsPrefsEditor.putInt(MainActivity.MAX_ROUNDS, Integer.parseInt(mRoundLimitText.getText().toString()));

                finish();
            }
        });

        settingsPrefsEditor.apply();
    }
}
