package com.chii.antforest.view;

import android.content.Context;
import android.widget.Toast;

import com.chii.antforest.hook.HookerDispatcher;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.Log;

public class AntForestToast {
    private static final String TAG = AntForestToast.class.getCanonicalName();
    public static Context context;

    public static void show(CharSequence cs) {
        try {
            Log.i("toast!!!!!!!!!!!!!!!!!!!!!!!!!!!", "selfContext" + (context != null));
            if (context != null && Config.showToast()) {
                HookerDispatcher.handler.post(
                        new Runnable() {
                            CharSequence cs;

                            Runnable setData(CharSequence c) {
                                cs = c;
                                return this;
                            }

                            @Override
                            public void run() {
                                try {
                                    Log.i("toast2!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                                            "selfContext" + (context != null));

                                    Toast.makeText(context, cs, Toast.LENGTH_SHORT).show();
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
