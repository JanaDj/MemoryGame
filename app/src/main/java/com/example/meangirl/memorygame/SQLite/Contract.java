package com.example.meangirl.memorygame.SQLite;
import android.provider.BaseColumns;

public class Contract {
    //every table would represent an inner class
    public static final  class ScoresSchema implements BaseColumns {
        //table name
        public static final String TABLE_NAME = "high_scores";
        //columns
        public static final String _ID = BaseColumns._ID;
        public static final String FIELD_NAME = "player_name";
        public static final String FIELD_SCORE = "score";
    }
}
