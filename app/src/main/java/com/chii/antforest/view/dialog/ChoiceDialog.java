package com.chii.antforest.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.chii.antforest.util.Config;

public class ChoiceDialog {
    public enum ChoiceDialogMode {
        SEND_TYPE, RECALL_ANIMAL_TYPE
    }

    public static void showChoiceDialog(Context c, CharSequence title, CharSequence[] array,
                                        int num, ChoiceDialogMode em) {
        AlertDialog sendTypeDialog = new AlertDialog.Builder(c)
                .setTitle(title)
                .setSingleChoiceItems(array, num,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (em) {
                                    case SEND_TYPE:
                                        Config.setSendType(i);
                                        break;
                                    case RECALL_ANIMAL_TYPE:
                                        Config.setRecallAnimalType(i);
                                        break;
                                    default:
                                }
                            }
                        }).setPositiveButton("确认", null)
                .create();
        sendTypeDialog.show();
    }


}
