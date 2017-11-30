package com.example.bryan_2.geekout_sqltest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.RadioGroup;

/**
 * Created by kschechter on 11/15/2017.
 */

public class SettingsActivity extends Activity {

    private RadioGroup mGameModeRadioGroup;
    private TextView mPointLimitText;
    private TextView mRoundLimitText;
    private int originalGameMode;
    private int originalRoundLimit;
    private int originalPointLimit;
    private ImageButton decrementPointButton;
    private ImageButton incrementPointButton;
    private ImageButton decrementRoundButton;
    private ImageButton incrementRoundButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "Started Settings activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View myView = this.getWindow().getDecorView();

        myView.setBackgroundResource(R.color.geekout);
        mGameModeRadioGroup = (RadioGroup) findViewById(R.id.gameModeRadioButtons);
        mPointLimitText = findViewById(R.id.currentPointValue);
        mRoundLimitText = findViewById(R.id.currentRoundValue);
        incrementPointButton = findViewById(R.id.incrementPointButton);
        incrementPointButton.setImageResource(android.R.drawable.ic_input_add);
        incrementRoundButton = findViewById(R.id.incrementRoundButton);
        incrementRoundButton.setImageResource(android.R.drawable.ic_input_add);
        decrementPointButton = findViewById(R.id.decrementPointButton);
        decrementPointButton.setImageResource(android.R.drawable.ic_input_delete);
        decrementRoundButton = findViewById(R.id.decrementRoundButton);
        decrementRoundButton.setImageResource(android.R.drawable.ic_input_delete);

        final SharedPreferences settingsPrefs = getSharedPreferences
                (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // Settings have not been initialized yet, should not happen here
        if (settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
            originalGameMode = QuestionActivity.POINTS_MODE;
        } else {
            originalGameMode = settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1);
        }
        if (settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, QuestionActivity.DEFAULT_ROUNDS);
            originalRoundLimit = QuestionActivity.DEFAULT_ROUNDS;
        } else {
            originalGameMode = settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1);
        }
        if (settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, QuestionActivity.DEFAULT_POINTS);
            originalPointLimit = QuestionActivity.DEFAULT_POINTS;
        } else {
            originalPointLimit = settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1);
        }
        // apply changes to create defaults if necessary
        settingsPrefsEditor.apply();
        if (settingsPrefs.getInt(QuestionActivity.GAME_MODE, -1) == QuestionActivity.ROUND_MODE) {
            mGameModeRadioGroup.check(R.id.roundsLimit);
        }
        mPointLimitText.setText(String.valueOf(settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)));
        mRoundLimitText.setText(String.valueOf(settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1)));

        incrementPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS,
                        ((settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)) + 1));
                settingsPrefsEditor.apply();
                Log.d("setPoints", String.valueOf(settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)));
                mPointLimitText.setText(String.valueOf(
                        settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)));
            }
        });

        decrementPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1) > 1) {
                    settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS,
                            ((settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)) - 1));
                    settingsPrefsEditor.apply();
                    mPointLimitText.setText(String.valueOf(
                            settingsPrefs.getInt(QuestionActivity.MAX_POINTS, -1)));
                }
            }
        });

        incrementRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS,
                        settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) + 1);
                settingsPrefsEditor.apply();
                mRoundLimitText.setText(String.valueOf(
                        settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1)));
            }
        });

        decrementRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) > 1) {
                    settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS,
                            settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1) - 1);
                    settingsPrefsEditor.apply();
                    mRoundLimitText.setText(String.valueOf(
                            settingsPrefs.getInt(QuestionActivity.MAX_ROUNDS, -1)));
                }
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // restore original prefs
                Log.d("originalRoundLimit", ""+originalRoundLimit);
                settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, originalGameMode);
                settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, originalRoundLimit);
                settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, originalPointLimit);
                mPointLimitText.setText(String.valueOf(originalPointLimit));
                mRoundLimitText.setText(String.valueOf(originalRoundLimit));
                settingsPrefsEditor.apply();

                finish();
            }
        });

        final Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // set shared prefs game mode
                if (mGameModeRadioGroup.getCheckedRadioButtonId() == R.id.pointsLimit) {
                    settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
                } else {
                    // rounds limit
                    settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.ROUND_MODE);
                }

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
