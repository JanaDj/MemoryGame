package com.example.meangirl.memorygame;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.meangirl.memorygame.SQLite.Contract;
import com.example.meangirl.memorygame.SQLite.DatabaseHelper;
import java.util.ArrayList;

public class HighScore extends AppCompatActivity {
    Button resetScoresBtn;
    ListView listView;
    ArrayList<String> namesList;
    ArrayList<String> scoreList;
    private ScoreListAdapter scoreListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_page);
        initData();

        scoreListAdapter = new ScoreListAdapter(this, namesList, scoreList);
        listView.setAdapter(scoreListAdapter);

        resetScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HighScore.this);
                builder.setTitle("Are you sure you want to reset the scores?");

                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //delete all data
                        dbHelper.onUpgrade(db,DatabaseHelper.DB_VERSION, DatabaseHelper.DB_VERSION+1);
                        Toast.makeText(getApplicationContext(), "Data has been successfully cleared", Toast.LENGTH_LONG).show();
                        //refresh displayed data
                        initData();
                        scoreListAdapter = new ScoreListAdapter(getApplicationContext(), namesList, scoreList);
                        listView.setAdapter(scoreListAdapter);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    /**
     * Retreive data from the db and fill the lists
     */
    void initData() {
        resetScoresBtn = findViewById(R.id.resetScoresBtn);
        listView = (ListView) findViewById(R.id.scoreList);
        namesList = new ArrayList<>();
        scoreList = new ArrayList<>();
        String searchQuery = "SELECT * FROM " + Contract.ScoresSchema.TABLE_NAME + " ORDER BY CAST(" + Contract.ScoresSchema.FIELD_SCORE + " AS INTEGER)";
        DatabaseHelper dbHelper = new DatabaseHelper(HighScore.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(searchQuery,null);
        if(c.moveToFirst()) {
            do {
                namesList.add(c.getString(c.getColumnIndex(Contract.ScoresSchema.FIELD_NAME)));
                scoreList.add(c.getString(c.getColumnIndex(Contract.ScoresSchema.FIELD_SCORE)));
            }
            while(c.moveToNext());
        }
        c.close();
        db.close();
    }
}
