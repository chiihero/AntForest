package com.chii.antforest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;

import com.chii.antforest.hook.HookerDispatcher;
import com.chii.antforest.pojo.ClassMember;
import com.chii.antforest.hook.RpcCall;
import com.chii.antforest.task.AntCooperate;
import com.chii.antforest.task.AntFarm;
import com.chii.antforest.task.AntForest;
import com.chii.antforest.task.AntForestNotification;
import com.chii.antforest.task.AntForestToast;
import com.chii.antforest.task.AntMember;
import com.chii.antforest.task.AntSports;
import com.chii.antforest.ui.activity.MainActivity;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.Log;
import com.chii.antforest.util.Statistics;

import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    private static final String TAG = Main.class.getCanonicalName();
    private static PowerManager.WakeLock wakeLock;
    public static Handler handler;
    private static Runnable runnable;
    private static int times;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("HookLogic >> current package chiilog:" + lpparam.packageName);
        if (ClassMember.COM_CHII_ANTFOREST.equals(lpparam.packageName)) {
            Log.i(TAG, "chiilog" + lpparam.packageName);
            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), lpparam.classLoader, "setModuleActive", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    param.args[0] = true;
                }
            });
        }

        if (ClassMember.COM_EG_ANDROID_ALIPAYGPHONE.equals(lpparam.packageName)) {
            Log.i(TAG, "chiilog" + lpparam.packageName);
            HookerDispatcher hookerDispatcher = new HookerDispatcher();
            hookerDispatcher.hookLauncherService(lpparam.classLoader);
            hookerDispatcher.hookRpcCall(lpparam.classLoader);
        }
    }

}
