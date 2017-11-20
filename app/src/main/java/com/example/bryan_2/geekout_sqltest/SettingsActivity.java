package com.example.bryan_2.geekout_sqltest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
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
        Log.i("TAG", "Started Settings activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGameModeRadioGroup = (RadioGroup) findViewById(R.id.gameModeRadioButtons);
        mPointLimitText = (EditText) findViewById(R.id.pointLimitEditText);
        mRoundLimitText = (EditText) findViewById(R.id.roundLimitEditText);

        SharedPreferences settingsPrefs = getSharedPreferences
                (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // Settings have not been initialized yet, should not happen here
        if (settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
        }
        if (settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, QuestionActivity.DEFAULT_ROUNDS);
        }
        // if implementing time, uncomment this
        /*
        if (settingsPrefs.getInt(QuestionActivity.MAX_MINUTES, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_MINUTES, QuestionActivity.DEFAULT_MINUTES);
        }
        */
        if (settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, QuestionActivity.DEFAULT_POINTS);
        }
        // apply changes to create defaults if necessary
        settingsPrefsEditor.apply();
        mPointLimitText.setText(String.valueOf(settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)));
        mRoundLimitText.setText(String.valueOf(settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1)));

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
                    settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
                } else {
                    // rounds limit
                    settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.ROUND_MODE);
                }

                // TODO Only remember the value if the user pressed update
                // Case- If you change the value then press cancel, the value will remain even
                // if the app is closed and restarted.
                
                // set shared prefs point limit
                String max_points_string = mPointLimitText.getText().toString();
                String max_rounds_string = mRoundLimitText.getText().toString();
                if (max_points_string.equals(""))
                    max_points_string = "5";
                settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, Integer.parseInt(max_points_string));

                // set shared prefs round limit
                if (max_rounds_string.equals(""))
                    max_rounds_string = "6";
                settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, Integer.parseInt(max_rounds_string));

                settingsPrefsEditor.apply();
                
                finish();
            }
        });
    }
}
