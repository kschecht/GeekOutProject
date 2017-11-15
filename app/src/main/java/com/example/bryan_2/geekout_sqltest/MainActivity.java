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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView mTV;
    private DBHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;
    private Cursor cursor;
    private String TAG = "TAG";
    private String chosenCategory = "Fantasy";
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
                Log.i(TAG, "Post-Count: " + cursor.getCount());
                Log.i(TAG, DatabaseUtils.dumpCursorToString(cursor));

                // TODO Check if moveToNext() != NULL, if so they have method to reset cursor
                if (true) {
                    Log.i(TAG, "Move to first called");

                    Log.i(TAG, "Category: " + cursor.getString(cursor.getColumnIndex("category")));
                    Log.i(TAG, "Name: " + cursor.getString(cursor.getColumnIndex("name")));
                    Log.i(TAG, "Bid: " + cursor.getString(cursor.getColumnIndex("bid")));

                    String concated_question = cursor.getString(cursor.getColumnIndex("bid")) + " " +
                            cursor.getString(cursor.getColumnIndex("name"));
                    mTV.setText(concated_question);
                    Log.i(TAG, String.valueOf(cursor.moveToNext()));
                }
                else {
                    Log.i(TAG, "Not called moveToFirst");
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

        // TODO Set up cursors for the separate categories
        cursor = mDbHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                DBHelper.columns, mDbHelper.QUESTION_CATEGORY + "=?", new String[] {chosenCategory}, null, null,
                null, "3");
        cursor.moveToFirst();
		
		/*
			Options for reshuffling questions
			1. Have a hashmap that checks to see if question has already been checked
				Ok, kinda inefficient if most questions already answered
				Easy to implement
				
			2. Have multiple cursors for categories
				To update cursor (Since java is pass by value) can have method that changes and returns new cursor
				I like this idea more
		
		
		
		
		
		
		*/
    }

    // Insert several question records
    private void insertQuestions() {
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
