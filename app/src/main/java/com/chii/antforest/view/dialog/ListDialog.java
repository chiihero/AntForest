package com.chii.antforest.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chii.antforest.R;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.CooperationIdMap;
import com.chii.antforest.util.FriendIdMap;
import com.chii.antforest.pojo.AlipayCooperate;
import com.chii.antforest.pojo.AlipayId;
import com.chii.antforest.pojo.AlipayUser;
import com.chii.antforest.view.adapter.ListDialogAdapter;

import java.util.List;

public class ListDialog {
    private static EditText edt_find;
    private static ListView lv_list;
    private static List<String> selectedList;
    private static List<Integer> countList;
    private static ListDialogAdapter.ViewHolder curViewHolder;
    private static AlipayId curAlipayId;


    public static void show(Context c, CharSequence title, List<?> list, List<String> stringListl,
                            List<Integer> integerList) {
        selectedList = stringListl;
        countList = integerList;
        ListDialogAdapter listAdapter = ListDialogAdapter.get(c);
        listAdapter.setBaseList(list);
        listAdapter.setSelectedList(selectedList);
        showListDialog(c, title);
    }

    private static void showListDialog(Context context, CharSequence title) {
        AlertDialog listDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(getListView(context))
                .setPositiveButton("确定", null)
                .create();

        listDialog.setOnShowListener(
                new OnShowListener() {
                    Context c;

                    OnShowListener setContext(Context c) {
                        this.c = c;
                        return this;
                    }

                    @Override
                    public void onShow(DialogInterface p1) {
                        ListDialogAdapter.get(c).notifyDataSetChanged();
                    }
                }.setContext(context));
        listDialog.show();
    }


