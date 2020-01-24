package com.chii.antforest.hook;

import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.chii.antforest.task.AntForestToast;
import com.chii.antforest.pojo.ClassMember;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.Log;

public class RpcCall {
    private static final String TAG = RpcCall.class.getCanonicalName();
    private static Method rpcCallMethod;
    private static Method getResponseMethod;
    private static Object curH5PageImpl;
    public static boolean sendXEdgeProBroadcast;

    public static String invoke(ClassLoader loader, String args0, String args1) {
        if (rpcCallMethod == null) {
            try {
                Class<?> rpcClazz = loader.loadClass(ClassMember.COM_ALIPAY_MOBILE_NEBULABIZ_RPC_H5RPCUTIL);
                Class<?> h5PageClazz = loader.loadClass(ClassMember.COM_ALIPAY_MOBILE_H5CONTAINER_API_H5PAGE);
                Class<?> jsonClazz = loader.loadClass(ClassMember.COM_ALIBABA_FASTJSON_JSONOBJECT);
                rpcCallMethod = rpcClazz.getMethod(
                        ClassMember.rpcCall, String.class, String.class, String.class,
                        boolean.class, jsonClazz, String.class, boolean.class, h5PageClazz,
                        int.class, String.class, boolean.class, int.class);
                Log.i(TAG, "get Old RpcCallMethod successfully");
            } catch (Throwable t) {
                Log.i(TAG, "get Old RpcCallMethod err:");
                //Log.printStackTrace(TAG, t);
            }

            if (rpcCallMethod == null) {
                try {
                    Class<?> h5PageClazz = loader.loadClass(ClassMember.COM_ALIPAY_MOBILE_H5CONTAINER_API_H5PAGE);
                    Class<?> jsonClazz = loader.loadClass(ClassMember.COM_ALIBABA_FASTJSON_JSONOBJECT);
                    Class<?> rpcClazz = loader.loadClass(ClassMember.COM_ALIPAY_MOBILE_NEBULAAPPPROXY_API_RPC_H5RPCUTIL);
                    rpcCallMethod = rpcClazz.getMethod(
                            ClassMember.rpcCall, String.class, String.class, String.class,
                            boolean.class, jsonClazz, String.class, boolean.class, h5PageClazz,
                            int.class, String.class, boolean.class, int.class, String.class);
                    Log.i(TAG, "get RpcCallMethod successfully");
                } catch (Throwable t) {
                    Log.i(TAG, "get RpcCallMethod err:");
                    //Log.printStackTrace(TAG, t);
                }
            }
        }

        try {
            Object o = null;
            switch (rpcCallMethod.getParameterTypes().length) {
                case 12:
                    o = rpcCallMethod.invoke(
                            null, args0, args1, "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
                    break;
                default:
                    o = rpcCallMethod.invoke(
                            null, args0, args1, "", true, null, null, false, curH5PageImpl, 0, "", false, -1, "");
            }
            String str = getResponse(o);
            Log.i(TAG, "argument: " + args0 + ", " + args1);
            Log.i(TAG, "response: " + str);
            return str;
        } catch (Throwable t) {
            Log.i(TAG, "invoke err:");
            Log.printStackTrace(TAG, t);
            if (t instanceof InvocationTargetException) {
                if (AntForestToast.context != null && sendXEdgeProBroadcast) {
                    sendXEdgeProBroadcast = false;
                    Intent it = new Intent("com.jozein.xedgepro.PERFORM");
                    it.putExtra("data", Config.xedgeproData());
                    AntForestToast.context.sendBroadcast(it);
                    Log.recordLog(t.getCause().getMessage() + ",发送XposedEdgePro广播", "");
                }
            }
        }
        return null;
    }

    public static String getResponse(Object resp) {
        try {
            if (getResponseMethod == null) {
                getResponseMethod = resp.getClass().getMethod(ClassMember.getResponse);
            }

            return (String) getResponseMethod.invoke(resp);
        } catch (Throwable t) {
            Log.i(TAG, "getResponse err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

}