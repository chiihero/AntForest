package com.chii.antforest.task;

import android.content.Context;
import android.widget.Toast;

import com.chii.antforest.Main;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.Log;

public class AntForestToast {
    private static final String TAG = AntForestToast.class.getCanonicalName();
    public static Context context;

    public static void show(CharSequence cs) {
        try {
            if (context != null && Config.showToast()) {
                Main.handler.post(
                        new Runnable() {
                            CharSequence cs;

                            public Runnable setData(CharSequence c) {
                                cs = c;
                                return this;
                            }

                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(context, cs, 0).show();
                                } catch (Throwable t) {
                                    Log.i(TAG, "show.run err:");
                                    Log.printStackTrace(TAG, t);
                                }
                            }
                        }.setData(cs));
            }
        } catch (Throwable t) {
            Log.i(TAG, "show err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
