package com.example.meangirl.memorygame.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "HighScore.db";
    public static int DB_VERSION = 1;

    public static String DATABASE_CREATION =
            "CREATE TABLE "+ Contract.ScoresSchema.TABLE_NAME + " (" +
                    Contract.ScoresSchema._ID + " INTEGER PRIMARY KEY autoincrement NOT NULL, "
                    + Contract.ScoresSchema.FIELD_NAME + " TEXT, "
                    + Contract.ScoresSchema.FIELD_SCORE + " TEXT" +
                    ")";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ScoresSchema.TABLE_NAME);
        onCreate(db);
    }
}