    private static View getListView(Context c) {
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_list, null);
        OnBtnClickListener onBtnClickListener = new OnBtnClickListener();
        Button btnFindLast = v.findViewById(R.id.btn_find_last);
        Button btnFindNext = v.findViewById(R.id.btn_find_next);
        btnFindLast.setOnClickListener(onBtnClickListener);
        btnFindNext.setOnClickListener(onBtnClickListener);
        edt_find = v.findViewById(R.id.edt_find);
        lv_list = v.findViewById(R.id.lv_list);
        lv_list.setAdapter(ListDialogAdapter.get(c));
        lv_list.setOnItemClickListener(
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        curViewHolder = (ListDialogAdapter.ViewHolder) view.getTag();
                        curAlipayId = (AlipayId) parent.getAdapter().getItem(position);
                        if (countList == null) {
                            if (curViewHolder.cb.isChecked()) {
                                selectedList.remove(curAlipayId.id);
                                curViewHolder.cb.setChecked(false);
                            } else {
                                if (!selectedList.contains(curAlipayId.id)) {
                                    selectedList.add(curAlipayId.id);
                                }
                                curViewHolder.cb.setChecked(true);
                            }
                            Config.hasChanged = true;
                        } else {
                            showEdtDialog(parent.getContext());
                        }
                    }
                });
        lv_list.setOnItemLongClickListener(
                new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position
                            , long id) {
                        curAlipayId = (AlipayId) parent.getAdapter().getItem(position);
                        if (curAlipayId instanceof AlipayCooperate) {
                            showDeleteDialog(parent.getContext());
                        } else {
                            showOptionsDialog(parent.getContext());
                        }
                        return true;
                    }
                });
        return v;
    }

    static class OnBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (edt_find.length() <= 0) {
                return;
            }
            ListDialogAdapter listDialogAdapter = ListDialogAdapter.get(v.getContext());
            int index = -1;
            switch (v.getId()) {
                case R.id.btn_find_last:
                    // 下面Text要转String，不然判断equals会出问题
                    index = listDialogAdapter.findLast(edt_find.getText().toString());
                    break;

                case R.id.btn_find_next:
                    // 同上
                    index = listDialogAdapter.findNext(edt_find.getText().toString());
                    break;
                default:
            }
            if (index < 0) {
                Toast.makeText(v.getContext(), "Not found", Toast.LENGTH_SHORT).show();
            } else {
                lv_list.setSelection(index);
            }
        }
    }

    private static void showEdtDialog(Context c) {
        EditText editText = new EditText(c);
        AlertDialog edtDialog = new AlertDialog.Builder(c)
                .setTitle(curAlipayId.name)
                .setView(editText)
                .setPositiveButton("确定", new OnClickListener() {
                    Context c;

                    OnClickListener setContext(Context c) {
                        this.c = c;
                        return this;
                    }

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        int count = 0;
                        if (editText.length() > 0) {
                            try {
                                count = Integer.parseInt(editText.getText().toString());
                            } catch (Throwable t) {
                                return;
                            }
                        }
                        int index = selectedList.indexOf(curAlipayId.id);
                        if (count > 0) {
                            if (index < 0) {
                                selectedList.add(curAlipayId.id);
                                countList.add(count);
                            } else {
                                countList.set(index, count);
                            }
                            curViewHolder.cb.setChecked(true);
                        } else {
                            if (index >= 0) {
                                selectedList.remove(index);
                                countList.remove(index);
                            }
                            curViewHolder.cb.setChecked(false);
                        }
                        Config.hasChanged = true;

                        ListDialogAdapter.get(c).notifyDataSetChanged();
                    }
                }.setContext(c))
                .setNegativeButton("取消", null)
                .create();
        edtDialog.show();

        if (curAlipayId instanceof AlipayCooperate) {
            editText.setHint("grams");
        } else {
            editText.setHint("count");
        }
        int i = selectedList.indexOf(curAlipayId.id);
        if (i >= 0) {
            editText.setText(String.valueOf(countList.get(i)));
        } else {
            editText.getText().clear();
        }
    }


    private static void showOptionsDialog(Context c) {
        CharSequence[] ltem = {"查看ta的蚂蚁森林", "查看ta的蚂蚁庄园", "删除"};
        AlertDialog optionsDialog = new AlertDialog.Builder(c)
                .setTitle("Options")
                .setItems(ltem, new OnClickListener() {
                            Context c;

                            OnClickListener setContext(Context c) {
                                this.c = c;
                                return this;
                            }

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = null;
                                switch (which) {
                                    case 0:
                                        url = "alipays://platformapi/startapp?saId=10000007" +
                                                "&qrcode=https%3A%2F%2F60000002.h5app.alipay" +
                                                ".com%2Fwww%2Fhome.html%3FuserId%3D";
                                        break;
                                    case 1:
                                        url = "alipays://platformapi/startapp?saId=10000007" +
                                                "&qrcode=https%3A%2F%2F66666674.h5app.alipay" +
                                                ".com%2Fwww%2Findex.htm%3Fuid%3D";
                                        break;
                                    case 2:
                                        showDeleteDialog(c);
                                        break;
                                    default:
                                }
                                if (url != null) {
                                    Intent it = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(url + curAlipayId.id));
                                    c.startActivity(it);
                                }
                            }
                        }.setContext(c)
                ).setNegativeButton("取消", null).create();
        optionsDialog.show();

    }


    private static void showDeleteDialog(Context c) {
        AlertDialog deleteDialog = new AlertDialog.Builder(c)
                .setTitle("删除 " + curAlipayId.name)
                .setPositiveButton("确定", new OnClickListener() {
                    Context c;

                    OnClickListener setContext(Context c) {
                        this.c = c;
                        return this;
                    }

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (curAlipayId instanceof AlipayUser) {
                                FriendIdMap.removeIdMap(curAlipayId.id);
                                AlipayUser.remove(curAlipayId.id);
                            } else if (curAlipayId instanceof AlipayCooperate) {
                                CooperationIdMap.removeIdMap(curAlipayId.id);
                                AlipayCooperate.remove(curAlipayId.id);
                            }
                            selectedList.remove(curAlipayId.id);
                            ListDialogAdapter.get(c).exitFind();
                        }
                        ListDialogAdapter.get(c).notifyDataSetChanged();
                    }
                }.setContext(c))
                .setNegativeButton("取消", null)
                .create();
        deleteDialog.show();
    }


}
