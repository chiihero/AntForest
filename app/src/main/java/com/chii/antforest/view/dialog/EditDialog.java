package com.chii.antforest.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

import com.chii.antforest.util.Config;

public class EditDialog {

    public enum EditMode {
        CHECK_INTERVAL, THREAD_COUNT, ADVANCE_TIME, COLLECT_INTERVAL,
        COLLECT_TIMEOUT, RETURN_WATER_30, RETURN_WATER_20, RETURN_WATER_10,
        MIN_EXCHANGE_COUNT, LATEST_EXCHANGE_TIME
    }
    public static void showEditDialog(Context c, CharSequence title,Object str, EditMode em) {
        EditText edt = new EditText(c);
        edt.setText(String.valueOf(str));
        AlertDialog editDialog = new AlertDialog.Builder(c)
                .setTitle(title)
                .setView(edt)
                .setPositiveButton(
                        "确认",
                        (dialogInterface, i) -> {
                            int input = Integer.parseInt(edt.getText().toString());
                            switch (em) {
                                case CHECK_INTERVAL:
                                    if (input > 0) {
                                        Config.setCheckInterval(input * 60_000);
                                    }
                                    break;
                                case THREAD_COUNT:
                                    if (input >= 0) {
                                        Config.setThreadCount(input);
                                    }
                                    break;

                                case ADVANCE_TIME:
                                    Config.setAdvanceTime(input);
                                    break;

                                case COLLECT_INTERVAL:
                                    if (input >= 0) {
                                        Config.setCollectInterval(input);
                                    }
                                    break;

                                case COLLECT_TIMEOUT:
                                    if (input > 0) {
                                        Config.setCollectTimeout(input * 1_000);
                                    }
                                    break;

                                case RETURN_WATER_30:
                                    if (input >= 0) {
                                        Config.setReturnWater30(input);
                                    }
                                    break;

                                case RETURN_WATER_20:
                                    if (input >= 0) {
                                        Config.setReturnWater20(input);
                                    }
                                    break;

                                case RETURN_WATER_10:
                                    if (input >= 0) {
                                        Config.setReturnWater10(input);
                                    }
                                    break;

                                case MIN_EXCHANGE_COUNT:
                                    if (input >= 0) {
                                        Config.setMinExchangeCount(input);
                                    }
                                    break;

                                case LATEST_EXCHANGE_TIME:
                                    if (input >= 0 && input < 24) {
                                        Config.setLatestExchangeTime(input);
                                    }
                                    break;
                                default:
                            }
                        })
                .create();
        editDialog.show();

    }


}
