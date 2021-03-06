package com.chii.antforest.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chii.antforest.R;
import com.chii.antforest.pojo.AlipayId;

import java.util.List;

public class ListDialogAdapter extends BaseAdapter {
    private static ListDialogAdapter adapter;
    private Context context;
    private List<?> list;
    private List<String> selects;
    private int findIndex = -1;
    private CharSequence findWord = null;

    public static ListDialogAdapter get(Context c) {
        if (adapter == null) {
            adapter = new ListDialogAdapter(c);
        }
        return adapter;
    }


    private ListDialogAdapter(Context c) {
        context = c;
    }

    public void setBaseList(List<?> l) {
        if (l != list) {
            exitFind();
        }
        list = l;
    }

    public void setSelectedList(List<String> l) {
        selects = l;
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
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int p1) {
        return list.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        return p1;
    }

    @Override
    public View getView(int p1, View p2, ViewGroup p3) {
        ViewHolder vh;
        if (p2 == null) {
            vh = new ViewHolder();
            p2 = LayoutInflater.from(context).inflate(R.layout.list_item_dialog, null);
            vh.tv = p2.findViewById(R.id.tv_idn);
            vh.cb = p2.findViewById(R.id.cb_list);
            p2.setTag(vh);
        } else {
            vh = (ViewHolder) p2.getTag();
        }

        AlipayId ai = (AlipayId) list.get(p1);
        vh.tv.setText(ai.name);
        vh.tv.setTextColor(findIndex == p1 ? Color.RED : Color.BLACK);
        vh.cb.setChecked(selects != null && selects.contains(ai.id));
        return p2;
    }

    public class ViewHolder {
        public TextView tv;
        public CheckBox cb;
    }

}
