package com.chii.antforest.hook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;

import com.chii.antforest.pojo.HookClass;
import com.chii.antforest.task.AntCooperate;
import com.chii.antforest.task.AntFarm;
import com.chii.antforest.task.AntForest;
import com.chii.antforest.task.AntMember;
import com.chii.antforest.task.AntSports;
import com.chii.antforest.task.KBMember;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.Log;
import com.chii.antforest.util.Statistics;
import com.chii.antforest.view.AntForestNotification;
import com.chii.antforest.view.AntForestToast;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookerDispatcher {
    private static final String TAG = HookerDispatcher.class.getCanonicalName();
    private static PowerManager.WakeLock wakeLock;
    public static Handler handler;
    private static Runnable runnable;
    private static int times = 0;
//    private ScheduledExecutorService scheduledThreadPool;

    public void hookLauncherService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod(
                    HookClass.COM_ALIPAY_ANDROID_LAUNCHER_SERVICE_LAUNCHERSERVICE, loader,
                    "onCreate", new XC_MethodHook() {
                        ClassLoader loader;

                        public XC_MethodHook setData(ClassLoader cl) {
                            loader = cl;
                            return this;
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Service service = (Service) param.thisObject;
                            XposedBridge.log("HookLogic >>  chiilog:" + service.toString());
                            AntForestToast.context = service.getApplicationContext();
                            times = 0;
                            if (Config.stayAwake()) {
                                PowerManager pm =
                                        (PowerManager) service.getSystemService(Context.POWER_SERVICE);
                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                        service.getClass().getName());
                                wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
                            }
                            if (handler == null) {
                                handler = new Handler();
                            }
                            AntForestToast.show("antforest：进入支付宝");

//                            if (scheduledThreadPool == null) {
//                                scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
//                                        new BasicThreadFactory.Builder().namingPattern
//                                        ("example-schedule-pool-%d").daemon(true).build());
//                            }
//                            Log.recordLog("定时检测开始", "");
//                            scheduledThreadPool.scheduleAtFixedRate(new AntForest(loader,
//                            times), 1000, Config.checkInterval()*60*1000, TimeUnit.MILLISECONDS);
//                            times+=1;
                            if (runnable == null) {
                                runnable = new Runnable() {
                                    Service service;
                                    ClassLoader loader;

                                    public Runnable setData(Service s, ClassLoader cl) {
                                        service = s;
                                        loader = cl;
                                        return this;
                                    }

                                    @Override
                                    public void run() {
                                        Config.shouldReload = true;
                                        RpcCall.sendXEdgeProBroadcast = true;
                                        Statistics.resetToday();

                                        AntForest.checkEnergyRanking(loader, times);
                                        AntCooperate.start(loader, times);
                                        AntFarm.start(loader);
                                        AntMember.receivePoint(loader, times);
                                        AntSports.start(loader, times);
                                        KBMember.start(loader);
                                        if (Config.collectEnergy() || Config.enableFarm()) {
                                            handler.postDelayed(this, Config.checkInterval());
                                        } else {
                                            AntForestNotification.stop(service, false);
                                        }
                                        times = (times + 1) % (3600_000 / Config.checkInterval());
                                    }
                                }.setData(service, loader);
                            }
                            if (Config.collectEnergy() || Config.enableFarm()) {
                                AntForestNotification.start(service);
                                handler.post(runnable);
                            }
                        }
                    }.setData(loader));
            Log.i(TAG, "hook onCreate successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onCreate err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod(HookClass.COM_ALIPAY_ANDROID_LAUNCHER_SERVICE_LAUNCHERSERVICE, loader, "onDestroy", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (wakeLock != null) {
                        wakeLock.release();
                        wakeLock = null;
                    }
                    Service service = (Service) param.thisObject;
                    AntForestNotification.stop(service, false);
                    AntForestNotification.setContentText("支付宝前台服务被销毁");
                    Log.recordLog("支付宝前台服务被销毁", "");
                    handler.removeCallbacks(runnable);
                    if (Config.autoRestart()) {
                        AlarmManager alarmManager =
                                (AlarmManager) service.getSystemService(Context.ALARM_SERVICE);
                        Intent it = new Intent();
                        it.setClassName(HookClass.COM_EG_ANDROID_ALIPAYGPHONE,
                                HookClass.COM_ALIPAY_ANDROID_LAUNCHER_SERVICE_LAUNCHERSERVICE);
                        PendingIntent pi = PendingIntent.getService(service, 0, it,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                System.currentTimeMillis() + 1000, pi);
                    }
                }
            });
            Log.i(TAG, "hook onDestroy successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onDestroy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public void hookRpcCall(ClassLoader loader) {
        try {
            Class<?> clazz =
                    loader.loadClass(HookClass.COM_ALIPAY_MOBILE_NEBULAAPPPROXY_API_RPC_H5APPRPCUPDATE);
            Class<?> H5PageClazz =
                    loader.loadClass(HookClass.COM_ALIPAY_MOBILE_H5CONTAINER_API_H5PAGE);
            XposedHelpers.findAndHookMethod(
                    clazz, HookClass.matchVersion, H5PageClazz, Map.class, String.class,
                    XC_MethodReplacement.returnConstant(false));
            Log.i(TAG, "hook " + HookClass.matchVersion + " successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook " + HookClass.matchVersion + " err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
