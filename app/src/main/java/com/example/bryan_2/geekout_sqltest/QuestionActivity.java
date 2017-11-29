package com.example.bryan_2.geekout_sqltest;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    // name of SharedPreferences with settings
    public static final String SETTINGS_PREFS_NAME = "SettingsPrefs";
    // SharedPreferences GameMode
    public static final String GAME_MODE = "GameMode";
    public static final int POINTS_MODE = 0;
    // if implementing time, uncomment this
    //public static final int TIME_MODE = 1;
    public static final int ROUND_MODE = 2;
    // SharedPreferences game point limit (if POINTS_MODE)
    public static final String MAX_POINTS = "MaxPoints";
    public static final int DEFAULT_POINTS = 5;
    // SharedPreferences game time limit (if TIME_MODE)
    // if implementing time, uncomment this
    /*
    public static final String MAX_MINUTES = "MaxMinutes";
    public static final int DEFAULT_MINUTES = 10;
    */
    // SharedPreferences game round limit (if ROUND_MODE)
    public static final String MAX_ROUNDS = "MaxRounds";
    public static final int DEFAULT_ROUNDS = 6;
    /* To access SharedPreferences settings data, do:
        SharedPreferences settingsPrefs = getSharedPreferences
            (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        settingsPrefs.getInt(GAME_MODE, -1);
        settingsPrefs.getInt(MAX_POINTS, -1);
        settingsPrefs.getInt(MAX_MINUTES, -1);
        settingsPrefs.getInt(MAX_ROUNDS, -1);
     */

    static final public String INTENT_TAG = "Picked Questions";
    static final public String INTENT_COLOR = "Question Color";
    static final public String STATE_USED_QUESTIONS = "used questions";

    static final public String GAMES = "Games";
    static final public String COMICS = "Comic Books";
    static final public String SCI_FI = "Sci Fi";
    static final public String FANTASY = "Fantasy";
    static final public String MISCELLANEOUS = "Miscellaneous";


    private TextView mTV;
    private DBHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;
    private Cursor cursor;
    private String TAG = "TAG";
    private DialogFragment mDialog;
    private String chosenCategory = GAMES;
    private HashMap<String, Cursor> cursorHashMap;
    private ArrayList<String> usedQuestions;

    private View myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
            General Setup
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        /*
           SharedPreferences
         */
        SharedPreferences settingsPrefs = getSharedPreferences
                (QuestionActivity.SETTINGS_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor settingsPrefsEditor = settingsPrefs.edit();

        // initialize settings to defaults if they haven't been initialized yet
        if (settingsPrefs.getInt(GAME_MODE, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.GAME_MODE, QuestionActivity.POINTS_MODE);
        }
        if (settingsPrefs.getInt(MAX_ROUNDS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_ROUNDS, QuestionActivity.DEFAULT_POINTS);
        }
        // if implementing time, uncomment this
        /*
        if (settingsPrefs.getInt(MAX_MINUTES, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_MINUTES, QuestionActivity.DEFAULT_MINUTES);
        }
        */
        if (settingsPrefs.getInt(MAX_POINTS, -1) == -1) {
            settingsPrefsEditor.putInt(QuestionActivity.MAX_POINTS, QuestionActivity.DEFAULT_ROUNDS);
        }
        settingsPrefsEditor.apply();


        /*
            Toolbar Settings
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO Re-Implement settings button
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchSettingsActInt = new Intent(QuestionActivity.this, SettingsActivity.class);
                startActivity(launchSettingsActInt);
            }
        });


        /*
            Main Question Database Portion
         */
        chosenCategory = getIntent().getStringExtra(INTENT_TAG);
        myView = this.getWindow().getDecorView();
        myView.setBackgroundResource(Integer.valueOf(getIntent().getStringExtra(INTENT_COLOR)));

        mTV = (TextView) findViewById(R.id.textView);

        // Restore used question list
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            usedQuestions = savedInstanceState.getStringArrayList(STATE_USED_QUESTIONS);
        } else {
            if (usedQuestions == null) {
                usedQuestions = new ArrayList<String>();
                usedQuestions.add("Games: \n4 Characters from Mortal Kombat"); //TODO remove this line
                usedQuestions.add("Games: \n2 Videogames in which you can play as the villain");
                usedQuestions.add("Games: \n2 Board games with a sports theme");
//                usedQuestions.add("Games: \n4 Collectible card games");
//                usedQuestions.add("Games: \n4 Named villains in videogames");
            }


        }


        Button diffquestionButton = (Button) findViewById(R.id.diffquestionButton);
        diffquestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTV.setText(query_question());
            }
        });

        // Create a new DatabaseHelper
        mDbHelper = new DBHelper(this);

        // start with an empty database
        // TODO REMOVE THIS
        clearAll();

        // Insert records
        insertQuestions();


        // Creating cursors for the five main categories and randomly organizing their contents
        cursorHashMap = new HashMap<String, Cursor>();
        for (String cat : new String[]{GAMES, COMICS, SCI_FI, FANTASY, MISCELLANEOUS}) {
            cursorHashMap.put(cat, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                    DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[]{cat}, null, null,
                    "RANDOM()", null));
        }

        /*
         Moves each cursor in the hashmap to the first position in the list of questions

          EXTREMELY IMPORTANT: Must always move cursor to the front
          */
        for (HashMap.Entry<String, Cursor> entry : cursorHashMap.entrySet()) {
            entry.getValue().moveToFirst();
        }
        // Sets TextView Text for the first time
        mTV.setText(query_question());
        /*
                Checking if in usedQuestion

                while loop
                If so
                move cursor
                assign cursor
                make new string

                edge case:
                1. Nothing left in category
                2. Cursor reaches the end and needs to reshuffle

                // Why can't we just delete queried questions?
                1. back button issue means the question will remain deleted
                    - can be overriden
                2. What if we use all the questions?
                    - Eliminate category?
                3. Would be nice to remember which question each team got
                4. Would have to reload all the questions
                5. Won't remember used questions when onDestroyed is called




        */


      /*
        Dialog for picking Done button
       */
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Done with picking");

                // TODO Figure out a way to wait for user response to dialog before proceeding
                // Create a new AlertDialogFragment
                mDialog = PlayerChangeDialogFragment.newInstance();

                // Show AlertDialogFragment
                mDialog.show(getFragmentManager(), "Alert");

                // Adds picked question to the used question list
                // TODO Remove question if back button pressed on bidding activity
                Log.i("USED", String.valueOf(mTV.getText()));
                usedQuestions.add(String.valueOf(mTV.getText()));


                // Starts Bidding Activity
                Intent intent = new Intent(QuestionActivity.this, BiddingActivity.class);
                startActivity(intent);
            }
        });

        /*
            TODO - Remove? Seems like the back button accomplishes this
         */
        Button rerollButton = (Button) findViewById(R.id.rerollButton);
        rerollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Re-Roll for Category");
                Intent intent = new Intent(QuestionActivity.this, DiceRoller.class);
                startActivity(intent);
            }
        });
    }

    private String query_question() {
//        Log.i(TAG, "Cursor Succeeded");
//        Log.i(TAG, "Post-Count: " + cursor.getCount());
//        Log.i(TAG, DatabaseUtils.dumpCursorToString(cursor));
//
//
//        Log.i(TAG, "Category: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_CATEGORY)));
//        Log.i(TAG, "Name: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME)));
//        Log.i(TAG, "Bid: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)));

        cursor = cursorHashMap.get(chosenCategory);
        if (cursor == null) {
            Log.i(TAG, "Cursor is null");
            return "Null String: Category Empty";
        }
        else {
            String result = chosenCategory + ": \n" + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)) + " " +
                    cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME));

            Log.i(TAG, DatabaseUtils.dumpCursorToString(cursor));
            Log.i(TAG, "Category: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_CATEGORY)));
            Log.i(TAG, "Name: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME)));
            Log.i(TAG, "Bid: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)));

            // Doesn't work because question still in there, they just get randomized
            while (usedQuestions.contains(result)) {
                if (!nextQuestion()) {
                    result = "Null String: All questions used";
                }
                else {
                    result = chosenCategory + ": \n" + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)) + " " +
                            cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME));
                }

            }
            nextQuestion(); // Preps the next question

            Log.i(TAG, "Query_Question reulst: " + result);

            return result;
        }
    }

    private boolean nextQuestion() {
        // Shuffles if at the end of question list
        if (!cursorHashMap.get(chosenCategory).moveToNext()) {
            cursorHashMap.put(chosenCategory, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                    DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[]{chosenCategory}, null, null,
                    "RANDOM()", null));
            cursorHashMap.get(chosenCategory).moveToFirst(); // ALWAYS MOVE THE CURSOR TO THE FIRST POSITION

            Log.i(TAG, chosenCategory + " category reshuffled");
            Toast.makeText(getApplicationContext(), chosenCategory + " category reshuffled", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
    // Insert several question records
    private void insertQuestions() {

        // Accesses the CSV file containing the list of questions
        String mCSVfile = "436questions.csv";
        AssetManager manager = getApplicationContext().getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));

        String line = "";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");

//                Uncomment and changed columns.length if you want to skip bad csv rows
//                if (colums.length != 4) {
//                    Log.d("CSVParser", "Skipping Bad CSV Row");
//                    continue;
//                }

                // Inserts the data into the database
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.QUESTION_NAME, colums[1].trim());
                cv.put(mDbHelper.QUESTION_BID, colums[2].trim());
                cv.put(mDbHelper.QUESTION_CATEGORY, colums[3].trim());
                db.insert(mDbHelper.TABLE_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO get rid of Settings button and use this instead
//            Intent launchSettingsActInt = new Intent(QuestionActivity.this, SettingsActivity.class);
//            startActivity(launchSettingsActInt);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Saves used questions

     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putStringArrayList(STATE_USED_QUESTIONS, usedQuestions);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    // Delete all records
    private void clearAll() {

        mDbHelper.getWritableDatabase().delete(DBHelper.TABLE_NAME, null, null);

    }

    // Close database
    @Override
    protected void onDestroy() {

        mDbHelper.getWritableDatabase().close();
        mDbHelper.deleteDatabase();

        super.onDestroy();

    }

    /*
        To prevent issues from pressing the back button
     */
    @Override
    public void onBackPressed() {

    }
}
