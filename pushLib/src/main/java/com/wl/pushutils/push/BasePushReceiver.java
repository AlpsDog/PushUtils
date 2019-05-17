package com.wl.pushutils.push;

import android.content.Context;

/**
 * @Project: PushUtils
 * @Package: com.wl.pushutils.push.xiaomi
 * @Author: HSL
 * @Time: 2019/05/17 16:05
 * @E-mail: xxx@163.com
 * @Description: 这个人太懒，没留下什么踪迹~
 */
public interface BasePushReceiver {
    /**
     * 通知栏通知到达
     *
     * @param context
     * @param str
     */
    void onPushNoticeArrive(Context context, String str);

    /**
     * 通知栏通知点击
     *
     * @param context
     * @param str
     */
    void onPushNoticeClick(Context context, String str);

    /**
     * 透传消息
     *
     * @param context
     * @param str
     */
    void onReceiverMessage(Context context, String str);

    /**
     * 华为TOKEN
     *
     * @param context
     * @param token
     */
    void onReceiverHuaWeiToken(Context context, String token);
}
