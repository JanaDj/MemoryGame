package com.example.meangirl.memorygame;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.meangirl.memorygame.SQLite.Contract;
import com.example.meangirl.memorygame.SQLite.DatabaseHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    ImageButton soundBtn;
    boolean soundOn;
    MediaPlayer mediaPlayer;
    ImageView iv11, iv12, iv13, iv14, iv21, iv22, iv23, iv24, iv31, iv32, iv33, iv34, iv41, iv42, iv43, iv44;
    Button resetBtn, highScoresBtn;
    TextView cardsTurnedTv;
    ArrayList<ImageView> imageViewsList;
    ArrayList<Drawable> imageList;
    HashMap<ImageView, Drawable> boardDataList;
    public ImageView img1,img2;
    int turnsCount;

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        connectWithXml();
        populateData();
        soundOn = true;

        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundOn){

                    soundBtn.setImageResource(R.drawable.soundoff);
                    soundOn = false;
                } else {
                    soundBtn.setImageResource(R.drawable.soundon);
                    soundOn = true;
                }
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1 = null;
                img2 = null;
                turnsCount = 0;
                cardsTurnedTv.setText("0");
                //in case someone matched a pair and clicked reset right away
                setImageAlpha();
                populateData();
                resetBoard();

            }
        });
        highScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighScore.class);
                startActivity(intent);
            }
        });
    }
    /**
     * OnClick method for the imageViews
     * @param view, Clicked ImageView
     */
    public void onClick(View view) {
        ImageView iv = (ImageView) view;

        if (img1 == null) {
            iv.setImageDrawable(boardDataList.get(iv));
            img1 = iv;
            img1.setClickable(false);
            turnsCount++;
            cardsTurnedTv.setText(Integer.toString(turnsCount));

        } else if (img2 == null) {
                iv.setImageDrawable(boardDataList.get(iv));
                img2 = iv;
                img2.setClickable(false);
                turnsCount++;
                cardsTurnedTv.setText(Integer.toString(turnsCount));

                //checking if the pair is found
                if ((img1.getDrawable().getConstantState().equals(img2.getDrawable().getConstantState()))) {
                    //if pair is found, views disappear from the board
                    img1.animate().alpha(0).setDuration(2000);
                    img2.animate().alpha(0).setDuration(2000);
                    if(soundOn) {
                        mediaPlayer.start();
                    }
//                    img1.setVisibility(View.INVISIBLE);
//                    img2.setVisibility(View.INVISIBLE);

                    //remove found pair from the list
                    imageViewsList.remove(img1);
                    imageViewsList.remove(img2);
                    //reset containers for next set
                    img1 = null;
                    img2 = null;

                    //we need to check if the board is now clear:
                    if(imageViewsList.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        builder.setTitle("Enter your name:");
                        // Set up the input
                        final EditText input = new EditText(HomePage.this);
                        // limit the number of characters for input
                        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});

                        builder.setView(input);
                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //add score
                                DatabaseHelper dbHelper = new DatabaseHelper(HomePage.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                if(!TextUtils.isEmpty(input.getText())) {
                                    contentValues.put(Contract.ScoresSchema.FIELD_NAME, input.getText().toString());
                                    contentValues.put(Contract.ScoresSchema.FIELD_SCORE, turnsCount);
                                    //inserting into the db
                                    db.insert(Contract.ScoresSchema.TABLE_NAME, null, contentValues);
                                    //redirect to the high scores list
                                    Intent intent = new Intent(HomePage.this, HighScore.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetBtn.callOnClick();
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    //if the pair is not found, we reset the board
                } else {
                    Runnable r = new Runnable() {
                        // Override the run Method
                        public void run() {
                            resetBoard();
                            img1 = null;
                            img2 = null;
                        }
                    };
                    mHandler.postDelayed(r, 1000);
                }
            }

    }
    /**
     * Method to populate hashMap with imageViews and images
     */
    void populateData() {
            initData();
        Collections.shuffle(imageList);
        for(int i = 0; i <imageList.size(); i++){
            boardDataList.put(imageViewsList.get(i), imageList.get(i));
        }
    }
    /**
     * Method to reset the board and set all imageviews to the background image
     */
    void resetBoard(){
        for (ImageView i : imageViewsList) {
            i.setImageDrawable(getResources().getDrawable(R.drawable.background));
            i.setClickable(true);
        }
    }
    /**
     * Methood to initialize data - populate the board data HashMap and set visibility for all imageViews
     */

    void initData(){
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.yay);
        boardDataList = new HashMap<>();
        imageViewsList = new ArrayList<>();
        imageViewsList.addAll(Arrays.asList(iv11,iv12,iv13,iv14,iv21,iv22,iv23,iv24,iv31,iv32,iv33,iv34,iv41,iv42,iv43,iv44));
        imageList = new ArrayList<>();
        imageList.addAll(Arrays.asList(getResources().getDrawable(R.drawable.a1),getResources().getDrawable(R.drawable.a2),getResources().getDrawable(R.drawable.a3),getResources().getDrawable(R.drawable.a4),getResources().getDrawable(R.drawable.a5),getResources().getDrawable(R.drawable.a6),getResources().getDrawable(R.drawable.a7),getResources().getDrawable(R.drawable.a8),getResources().getDrawable(R.drawable.a1),getResources().getDrawable(R.drawable.a2),getResources().getDrawable(R.drawable.a3),getResources().getDrawable(R.drawable.a4),getResources().getDrawable(R.drawable.a5),getResources().getDrawable(R.drawable.a6),getResources().getDrawable(R.drawable.a7),getResources().getDrawable(R.drawable.a8)));
        setImageAlpha();

    }

    /**
     * Method to set image alpha to 1 for all images (set visible) with fade in animation
     */
    void setImageAlpha() {
        iv11.animate().alpha(1).setDuration(2000);
        iv11.animate().alpha(1).setDuration(2000);
        iv12.animate().alpha(1).setDuration(2000);
        iv13.animate().alpha(1).setDuration(2000);
        iv14.animate().alpha(1).setDuration(2000);
        iv21.animate().alpha(1).setDuration(2000);
        iv22.animate().alpha(1).setDuration(2000);
        iv23.animate().alpha(1).setDuration(2000);
        iv24.animate().alpha(1).setDuration(2000);
        iv31.animate().alpha(1).setDuration(2000);
        iv32.animate().alpha(1).setDuration(2000);
        iv33.animate().alpha(1).setDuration(2000);
        iv34.animate().alpha(1).setDuration(2000);
        iv41.animate().alpha(1).setDuration(2000);
        iv42.animate().alpha(1).setDuration(2000);
        iv43.animate().alpha(1).setDuration(2000);
        iv44.animate().alpha(1).setDuration(2000);
        iv44.animate().alpha(1).setDuration(2000);
//        iv11.setAlpha(1f);
//        iv11.setAlpha(1f);
//        iv12.setAlpha(1f);
//        iv13.setAlpha(1f);
//        iv14.setAlpha(1f);
//        iv21.setAlpha(1f);
//        iv22.setAlpha(1f);
//        iv23.setAlpha(1f);
//        iv24.setAlpha(1f);
//        iv31.setAlpha(1f);
//        iv32.setAlpha(1f);
//        iv33.setAlpha(1f);
//        iv34.setAlpha(1f);
//        iv41.setAlpha(1f);
//        iv42.setAlpha(1f);
//        iv43.setAlpha(1f);
//        iv44.setAlpha(1f);
//        iv44.setAlpha(1f);
    }
    /**
     * method to connect XML components with java code
     */
    void connectWithXml(){
        iv11 = findViewById(R.id.IV11);
        iv12 = findViewById(R.id.IV12);
        iv13 = findViewById(R.id.IV13);
        iv14 = findViewById(R.id.IV14);
        iv21 = findViewById(R.id.IV21);
        iv22 = findViewById(R.id.IV22);
        iv23 = findViewById(R.id.IV23);
        iv24 = findViewById(R.id.IV24);
        iv31 = findViewById(R.id.IV31);
        iv32 = findViewById(R.id.IV32);
        iv33 = findViewById(R.id.IV33);
        iv34 = findViewById(R.id.IV34);
        iv41 = findViewById(R.id.IV41);
        iv42 = findViewById(R.id.IV42);
        iv43 = findViewById(R.id.IV43);
        iv44 = findViewById(R.id.IV44);
        resetBtn = findViewById(R.id.resetBtn);
        highScoresBtn = findViewById(R.id.highScoresBtn);
        cardsTurnedTv = findViewById(R.id.cardsTurnedTV);
        soundBtn = findViewById(R.id.soundBtn);
        turnsCount = 0;
            initData();

    }
}
