package com.chii.antforest;

import com.chii.antforest.hook.HookerDispatcher;
import com.chii.antforest.pojo.ClassMember;
import com.chii.antforest.ui.activity.MainActivity;
import com.chii.antforest.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    private static final String TAG = Main.class.getCanonicalName();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("HookLogic >> current package chiilog:" + lpparam.packageName);
        if (ClassMember.COM_CHII_ANTFOREST.equals(lpparam.packageName)) {
            Log.i(TAG, "chiilog" + lpparam.packageName);
            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), lpparam.classLoader,
                    "setModuleActive", boolean.class, new XC_MethodHook() {
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
