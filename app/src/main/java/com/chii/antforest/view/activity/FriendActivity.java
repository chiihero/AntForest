package com.chii.antforest.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chii.antforest.R;
import com.chii.antforest.pojo.AlipayCooperate;
import com.chii.antforest.pojo.AlipayId;
import com.chii.antforest.pojo.AlipayUser;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.CooperationIdMap;
import com.chii.antforest.util.FriendIdMap;
import com.chii.antforest.view.adapter.ListDialogAdapter;
import com.chii.antforest.view.adapter.ListFriendAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class FriendActivity extends Activity implements ListFriendAdapter.Callback {
    public static String TAG = "FriendActivity";

    @BindView(R.id.textView_friend_list)
    TextView textViewFriendList;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.listView_friend)
    ListView listViewFriend;
    @BindView(R.id.edt_find)
    EditText edtFind;
    @BindView(R.id.btn_find_last)
    Button btnFindLast;
    @BindView(R.id.btn_find_next)
    Button btnFindNext;
    private Map<String, List> maps;
    ListFriendAdapter mAdapter;
    private static AlipayId curAlipayId;
    private List<String> selectedList;
    private List<Integer> countList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Log.i(TAG, "onCreate: " + type);

        initList(type);
    }

    @OnItemClick(R.id.listView_friend)
    public void onItemClick(AdapterView<?> parent, View view, int position
            , long id) {
        Toast.makeText(this, view.getTag().toString(), Toast.LENGTH_LONG).show();
    }


    @OnItemLongClick(R.id.listView_friend)
    public void onItemLongClick(AdapterView<?> parent, View view, int position
            , long id) {

        curAlipayId = (AlipayId) parent.getAdapter().getItem(position);
        if (curAlipayId instanceof AlipayCooperate) {
            showDeleteDialog(parent.getContext());
        } else {
            showOptionsDialog(parent.getContext());
        }
    }


    @OnClick({R.id.textView1, R.id.textView2, R.id.textView3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                break;
            case R.id.textView2:
                break;
            case R.id.textView3:
                break;
            default:
        }
    }

    @OnClick({R.id.btn_find_last, R.id.btn_find_next})
    public void find(View view) {
        if (edtFind.length() <= 0) {
            return;
        }
        int index = -1;
        switch (view.getId()) {
            case R.id.btn_find_last:
                index = mAdapter.findLast(edtFind.getText().toString());

                break;
            case R.id.btn_find_next:
                index = mAdapter.findNext(edtFind.getText().toString());
                break;
            default:
        }
        if (index < 0) {
            Toast.makeText(view.getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            listViewFriend.setSelection(index);
        }
    }

    private void initList(String type) {
        maps = new HashMap<>();
        //是否需要取反
        List<Boolean> hasFalse = new ArrayList<>();
        if ("forest".equals(type)) {
            textViewFriendList.setText("蚂蚁森林好友列表");
            textView1.setText("偷能量");
            textView2.setText("帮收");
            textView3.setText("浇水");

            //不偷能量列表
            maps.put("0", Config.getDontCollectList());
            hasFalse.add(false);
            maps.put("0.showCount", null);
            //不帮收能量列表
            maps.put("1", Config.getDontHelpCollectList());
            hasFalse.add(false);
            maps.put("1.showCount", null);
            //浇水列表
            maps.put("2", Config.getWaterFriendList());
            hasFalse.add(true);
            maps.put("2.showCount", Config.getWaterCountList());
            //取反列表
            maps.put("hasFalse", hasFalse);

        } else if ("farm".equals(type)) {
            textViewFriendList.setText("蚂蚁庄园好友列表");
            textView1.setText("赶鸡");
            textView2.setText("喂鸡");
            textView3.setText("通知");

            //不赶鸡列表
            maps.put("0", Config.getDontSendFriendList());
            hasFalse.add(false);
            maps.put("0.showCount", null);

            //喂鸡列表
            maps.put("1", Config.getFeedFriendAnimalList());
            hasFalse.add(true);
            maps.put("1.showCount", Config.getFeedFriendCountList());

            //不提醒列表
            maps.put("2", Config.getDontNotifyFriendList());
            hasFalse.add(false);
            maps.put("2.showCount", null);

            //取反列表
            maps.put("hasFalse", hasFalse);
        }
        mAdapter = new ListFriendAdapter(this, this, AlipayUser.getList(), maps);


        listViewFriend.setAdapter(mAdapter);
    }

    private void showOptionsDialog(Context c) {
        CharSequence[] ltem = {"查看ta的蚂蚁森林", "查看ta的蚂蚁庄园", "删除"};
        AlertDialog optionsDialog = new AlertDialog.Builder(c)
                .setTitle(curAlipayId.name)
                .setItems(ltem, new DialogInterface.OnClickListener() {
                            Context c;

                            DialogInterface.OnClickListener setContext(Context c) {
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

    private void showEdtDialog(Context c) {
        //todo
        EditText editText = new EditText(c);
        AlertDialog edtDialog = new AlertDialog.Builder(c)
                .setTitle(curAlipayId.name)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    Context c;

                    DialogInterface.OnClickListener setContext(Context c) {
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
                        } else {
                            if (index >= 0) {
                                selectedList.remove(index);
                                countList.remove(index);
                            }
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

    private void showDeleteDialog(Context c) {
        AlertDialog deleteDialog = new AlertDialog.Builder(c)
                .setTitle("删除 " + curAlipayId.name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    Context c;

                    DialogInterface.OnClickListener setContext(Context c) {
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
                            maps.get("0").remove(curAlipayId.id);
                            maps.get("1").remove(curAlipayId.id);
                            maps.get("2").remove(curAlipayId.id);

                            mAdapter.exitFind();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }.setContext(c))
                .setNegativeButton("取消", null)
                .create();
        deleteDialog.show();
    }


    @Override
    public void switchClick(CompoundButton buttonView, boolean isChecked) {
        int i = 0;
        //todo

        switch (buttonView.getId()) {
            case R.id.switch_1:
                selectedList = maps.get("0");
                i = (int) buttonView.getTag(R.id.sc1);
                curAlipayId = (AlipayId) mAdapter.getItem(i);
                if (maps.get("0.showCount") == null) {
                    if (!isChecked) {
                        selectedList.remove(curAlipayId.id);
                    } else if (!selectedList.contains(curAlipayId.id)) {
                        selectedList.add(curAlipayId.id);
                    }
                } else {
                    countList = maps.get("0.showCount");
                    Toast.makeText(this, "我是switch_1 " + i, Toast.LENGTH_SHORT).show();
                    showEdtDialog(this);
                }


                break;
            case R.id.switch_2:
                selectedList = maps.get("1");
                i = (int) buttonView.getTag(R.id.sc2);
                curAlipayId = (AlipayId) mAdapter.getItem(i);
                if (maps.get("1.showCount") == null) {
                    if (!isChecked) {
                        selectedList.remove(curAlipayId.id);
                    } else if (!selectedList.contains(curAlipayId.id)) {
                        selectedList.add(curAlipayId.id);
                    }
                }else {
                    countList = maps.get("1.showCount");
                    Toast.makeText(this, "我是switch_2 " + i,
                            Toast.LENGTH_SHORT).show();
                    showEdtDialog(this);
                }
                break;
            case R.id.switch_3:
                selectedList = maps.get("2");
                i = (int) buttonView.getTag(R.id.sc3);
                curAlipayId = (AlipayId) mAdapter.getItem(i);
                if (maps.get("2.showCount") == null) {

                    if (!isChecked) {
                        selectedList.remove(curAlipayId.id);
                    } else if (!selectedList.contains(curAlipayId.id)) {
                        selectedList.add(curAlipayId.id);
                    }
                } else {
                    countList = maps.get("2.showCount");
                    Toast.makeText(this, "我是switch_3 " + i,
                            Toast.LENGTH_SHORT).show();
                    showEdtDialog(this);
                }
                break;
            default:

        }
    }
}
