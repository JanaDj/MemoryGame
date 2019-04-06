package com.example.meangirl.memorygame;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> namesList;
    ArrayList<String> scoreList;


    public ScoreListAdapter(Context context, ArrayList<String> namesList, ArrayList<String> scoreList) {
        this.context = context;
        this.namesList = namesList;
        this.scoreList = scoreList;
    }

    @Override
    public int getCount() {
        return namesList.size();
    }

    @Override
    public Object getItem(int position) {
        return namesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.score_list_item,parent,false);
        }

            TextView rankTv = (TextView) convertView.findViewById(R.id.rankNumTV);
            TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
            TextView scoreTv = (TextView) convertView.findViewById(R.id.pointsTv);

            rankTv.setText(position + 1 + "");
            nameTv.setText(namesList.get(position) + "");
            scoreTv.setText(String.valueOf(scoreList.get(position)));

            return convertView;
    }
}
