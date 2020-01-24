package com.chii.antforest.hook;

import com.chii.antforest.util.Log;

/**
 * @author lenovo
 */
public class AntForestRpcCall {
    private static final String TAG = AntForestRpcCall.class.getCanonicalName();

    public static String rpcCallQueryEnergyRanking(ClassLoader loader, String startPoint) {
        try {
            String args1 = "[{\"av\":\"5\",\"ct\":\"android\",\"pageSize\":20,\"startPoint\":\""
                    + startPoint + "\"}]";
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.queryEnergyRanking", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallQueryEnergyRanking err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallQueryNextAction(ClassLoader loader, String userId) {
        try {
            String args1 = "[{\"canRobFlags\":\"F,F,F\",\"source\":\"_NO_SOURCE_\",\"userId\":\""
                    + userId + "\",\"version\":\"20181220\"}]";
            String res = RpcCall.invoke(loader, "alipay.antmember.forest.h5.queryNextAction", args1);

            return res;
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallQueryNextAction err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallCollectEnergy(ClassLoader loader, String userId, long bubbleId) {
        try {
            String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"userId\":\"" + userId + "\"}]";
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.collectEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallTransferEnergy(ClassLoader loader, String targetUser, String bizNo, int ordinal) {
        try {
            String args1 = "[{\"bizNo\":\"" + bizNo + ordinal + "\",\"targetUser\":\""
                    + targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"20181217\"}]";//
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.transferEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallTransferEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallForFriendCollectEnergy(ClassLoader loader, String targetUserId, long bubbleId) {
        try {
            String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"targetUserId\":\"" + targetUserId + "\"}]";
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.forFriendCollectEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallForFriendCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallQueryTaskList(ClassLoader loader) {
        try {
            String args1 = "[{\"version\":\"20191010\"}]";
            return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryTaskList", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallQueryTaskList err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCallReceiveTaskAward(ClassLoader loader, String taskType) {
        try {
            String args1 =
                    "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"ANTFOREST_TASK\",\"source\":\"ANTFOREST\",\"taskType\":\""
                            + taskType + "\"}]";
            return RpcCall.invoke(loader, "com.alipay.antiep.receiveTaskAward", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallReceiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private static String rpcCallQueryPropList(ClassLoader loader) {
        try {
            String args1 = "[{\"version\":\"\"}]"; //20181217
            return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryPropList", args1);
        } catch (Throwable t) {
            Log.i(TAG, "rpcCallQueryPropList err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }
}
