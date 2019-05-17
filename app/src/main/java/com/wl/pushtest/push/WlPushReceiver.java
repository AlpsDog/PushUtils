package com.wl.pushtest.push;

import android.content.Context;
import android.util.Log;

import com.wl.pushutils.push.BasePushReceiver;

/**
 * @Project: PushUtils
 * @Package: com.wl.pushtest.push
 * @Author: HSL
 * @Time: 2019/05/17 16:12
 * @E-mail: xxx@163.com
 * @Description: 推送业务处理~
 */
public class WlPushReceiver implements BasePushReceiver {

    public static final String TAG = "wangli_push";

    /**
     * 通知栏通知到达
     *
     * @param context
     * @param str
     */
    @Override
    public void onPushNoticeArrive(Context context, String str) {
        Log.d(TAG, "onPushNoticeArrive: " + str);
    }

    /**
     * 通知栏通知点击
     *
     * @param context
     * @param str
     */
    @Override
    public void onPushNoticeClick(Context context, String str) {
        Log.d(TAG, "onPushNoticeClick: " + str);
    }

    /**
     * 透传消息
     *
     * @param context
     * @param str
     */
    @Override
    public void onReceiverMessage(Context context, String str) {
        Log.d(TAG, "onReceiverMessage: " + str);
    }

    /**
     * 华为TOKEN
     *
     * @param context
     * @param token
     */
    @Override
    public void onReceiverHuaWeiToken(Context context, String token) {
        Log.d(TAG, "onReceiverHuaWeiToken:" + token);
    }
}
