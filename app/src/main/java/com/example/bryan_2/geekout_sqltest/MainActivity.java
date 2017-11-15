package com.example.bryan_2.geekout_sqltest;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static final private String GAMES = "Games";
    static final private String COMICS = "Comics";
    static final private String SCI_FI = "Sci-Fi";
    static final private String FANTASY = "Fantasy";
    static final private String MISCELLANEOUS = "Miscellaneous";

    private TextView mTV;
    private DBHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;
    private Cursor cursor;
    private String TAG = "TAG";
    private String chosenCategory = GAMES;
    private HashMap<String, Cursor> cursorHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar for settings and stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTV = (TextView) findViewById(R.id.textView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor = cursorHashMap.get(chosenCategory);

                if (cursor != null) {
                    Log.i(TAG, "Cursor Succeeded");
                    Log.i(TAG, "Post-Count: " + cursor.getCount());
                    Log.i(TAG, DatabaseUtils.dumpCursorToString(cursor));


                    Log.i(TAG, "Category: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_CATEGORY)));
                    Log.i(TAG, "Name: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME)));
                    Log.i(TAG, "Bid: " + cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)));

                    // Sets TextView Text
                    String concated_question = cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_BID)) + " " +
                            cursor.getString(cursor.getColumnIndex(mDbHelper.QUESTION_NAME));
                    mTV.setText(concated_question);

                    /*
                    If this is the last question in the list, shuffle the list

                    Can't move cursor.moveToNext because shallow copy? Need to move the actual
                    one stored in hashmap

                     */
                    if (!cursorHashMap.get(chosenCategory).moveToNext()) {
                        cursorHashMap.put(chosenCategory, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {chosenCategory}, null, null,
                                "RANDOM()", null));
                        cursorHashMap.get(chosenCategory).moveToFirst(); // ALWAYS MOVE THE CURSOR TO THE FIRST POSITION

                        Log.i(TAG, chosenCategory + " category reshuffled");
                        Toast.makeText(getApplicationContext(),chosenCategory + " category reshuffled", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i(TAG, "Error: Cursor Failed");
                }
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

        cursorHashMap.put(GAMES, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {GAMES}, null, null,
                "RANDOM()", null));
        cursorHashMap.put(COMICS, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {COMICS}, null, null,
                "RANDOM()", null));
        cursorHashMap.put(SCI_FI, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {SCI_FI}, null, null,
                "RANDOM()", null));
        cursorHashMap.put(FANTASY, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {FANTASY}, null, null,
                "RANDOM()", null));
        cursorHashMap.put(MISCELLANEOUS, mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {MISCELLANEOUS}, null, null,
                "RANDOM()", null));

        /*
         Moves each cursor in the hashmap to the first position in the list of questions

          EXTREMELY IMPORTANT: Must always move cursor to the front
          */
        for(HashMap.Entry<String, Cursor> entry : cursorHashMap.entrySet()) {
            entry.getValue().moveToFirst();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
