package com.chii.antforest.util;

import android.content.Context;

public class ContextUtil {

    public static Context selfContext;
    public static Context AntContext;

    public static Context getSelfContext() {
        return selfContext;
    }

    public static void setSelfContext(Context selfContext) {
        ContextUtil.selfContext = selfContext;
    }

    public static Context getAntContext() {
        return AntContext;
    }

    public static void setAntContext(Context antContext) {
        AntContext = antContext;
    }




}
