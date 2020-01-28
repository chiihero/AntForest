package com.chii.antforest.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chii.antforest.R;
import com.chii.antforest.pojo.AlipayId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListFriendAdapter extends BaseAdapter implements Switch.OnCheckedChangeListener {
    private Context mContext;
    private Callback mCallback;
    private List<?> list;
    private Map<String, List> maps;
    private int findIndex = -1;
    private CharSequence findWord = null;

    public ListFriendAdapter(Context mContext,Callback callback, List<?> list, Map<String, List> maps) {
        this.mContext = mContext;
        this.list = list;
        this.maps = maps;
        this.mCallback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public int findLast(CharSequence cs) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        if (!cs.equals(findWord)) {
            findIndex = -1;
            findWord = cs;
        }
        int i = findIndex;
        if (i < 0) {
            i = list.size();
        }
        for (; ; ) {
            i = (i + list.size() - 1) % list.size();
            AlipayId ai = (AlipayId) list.get(i);
            if (ai.name.contains(cs)) {
                findIndex = i;
                break;
            }
            if (findIndex < 0 && i == 0) {
                break;
            }
        }
        notifyDataSetChanged();
        return findIndex;
    }

    public int findNext(CharSequence cs) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        if (!cs.equals(findWord)) {
            findIndex = -1;
            findWord = cs;
        }
        for (int i = findIndex; ; ) {
            i = (i + 1) % list.size();
            AlipayId ai = (AlipayId) list.get(i);
            if (ai.name.contains(cs)) {
                findIndex = i;
                break;
            }
            if (findIndex < 0 && i == list.size() - 1) {
                break;
            }
        }
        notifyDataSetChanged();
        return findIndex;
    }

    public void exitFind() {
        findIndex = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_friend,
                    null);
            viewHolder.textView = convertView.findViewById(R.id.textView_friend);
            viewHolder.switch1 = convertView.findViewById(R.id.switch_1);
            viewHolder.switch2 = convertView.findViewById(R.id.switch_2);
            viewHolder.switch3 = convertView.findViewById(R.id.switch_3);
            convertView.setTag(viewHolder);

        } else {
            //获取viewHolder实例
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AlipayId ai = (AlipayId) list.get(position);
        viewHolder.textView.setTextColor(findIndex == position ? Color.RED : Color.BLACK);
        viewHolder.textView.setText(ai.name);

        List<Boolean> switchCheck = new ArrayList<>();
        for (int i = 0; i < (maps.size() - 1)/2; i++) {
            boolean check = maps.get(String.valueOf(i)) != null && maps.get(String.valueOf(i)).contains(ai.id);
            if ((Boolean) maps.get("hasFalse").get(i)) {
                switchCheck.add(check);
            } else {
                switchCheck.add(!check);
            }
        }
        viewHolder.switch1.setChecked(switchCheck.get(0));
        viewHolder.switch1.setTag(R.id.sc1, position);
        viewHolder.switch1.setOnCheckedChangeListener(this);
        viewHolder.switch2.setChecked(switchCheck.get(1));
        viewHolder.switch2.setTag(R.id.sc2, position);
        viewHolder.switch2.setOnCheckedChangeListener(this);
        viewHolder.switch3.setChecked(switchCheck.get(2));
        viewHolder.switch3.setTag(R.id.sc3, position);
        viewHolder.switch3.setOnCheckedChangeListener(this);

        return convertView;
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCallback.switchClick(buttonView,isChecked);
    }

    static class ViewHolder {
        TextView textView;
        Switch switch1;
        Switch switch2;
        Switch switch3;
    }
    public interface Callback {
        void switchClick(CompoundButton buttonView, boolean isChecked);
    }
}
