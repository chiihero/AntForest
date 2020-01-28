package com.chii.antforest.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chii.antforest.R;

import java.util.List;

public class ListLogViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> logList;

    public ListLogViewAdapter(Context mContext, List<String> logList) {
        this.mContext = mContext;
        this.logList = logList;
    }

    @Override
    public int getCount() {
        return logList.size();
    }

    @Override
    public Object getItem(int position) {
        return logList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_log, null);
        }
        TextView listViewLog = ViewHolder.get(convertView, R.id.item_log);
        listViewLog.setText(logList.get(position));

        return convertView;
    }
}
