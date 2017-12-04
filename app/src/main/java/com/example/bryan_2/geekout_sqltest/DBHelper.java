package com.example.bryan_2.geekout_sqltest;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by Bryan_2 on 11/5/2017.
 */

// database code derived from Adam Porter's DataManagementSQL app
public class DBHelper extends SQLiteOpenHelper {

    // Used here an maybe elsewhere
    final static String QUESTION_NAME = "name";
    final static String QUESTION_CATEGORY = "category";
    final static String QUESTION_BID = "bid";
    private final static String QUESTION_ID = "_id";

    // Used by other classes
    final static String TABLE_NAME = "questions";

    // Change this if you want less categories
    final static String[] columns = { QUESTION_ID, QUESTION_NAME , QUESTION_CATEGORY, QUESTION_BID};


    // Other stuff
    final private static String NAME = "question_db";
    final private static Integer VERSION = 1;
    final private Context mContext;

    final private static String CREATE_CMD =

            // These values need to match QUESTION_NAME, QUESTION_CATEGORY, and QUESTION_BID
            // written out tho for easy viewing
            "CREATE TABLE questions " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category TEXT NOT NULL, bid INTEGER)";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }


}
